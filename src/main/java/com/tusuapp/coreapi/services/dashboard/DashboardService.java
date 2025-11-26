package com.tusuapp.coreapi.services.dashboard;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.UserWallet;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.CreditPointRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.ROLE_TUTOR_ID;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BookingRequestRepo bookingsRepo;
    private final CreditPointRepo creditPointRepo;
    private final UserInfoRepo userRepo;

    public ResponseEntity<?> getTutorDashboard() {
        Long tutorId = getCurrentUserId();
        JSONObject response = new JSONObject();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<BookingRequest> requestList = bookingsRepo.findAllByTutorIdAndStatusIn(tutorId, listOf("requested"), pageable);
        List<BookingRequestDto> requestsDto = requestList.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("bookingRequests", requestsDto);
        List<BookingRequest> upcomingAccepted = bookingsRepo
                .findByTutorIdAndStatusInAndStartTimeAfterOrderByStartTimeAsc(
                        tutorId,
                        listOf(BookingConstants.STATUS_ACCEPTED, BookingConstants.STATUS_INPROGRESS),
                        getCurrentUTCTime()
                );
        List<BookingRequestDto> upcomingDto = upcomingAccepted.stream().map(BookingRequestDto::fromBookingRequest).toList();
        response.put("upcomingClasses", upcomingDto);
        Optional<UserWallet> credits = creditPointRepo.findByUserId(getCurrentUserId());
        response.put("totalEarning", 0);
        credits.ifPresent(userWallet -> response.put("totalEarning", userWallet.getBalance()));
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> getStudentDashboard(){
        Pageable pageable = PageRequest.of(0, 4, Sort.by("createdAt").descending());
        List<BookingRequest> requestList = bookingsRepo.findAllByStudentIdAndStatusIn(getCurrentUserId(), listOf("accepted", "requested"), pageable);
        List<BookingRequestDto> requestDtos = requestList.stream().map(BookingRequestDto::fromBookingRequest).toList();
        List<User> tutors = userRepo.findByRole(ROLE_TUTOR_ID);
        List<UserDto> tutorDtos = tutors.stream().map(UserDto::fromUser).toList();
        JSONObject response = new JSONObject();
        response.put("upcomingClasses", requestDtos);
        response.put("popularTutors", tutorDtos);
        return ResponseEntity.ok(response.toMap());
    }

}
