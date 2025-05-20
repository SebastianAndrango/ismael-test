package com.ismael.api;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.ismael.dto.PokeResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v2/move")
@RegisterRestClient(configKey = "poke-api")
public interface PokeApi {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<PokeResponse> getMoves();
}
