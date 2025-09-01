package com.tusuapp.coreapi.services.user.bookings;


import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.RescheduleRequest;
import com.tusuapp.coreapi.models.dtos.bookings.BookingRequestDto;
import com.tusuapp.coreapi.models.dtos.bookings.RescheduleBookingDto;
import com.tusuapp.coreapi.models.dtos.bookings.RescheduleRequestDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.BookingRescheduleRepo;
import com.tusuapp.coreapi.services.user.CreditService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import static com.tusuapp.coreapi.constants.BookingConstants.STATUS_RESCHEDULED;
import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isTutor;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getUtcDateTime;

@Service
public class RescheduleService {

    @Autowired
    private BookingRequestRepo bookingRepo;

    @Autowired
    private BookingRescheduleRepo rescheduleRepo;

    @Autowired
    private CreditService creditService;

    public static final String REJECTED = "rejected";
    public static final String ACCEPTED = "accepted";
    public static final String REQUESTED = "requested";


    public ResponseEntity<?> getPendingReschedules(String status){
        List<RescheduleRequest> requestList;
        if(isTutor()){
            requestList = rescheduleRepo.findAllByTutorIdAndStatus(getCurrentUserId(), status);
        }else{
            requestList = rescheduleRepo.findAllByStudentIdAndStatus(getCurrentUserId(), status);
        }
        List<RescheduleRequestDto> requestDtos = requestList.stream().map(RescheduleRequestDto::fromRescheduleRequest).toList();
        return ResponseEntity.ok(requestDtos);
    }

    public ResponseEntity<?> rescheduleBooking(RescheduleBookingDto rescheduleDto) {
        BookingRequest request = bookingRepo.findById(rescheduleDto.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("No booking found"));
        //check if any already open
        List<RescheduleRequest> existingReschedules = rescheduleRepo
                .findAllByBookingIdAndStatus(request.getId(), ACCEPTED);
        if(!existingReschedules.isEmpty()){
            return errorResponse(HttpStatus.CONFLICT,"An open reschedule request already exists");
        }

        RescheduleRequest reschedule = new RescheduleRequest();
        reschedule.setBooking(request);
        reschedule.setStudent(request.getStudent());
        reschedule.setTutor(request.getTutor());
        reschedule.setStatus(REQUESTED);
        LocalDateTime localDateTime = LocalDateTime.parse(rescheduleDto.getStartTime());
        localDateTime = getUtcDateTime(localDateTime);
        if(localDateTime.isBefore(getCurrentUTCTime())){
            return errorResponse(HttpStatus.BAD_REQUEST,"Cannot reschedule to past time");
        }
        System.out.println("utc time reschedule");
        System.out.println(localDateTime);
        reschedule.setProposedDateTime(localDateTime);
        reschedule.setMessage(reschedule.getMessage());
        reschedule = rescheduleRepo.save(reschedule);
        request.setIsRescheduled(true);
        request.setStatus(STATUS_RESCHEDULED);
        bookingRepo.save(request);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> updateRescheduleRequest(Long id, String status) {
        RescheduleRequest rescheduleRequest = rescheduleRepo
                .findById(id)
                .orElseThrow(()->new IllegalArgumentException("No request found"));
        if(!status.equals(ACCEPTED) && !status.equals(REJECTED)){
            return errorResponse(HttpStatus.BAD_REQUEST, "Status can only be accept or reject");
        }
        if(status.equals(REJECTED)){
            //credit points back, cancel
            creditService.addCredits(rescheduleRequest.getStudent().getId(),rescheduleRequest.getBooking().getHourlyCharge());
            rescheduleRequest.setStatus(REJECTED);
            rescheduleRequest.getBooking().setIsRescheduled(true);
            rescheduleRequest.getBooking().setStatus(STATUS_RESCHEDULED);
            bookingRepo.save(rescheduleRequest.getBooking());
        }else {
            saveBookingRequest(rescheduleRequest);
            rescheduleRequest.setStatus(ACCEPTED);
        }
        rescheduleRequest = rescheduleRepo.save(rescheduleRequest);
        return ResponseEntity.ok().build();
    }

    private BookingRequest saveBookingRequest(RescheduleRequest rescheduleRequest) {
        BookingRequest original = rescheduleRequest.getBooking();
        BookingRequest clone = new BookingRequest();
        try {
            BeanUtils.copyProperties(clone, original);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        clone.setId(null);
        clone.setStatus(BookingConstants.STATUS_REQUESTED);
        clone.setStartTime(rescheduleRequest.getProposedDateTime());
        clone.setEndTime(clone.getStartTime().plusHours(1));
        clone.setParentBookingId(rescheduleRequest.getBooking().getId());
        return bookingRepo.save(clone);
    }

}
