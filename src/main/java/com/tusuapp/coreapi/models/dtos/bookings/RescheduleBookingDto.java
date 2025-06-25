package com.tusuapp.coreapi.models.dtos.bookings;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RescheduleBookingDto {

    private String startTime;
    private String date;
    private String endTime;
    private Long bookingId;

}
