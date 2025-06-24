package com.tusuapp.coreapi.models.dtos;

import com.tusuapp.coreapi.models.Country;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
public class CountryDto {

    private Long id;

    private String name;


    private String countryCode;

    private String isoCode;

    private Integer dialCode;

    public static CountryDto fromCountry(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCountryCode(country.getCountryCode());
        dto.setIsoCode(country.getIsoCode());
        dto.setDialCode(country.getDialCode());
        return dto;
    }
}
