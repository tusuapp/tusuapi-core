package com.tusuapp.coreapi.services.dashboard;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;


@Service
public class DashboardService {

    @Autowired
    private BookingRequestRepo bookingsRepo;


    public ResponseEntity<?> getTutorDashboard() {
        Long tutorId = getCurrentUserId();
        JSONObject response = new JSONObject();
        List<BookingRequest> requestList = bookingsRepo.findAllByTutorIdAndStatusIn(tutorId, listOf("requested"));
        System.out.println(requestList.size());
        List<BookingRequestDto> requestsDto = requestList.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("bookingRequests", requestsDto);
        List<BookingRequest> upcomingAccepted = bookingsRepo
                .findByTutorIdAndStatusAndStartTimeAfterOrderByStartTimeAsc(
                        tutorId,
                        BookingConstants.STATUS_ACCEPTED,
                        getCurrentUTCTime()
                );
        List<BookingRequestDto> upcomingDto = upcomingAccepted.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("upcomingClasses", upcomingDto);
        response.put("totalEarning",40);
        return ResponseEntity.ok(response.toMap());

    }

}
