package com.tusuapp.coreapi.models.dtos.bookings;


import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.RescheduleRequest;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.utils.converters.TimeZoneConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getUtcDateTime;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.transformRescheduleFromUTC;

@Data
@ToString
public class RescheduleRequestDto {

    private Long id;
    private BookingRequestDto booking;
//    private UserDto student;
//    private UserDto tutor;
    private String message;
    private boolean isAccepted;
    private LocalDateTime proposedDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RescheduleRequestDto fromRescheduleRequest(RescheduleRequest request){
        transformRescheduleFromUTC(request);
        RescheduleRequestDto dto = new RescheduleRequestDto();
        dto.setAccepted(request.isAccepted());
        dto.setId(request.getId());
        dto.setBooking(BookingRequestDto.fromBookingRequest(request.getBooking()));
        dto.setMessage(request.getMessage());
        dto.setProposedDateTime(request.getProposedDateTime());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }

}
