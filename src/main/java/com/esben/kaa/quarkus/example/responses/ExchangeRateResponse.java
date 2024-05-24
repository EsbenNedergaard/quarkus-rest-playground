package com.esben.kaa.quarkus.example.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ExchangeRateResponse {

    private double conversion_rate;

    private String base_code;

    private String target_code;
}
