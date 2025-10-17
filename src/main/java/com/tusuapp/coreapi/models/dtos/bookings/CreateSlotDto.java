package com.tusuapp.coreapi.models.dtos.bookings;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSlotDto {

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    @JsonProperty("isTrialSlot")
    private boolean isTrialSlot;

}
