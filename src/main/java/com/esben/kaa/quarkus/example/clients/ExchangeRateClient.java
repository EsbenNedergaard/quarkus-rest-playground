package com.esben.kaa.quarkus.example.clients;

import com.esben.kaa.quarkus.example.responses.ExchangeRateResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient(baseUri = "https://v6.exchangerate-api.com")
public interface ExchangeRateClient {

    @GET
    @Path("/v6/{apiKey}/pair/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    ExchangeRateResponse getExchangeRate(@PathParam("apiKey") String apiKey, @PathParam("from") String from, @PathParam("to") String to);
}
