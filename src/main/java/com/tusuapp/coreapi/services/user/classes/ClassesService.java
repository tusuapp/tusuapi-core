package com.tusuapp.coreapi.services.user.classes;

import com.stripe.exception.StripeException;
import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.*;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.repositories.*;
import com.tusuapp.coreapi.services.payments.stripe.StripeService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.tusuapp.coreapi.constants.BookingConstants.STATUS_CHECKOUT;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isStudent;

/**
 * ClassesService created by Rithik S(coderithik@gmail.com)
 **/
@Service
public class ClassesService {

    @Autowired
    private TutorSlotRepo tutorSlotRepo;

    @Autowired
    private BookingRequestRepo bookingRequestRepo;

    @Autowired
    private UserInfoRepo userRepo;

    @Autowired
    private TutorDetailRepo tutorDetailRepo;

    @Autowired
    CreditPointRepo creditPointRepo;

    @Autowired
    private StripeService stripeService;

    public List<BookingRequest> getBookingRequests() {
        return bookingRequestRepo.findAll();
    }

    public ResponseEntity<?> getMyClasses(String typesParam) {
        List<String> types = List.of(typesParam.split(","));
        Long currentId = getCurrentUserId();

        List<BookingRequest> requests;
        if (isStudent()) {
            requests = bookingRequestRepo
                    .findAllByStudentIdAndStatusIn(currentId, types);
        } else {
            requests = bookingRequestRepo
                    .findAllByTutorIdAndStatusIn(currentId, types);
        }

        JSONObject response = new JSONObject();
        response.put("bookings", requests.toArray());
        return ResponseEntity.ok(response.toMap());

    }

    @Transactional
    public ResponseEntity<?> purchaseClass(Long bookingRequestId) throws StripeException {
        BookingRequest bookingRequest = bookingRequestRepo.findById(bookingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("No Booking Found"));
        //Check if the user has enough balance
        CredPointMaster creditPoint = creditPointRepo.findByStudentId(bookingRequest.getStudentId())
                .orElseThrow(()-> new IllegalArgumentException("Credit point not found for student"));
        if(bookingRequest.getTotalAmount() > creditPoint.getBalance() ){
            double remainingCreditsRequired = bookingRequest.getTotalAmount() - creditPoint.getBalance();
            return stripeService.purchaseRemainingCredits(bookingRequest.getId(), remainingCreditsRequired);
        }
        bookingRequest.setStatus(BookingConstants.STATUS_REQUESTED);
        bookingRequest.setIsPaid(true);
        TutorSlot tutorSlot = tutorSlotRepo.findById(bookingRequest.getSlotId())
                .orElseThrow(()->new IllegalArgumentException("Tutor slot no longer exists"));
        tutorSlot.setIsBooked(true);
        tutorSlotRepo.save(tutorSlot);
        bookingRequest = bookingRequestRepo.save(bookingRequest);
        return ResponseEntity.ok(bookingRequest);
    }

    public ResponseEntity<?> initiateBooking(InitiateBookingReqDto initiateBookingReqDto) throws Exception{
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
        bookingRequest.setStudentId(getCurrentUserId());
        bookingRequest.setSlotId(initiateBookingReqDto.getSlot_id());
        bookingRequest.setSubjectId(initiateBookingReqDto.getSubject_id());
        bookingRequest.setTutorId(tutorSlot.get().getTutorId());
        System.out.println(bookingRequest.getTutorId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        bookingRequest.setStartTime(LocalDateTime.parse(tutorSlot.get().getFromDatetime().toString(), formatter));
        bookingRequest.setEndTime(LocalDateTime.parse(tutorSlot.get().getToDatetime().toString(), formatter));
        Optional<TutorDetails> tutorDetails = tutorDetailRepo.findByUserId(bookingRequest.getTutorId());
        System.out.println(tutorDetails.get().getId());
        bookingRequest.setHourlyCharge(tutorDetails.get().getHourlyCharge());
        bookingRequest.setStatus(STATUS_CHECKOUT);
        bookingRequest = bookingRequestRepo.save(bookingRequest);
        return ResponseEntity.ok(bookingRequest);
    }

    public ResponseEntity<?> getClassDetails(String id) {
        Optional<BookingRequest> bookingRequestOpt = bookingRequestRepo.findById(Long.valueOf(id));
        if (bookingRequestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BookingRequest bookingRequest = bookingRequestOpt.get();

        if (Objects.equals(bookingRequest.getStudentId(), getCurrentUserId()) &&
                Objects.equals(bookingRequest.getTutorId(), getCurrentUserId())) {
            return ResponseEntity.notFound().build();
        }

        Optional<User> studentDetails = userRepo.findById(bookingRequest.getStudentId());
        Optional<User> tutorDetails = userRepo.findById(bookingRequest.getTutorId());
        if (studentDetails.isEmpty() || tutorDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutor or student not exists, may be they have deactivated");
        }
        BookingRequestDto response = BookingRequestDto.fromBookingRequest(
                bookingRequest,
                UserDto.fromUser(studentDetails.get()),
                UserDto.fromUser(tutorDetails.get()));
        return ResponseEntity.ok(response);
    }
}
