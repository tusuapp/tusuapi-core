package com.tusuapp.coreapi.models.dtos.bookings;


import lombok.Data;

@Data
public class ChangeBookingStatusDto {

    private String message;
    private Long bookingId;
    private String status;

}
