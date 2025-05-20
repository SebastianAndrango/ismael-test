package com.ismael.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PokeApiResource {

    @ConfigProperty(name = "poke.api.url")
    String pokeApiBaseUrl;

    @Inject
    EventBus event;

    private final ObjectMapper mapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchMoveList(@Context HttpHeaders httpHeaders){
        
        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(pokeApiBaseUrl))
            .header("User-Agent", "Quarkus-PokeClient/1.0")
            .header("Accept-Encoding", "identity")
            .GET();

            Map<String, List<String>> originalHeaders = httpHeaders.getRequestHeaders();
            originalHeaders.forEach((name,values)->{
                if(!"Host".equalsIgnoreCase(name)){
                    values.forEach(value-> requestBuilder.header(name, value));
                }
            });

            HttpRequest request = requestBuilder.build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            byte[] responseBody = response.body();

            try(InputStream inputStream = new ByteArrayInputStream(responseBody);
                InputStream decompressedStream = isResponseGzipped(responseBody) ? new GZIPInputStream(inputStream) : inputStream){
                    JsonNode jsonNode = mapper.readTree(decompressedStream);
                    String jsonString = mapper.writeValueAsString(jsonNode);

                    event.publish("poke-api", jsonString);

                    return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
                }

        } catch (Exception exception) {
            return Response.status(Response.Status.BAD_GATEWAY)
            .entity("Error while fetching data from PokeApi: " + exception.getMessage())
            .build();
        }
    }

    /**
     * Check if the given byte array is compressed using GZIP format.
     * @param content
     * @return true if GZIP-compressed, otherwise false 
     */
    private boolean isResponseGzipped(byte[] content){
        final int GZIP_MAGIC_1 = 0x1F;
        final int GZIP_MAGIC_2 = 0x8B;

        if (content == null || content.length < 2) {
            return false;
        }

        int firstByte = content[0] & 0XFF;
        int secondByte = content[1] & 0XFF;

        return firstByte == GZIP_MAGIC_1 && secondByte == GZIP_MAGIC_2;
    }


}
