package com.esben.kaa.quarkus.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateModel {
    @JsonProperty("DKK")
    double DKK;
    @JsonProperty("USD")
    double USD;
}
