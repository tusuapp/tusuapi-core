package com.tusuapp.coreapi.services.user.classes;


import com.tusuapp.coreapi.models.BookingSession;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.BookingSessionRepo;
import com.tusuapp.coreapi.services.bbb.BBBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.tusuapp.coreapi.utils.OTPUtil.generateOTP;
import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isStudent;

@Service
public class ClassService {

    @Autowired
    private BookingSessionRepo bookingSessionRepo;

    @Autowired
    private BBBService bbbService;


    public ResponseEntity<?> startClass(Long bookingId) throws Exception {
        BookingSession session = bookingSessionRepo.findByBooking_Id(bookingId).
                orElseThrow(() -> new IllegalArgumentException("Class might not be started yet."));
        if (!(Objects.equals(session.getBooking().getTutor().getId(), getCurrentUserId())) &&
                !(Objects.equals(session.getBooking().getStudent().getId(), getCurrentUserId()))) {
            return ResponseEntity.notFound().build();
        }
        String meetingId = "tusu_booking_" + session.getId();
        session.setMeetingId(meetingId);
        if (bbbService.isMeetingRunning(meetingId)) {
            if (session.getStudentBBBUrl() == null) {
                String studentUrl = generateBBBUrl(session.getBooking().getStudent(), meetingId, session.getStudentPass());
                session.setStudentBBBUrl(studentUrl);
            }
            if (session.getTutorBBBUrl() == null) {
                String tutorUrl = generateBBBUrl(session.getBooking().getTutor(), meetingId, session.getTutorPass());
                session.setTutorBBBUrl(tutorUrl);
            }
            if (isStudent()) {
                session.setStudentJoined(true);
            } else {
                session.setTutorJoined(true);
            }
            session = bookingSessionRepo.save(session);
            return ResponseEntity.ok(session);
        }
        String createdUrl = bbbService.generateCreateUrl(session);
        String response = restClient(RestClient.builder(), createdUrl).get().retrieve().body(String.class);
        String studentUrl = generateBBBUrl(session.getBooking().getStudent(), meetingId, session.getStudentPass());
        String tutorUrl = generateBBBUrl(session.getBooking().getTutor(), meetingId, session.getTutorPass());
        if (response.contains("FAILED")) {
            return errorResponse(HttpStatus.BAD_GATEWAY, "Unable to create session");
        }
        session.setStudentBBBUrl(studentUrl);
        session.setTutorBBBUrl(tutorUrl);
        if (isStudent()) {
            session.setStudentJoined(true);
        } else {
            session.setTutorJoined(true);
        }
        session = bookingSessionRepo.save(session);
//        removeUnauthorizedDetails(session);
        return ResponseEntity.ok(session);
    }


    private void removeUnauthorizedDetails(BookingSession session) {
        if (isStudent()) {
            session.setTutorPass(null);
            session.setTutorBBBUrl(null);
        } else {
            session.setStudentPass(null);
            session.setStudentBBBUrl(null);
        }
    }

    private String generateBBBUrl(User user, String meetingId, String pass) throws Exception {
        String userName = user.getFullName();
        return bbbService.generateJoinUrl(userName, meetingId, pass);
    }

    RestClient restClient(RestClient.Builder builder, String createdUrl) {
        return builder
                .baseUrl(createdUrl)
                .build();
    }

}
