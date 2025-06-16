package com.tusuapp.coreapi.models.dtos.bookings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiateBookingReqDto {
    private Long slot_id;
    private Long subject_id;
}
