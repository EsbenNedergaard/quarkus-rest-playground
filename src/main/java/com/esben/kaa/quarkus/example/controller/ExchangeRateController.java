package com.esben.kaa.quarkus.example.controller;

import com.esben.kaa.quarkus.example.clients.ExchangeRateClient;
import com.esben.kaa.quarkus.example.models.ExchangeRateModel;
import com.esben.kaa.quarkus.example.responses.ExchangeRateResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequestScoped
@Path("/exchange-rate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "exchangeRate", description = "Exchange rates")
public class ExchangeRateController {

    @Inject
    @RestClient
    ExchangeRateClient exchangeRateClient;


    @GET
    @Path("/dkk-usd")
    @Operation(summary = "Gets exchange rate from DKK to USD")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRateModel.class)))})
    public Response getDkkToUsdRate(@Parameter(description = "API key for accessing the exchange rate service", required = true)
                                    @QueryParam("apiKey") String apiKey) {
        ExchangeRateResponse clientExchangeRate = exchangeRateClient.getExchangeRate(apiKey, "DKK", "USD");

        ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
        int baseNo = 100;
        exchangeRateModel.setDKK(baseNo);
        exchangeRateModel.setUSD(clientExchangeRate.getConversion_rate() * baseNo);
        return Response.ok(exchangeRateModel).build();
    }
}
