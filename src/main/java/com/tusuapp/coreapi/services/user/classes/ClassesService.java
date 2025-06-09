package com.tusuapp.coreapi.services.user.classes;

import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.TutorSlot;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.TutorSlotRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public ResponseEntity<?> initiateBooking(InitiateBookingReqDto initiateBookingReqDto) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCreatedAt(LocalDateTime.now());
        bookingRequest.setStudentId(getCurrentUserId());
        bookingRequest.setTutorId(Long.valueOf(initiateBookingReqDto.getTutor_id()));
        bookingRequest.setSlotId(initiateBookingReqDto.getSlot_id());
        bookingRequest.setSubjectId(initiateBookingReqDto.getSubject_id());
        Optional<TutorSlot> tutorSlot = tutorSlotRepo.findById(initiateBookingReqDto.getSlot_id());
        if(tutorSlot.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slot found");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        bookingRequest.setStartTime(LocalDateTime.parse(tutorSlot.get().getFromDatetime().toString(),formatter));
        bookingRequest.setEndTime(LocalDateTime.parse(tutorSlot.get().getToDatetime().toString(),formatter));
        Optional<TutorDetails> tutorDetails = tutorDetailRepo.findById(bookingRequest.getTutorId());
        bookingRequest.setHourlyCharge(tutorDetails.get().getHourlyCharge());
        bookingRequest.setStatus(STATUS_CHECKOUT);
        bookingRequest = bookingRequestRepo.save(bookingRequest);
        return ResponseEntity.ok(bookingRequest);
    }

    public ResponseEntity<?> getClassDetails(String id) {
        Optional<BookingRequest> bookingRequestOpt = bookingRequestRepo.findById(Long.valueOf(id));
        if(bookingRequestOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BookingRequest bookingRequest = bookingRequestOpt.get();

        if(Objects.equals(bookingRequest.getStudentId(), getCurrentUserId()) &&
                Objects.equals(bookingRequest.getTutorId(), getCurrentUserId())){
            return ResponseEntity.notFound().build();
        }

        Optional<User> studentDetails = userRepo.findById(bookingRequest.getStudentId());
        Optional<User> tutorDetails = userRepo.findById(bookingRequest.getTutorId());
        if (studentDetails.isEmpty() || tutorDetails.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutor or student not exists, may be they have deactivated");
        }
        BookingRequestDto response = BookingRequestDto.fromBookingRequest(
                bookingRequest,
                UserDto.fromUser(studentDetails.get()),
                UserDto.fromUser(tutorDetails.get()));
        return ResponseEntity.ok(response);
    }
}
