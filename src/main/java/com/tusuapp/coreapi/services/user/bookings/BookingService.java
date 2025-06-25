package com.tusuapp.coreapi.services.user.bookings;

import com.stripe.exception.StripeException;
import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.*;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.models.dtos.bookings.ChangeBookingStatusDto;
import com.tusuapp.coreapi.models.dtos.bookings.RescheduleBookingDto;
import com.tusuapp.coreapi.repositories.*;
import com.tusuapp.coreapi.services.payments.stripe.StripeService;
import com.tusuapp.coreapi.services.user.CreditService;
import com.tusuapp.coreapi.services.user.notifications.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tusuapp.coreapi.constants.BookingConstants.*;
import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isStudent;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getUtcDateTime;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.transformBookingReqFromUTC;

/**
 * ClassesService created by Rithik S(coderithik@gmail.com)
 **/
@Service
public class BookingService {

    @Autowired
    private TutorSlotRepo tutorSlotRepo;

    @Autowired
    private BookingRequestRepo bookingRepo;

    @Autowired
    private UserInfoRepo userRepo;

    @Autowired
    private TutorDetailRepo tutorDetailRepo;

    @Autowired
    CreditPointRepo creditPointRepo;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookingRescheduleRepo rescheduleRepo;


    public ResponseEntity<?> getUserClasses(String typesParam, Integer limit) {
        List<String> types = List.of(typesParam.split(","));
        Long currentId = getCurrentUserId();

        List<BookingRequest> requests;
        if (isStudent()) {
            requests = bookingRepo
                    .findAllByStudentIdAndStatusIn(currentId, types);
        } else {
            requests = bookingRepo
                    .findAllByTutorIdAndStatusIn(currentId, types);
        }

        List<BookingRequestDto> dtos = requests.stream()
                .map(BookingRequestDto::new).toList();
        if (limit != null && dtos.size() > 3) {
            dtos = dtos.subList(0, limit);
        }
        return ResponseEntity.ok(Map.of("bookings", dtos));
    }

    @Transactional
    public ResponseEntity<?> purchaseClass(Long bookingRequestId) throws StripeException {
        BookingRequest bookingRequest = bookingRepo.findById(bookingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("No Booking Found"));
        if (!creditService.currentUserHasEnoughCredit(bookingRequest.getTotalAmount())) {
            double remainingCreditsRequired = bookingRequest.getTotalAmount() - creditService.getCurrentUserBalance();
            return stripeService.purchaseRemainingCredits(bookingRequest.getId(), remainingCreditsRequired);
        }
        bookingRequest.setStatus(BookingConstants.STATUS_REQUESTED);
        bookingRequest.setIsPaid(true);
        creditService.reduceCredits(bookingRequest.getStudent().getId(),bookingRequest.getTotalAmount());
        bookingRequest = bookingRepo.save(bookingRequest);
        notificationService.sendBookingNotifications(bookingRequest.getStudent(),bookingRequest.getTutor());
        return ResponseEntity.ok(new BookingRequestDto(bookingRequest));
    }

    public ResponseEntity<?> initiateBooking(InitiateBookingReqDto initiateBookingReqDto) throws Exception {
        Optional<TutorSlot> tutorSlot = tutorSlotRepo.findById(initiateBookingReqDto.getSlot_id());
        if (tutorSlot.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slot found");
        }
        //check slot is already booked
        if (tutorSlot.get().getIsBooked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slot already booked");
        }
        //check if already time passed
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCreatedAt(LocalDateTime.now());
        User student = userRepo.findById(getCurrentUserId()).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        bookingRequest.setStudent(student);
        bookingRequest.setSlotId(initiateBookingReqDto.getSlot_id());
        bookingRequest.setSubjectId(initiateBookingReqDto.getSubject_id());
        User tutor = userRepo.findById(tutorSlot.get().getTutorId()).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        bookingRequest.setTutor(tutor);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        bookingRequest.setStartTime(LocalDateTime.parse(tutorSlot.get().getFromDatetime().toString(), formatter));
        bookingRequest.setEndTime(LocalDateTime.parse(tutorSlot.get().getToDatetime().toString(), formatter));
        Optional<TutorDetails> tutorDetails = tutorDetailRepo.findByUserId(bookingRequest.getTutor().getId());
        bookingRequest.setHourlyCharge(tutorDetails.get().getHourlyCharge());
        bookingRequest.setStatus(STATUS_CHECKOUT);
        bookingRequest = bookingRepo.save(bookingRequest);
        return ResponseEntity.ok(BookingRequestDto.fromBookingRequest(bookingRequest));
    }

    public ResponseEntity<?> getBookingDetails(Long id) {
        Optional<BookingRequest> bookingRequestOpt = bookingRepo.findById(id);
        if (bookingRequestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BookingRequest bookingRequest = bookingRequestOpt.get();
        if (Objects.equals(bookingRequest.getStudent().getId(), getCurrentUserId()) &&
                Objects.equals(bookingRequest.getTutor().getId(), getCurrentUserId())) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> studentDetails = userRepo.findById(bookingRequest.getStudent().getId());
        Optional<User> tutorDetails = userRepo.findById(bookingRequest.getTutor().getId());
        if (studentDetails.isEmpty() || tutorDetails.isEmpty()) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Student or tutor might be deactivated");
        }
        BookingRequestDto response = BookingRequestDto.fromBookingRequest(
                bookingRequest,
                UserDto.fromUser(studentDetails.get()),
                UserDto.fromUser(tutorDetails.get()));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> changeBookingStatus(ChangeBookingStatusDto changeStatusDto) {
        BookingRequest request = bookingRepo.findById(changeStatusDto.getBookingId())
                .orElseThrow(()->new IllegalArgumentException("No booking found"));
        if(!request.getTutor().getId().equals(getCurrentUserId())){
            return ResponseEntity.notFound().build();
        }
        switch (changeStatusDto.getStatus()){
            case STATUS_REJECTED -> rejectBooking(request, changeStatusDto);
            case STATUS_ACCEPTED -> acceptBooking(request,changeStatusDto);
            default -> throw new IllegalArgumentException("Invalid status");
        }
        request = bookingRepo.save(request);
        return ResponseEntity.ok(BookingRequestDto.fromBookingRequest(request));
    }

    private void acceptBooking(BookingRequest request, ChangeBookingStatusDto changeStatusDto) {
        request.setStatus(STATUS_ACCEPTED);
        TutorSlot tutorSlot = tutorSlotRepo.findById(request.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Tutor slot no longer exists"));
        tutorSlot.setIsBooked(true);
        tutorSlotRepo.save(tutorSlot);
    }

    private void rejectBooking(BookingRequest request, ChangeBookingStatusDto changeStatusDto) {
        request.setStatus(STATUS_REJECTED);
        request.setRejectionReason(changeStatusDto.getMessage());
    }


}
