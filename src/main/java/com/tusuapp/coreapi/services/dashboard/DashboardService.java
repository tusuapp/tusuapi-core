package com.tusuapp.coreapi.services.dashboard;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.CredPointMaster;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.CreditPointRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;


@Service
public class DashboardService {

    @Autowired
    private BookingRequestRepo bookingsRepo;

    @Autowired
    private CreditPointRepo creditPointRepo;

    public ResponseEntity<?> getTutorDashboard() {
        Long tutorId = getCurrentUserId();
        JSONObject response = new JSONObject();
        List<BookingRequest> requestList = bookingsRepo.findAllByTutorIdAndStatusIn(tutorId, listOf("requested"));
        System.out.println(requestList.size());
        List<BookingRequestDto> requestsDto = requestList.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("bookingRequests", requestsDto);
        List<BookingRequest> upcomingAccepted = bookingsRepo
                .findByTutorIdAndStatusInAndStartTimeAfterOrderByStartTimeAsc(
                        tutorId,
                        listOf(BookingConstants.STATUS_ACCEPTED,BookingConstants.STATUS_INPROGRESS),
                        getCurrentUTCTime()
                );
        List<BookingRequestDto> upcomingDto = upcomingAccepted.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("upcomingClasses", upcomingDto);
        Optional<CredPointMaster> credits = creditPointRepo.findByUserId(getCurrentUserId());
        response.put("totalEarning",40);
        credits.ifPresent(credPointMaster -> response.put("totalEarning", credPointMaster.getBalance()));
        return ResponseEntity.ok(response.toMap());

    }

}
