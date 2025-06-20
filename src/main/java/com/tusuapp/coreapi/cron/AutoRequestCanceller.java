package com.tusuapp.coreapi.cron;


import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.BookingSession;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.BookingSessionRepo;
import com.tusuapp.coreapi.services.user.CreditService;
import com.tusuapp.coreapi.services.user.bookings.BookingService;
import com.tusuapp.coreapi.services.user.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

@Component
public class AutoRequestCanceller {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRequestRepo bookingRequestRepo;

    @Autowired
    private CreditService creditService;

    @Autowired
    private ClassService classService;

    @Autowired
    private BookingSessionRepo bookingSessionRepo;


    @Scheduled(fixedDelay = 1000)
    public void autoCancelRequested() {
        //Credit back the points
        //Change status to auto-cancelled
        //save
//        System.out.println("CRON JOB");
        List<String> statues = listOf("requested", "rescheduled");
        LocalDateTime utcTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
//        System.out.println(utcTime);
        List<BookingRequest> requests = bookingRequestRepo.findAllByStartTimeBeforeAndStatusIn(utcTime, statues);
//        System.out.println("Found " + requests.size() + " requests to be cancelled");
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void autoCreateBookingSession() {
        LocalDateTime utcTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        List<BookingRequest> requests = bookingRequestRepo.findAllAcceptedBookingsWithinNext15Minutes(utcTime,utcTime.plusMinutes(15));
        List<BookingSession> sessions = startSessions(requests);
        System.out.println("Started " + sessions.size() + " sessions");
    }

    public List<BookingSession> startSessions(List<BookingRequest> bookingRequests) {
        List<BookingSession> newSessions = new ArrayList<>();
        for (BookingRequest request : bookingRequests) {
            boolean exists = bookingSessionRepo.existsByBooking(request);
            if (!exists) {
                BookingSession session = new BookingSession();
                session.setBooking(request);
                newSessions.add(session);
                request.setStatus(BookingConstants.STATUS_INPROGRESS);
            }
        }
        if (!newSessions.isEmpty()) {
            newSessions = bookingSessionRepo.saveAll(newSessions);
            bookingRequestRepo.saveAll(bookingRequests);
        }
        return newSessions;
    }

}
