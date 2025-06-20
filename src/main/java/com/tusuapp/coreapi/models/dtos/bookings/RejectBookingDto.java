package com.tusuapp.coreapi.models.dtos.bookings;


import lombok.Data;

@Data
public class RejectBookingDto {

    private String message;
    private Long bookingId;

}
