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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
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
        List<String> statues = listOf("requested", "rescheduled");
        LocalDateTime utcTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        List<BookingRequest> requests = bookingRequestRepo.findAllByStartTimeBeforeAndStatusIn(utcTime, statues);
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public void autoCreateBookingSession() {
        LocalDateTime utcTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        List<BookingRequest> requests = bookingRequestRepo.findAllAcceptedBookingsWithinNext15Minutes(utcTime,utcTime.plusMinutes(15));
        List<BookingSession> sessions = startSessions(requests);
        System.out.println("Started " + sessions.size() + " sessions");
    }

    @Scheduled(fixedDelay = 13 * 1000)
    public void autoCompleteBookingSession() {
        List<BookingRequest> requests = bookingRequestRepo.findAllByStatusAndEndTimeLessThanEqual(BookingConstants.STATUS_INPROGRESS,getCurrentUTCTime());
        stopSessions(requests);
        System.out.println("Stopped " + requests.size() + " sessions");
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

    public void stopSessions(List<BookingRequest> requests) {
        requests.forEach((r)->{
            r.setStatus(BookingConstants.STATUS_COMPLETED);
            creditService.addCredits(r.getTutor().getId(),r.getHourlyCharge());
        });
        bookingRequestRepo.saveAll(requests);
    }


}
