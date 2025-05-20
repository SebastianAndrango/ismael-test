package com.ismael.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import com.ismael.dto.TextConcatRequest;
import com.ismael.exception.GenericException;
import com.ismael.service.TextConcatService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/test")
public class TextConcatResource {
    @Inject
    TextConcatService textConcatService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Concatenates five parameters", description = "Receives five text parameters in JSON format, validates them, and returns their concatenation.")
    public Response concatParams(@RequestBody(description = "Five params", required = true) TextConcatRequest request) {
        String[] params = new String[] {
                request.text1,
                request.text2,
                request.text3,
                request.text4,
                request.text5,
        };

        if (params.length != 5) {
            throw new GenericException("Only five parameters");
        }

        String paramsConcatenates = textConcatService.concat(params);
        return Response.ok(paramsConcatenates).build();
    }
}
