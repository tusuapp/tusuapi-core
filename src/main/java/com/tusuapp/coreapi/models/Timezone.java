package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Timezone created by Rithik S(coderithik@gmail.com)
 **/
@Data
@Entity
@Table(name = "timezones")
public class Timezone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Column(name = "timezone", length = 125)
    private String timezone;

    @Column(name = "gmt_offset")
    private Float gmtOffset;

    @Column(name = "dst_offset")
    private Float dstOffset;

    @Column(name = "raw_offset")
    private Float rawOffset;
}