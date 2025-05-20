package com.ismael.resource;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.ismael.api.PokeApi;
import com.ismael.dto.PokeResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api/v2/move")
public class PokeApiRestResource {

    @Inject
    @RestClient
    PokeApi pokeApiClient;

    @GET
    @Operation(summary = "Fetch pokemon list from external API", description = "Call the external PokeApi to retrieve and return a list of pokemon movements.")
    public CompletionStage<PokeResponse> getAllMoves() {
        return pokeApiClient.getMoves();
    }
}
