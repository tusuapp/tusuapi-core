package com.tusuapp.coreapi.services.user.classes;


import com.tusuapp.coreapi.models.BookingSession;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.BookingSessionRepo;
import com.tusuapp.coreapi.services.bbb.BBBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.tusuapp.coreapi.utils.OTPUtil.generateOTP;
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
        session.setStudentPass(generateOTP(4));
        session.setTutorPass(generateOTP(4));
        String meetingId = "tusu_booking_" + session.getId();
        session.setMeetingId(meetingId);
        String createdUrl = bbbService.generateCreateUrl(session);
        String response = restClient(RestClient.builder(), createdUrl).get().retrieve().body(String.class);
        if (response.contains("FAILED")) {
            return ResponseEntity.badRequest().body(response);
        }
        String studentUrl = generateBBBUrl(session.getBooking().getStudent(), meetingId, session.getStudentPass());
        String tutorUrl = generateBBBUrl(session.getBooking().getTutor(), meetingId, session.getTutorPass());
        session.setStudentBBBUrl(studentUrl);
        session.setTutorBBBUrl(tutorUrl);
        if (isStudent()) {
            session.setStudentJoined(true);
        } else {
            System.out.println("Tutor true");
            session.setTutorJoined(true);
        }
        session = bookingSessionRepo.save(session);
        removeUnauthorizedDetails(session);
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
