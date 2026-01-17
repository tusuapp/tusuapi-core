package com.tusuapp.coreapi.models.dtos.payments;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayoutCreationDto {
    private Double amount;
}
