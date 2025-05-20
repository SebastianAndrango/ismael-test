package com.ismael.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class LogHandler implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LogHandler.class);
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        InputStream entityStream = requestContext.getEntityStream();
        String body = new BufferedReader(new InputStreamReader(entityStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        LOGGER.infof(">> [INCOMING]  %s %s - Body: %s",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                body);

        requestContext.setEntityStream(
            new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))
        );
    }

    @Override
    public void filter(ContainerRequestContext requestcontext, ContainerResponseContext responseContext) {
        Object responseEntity = responseContext.getEntity();
        String responseBody = (responseEntity != null ? responseEntity.toString() : "");

        LOGGER.infof("<< [OUTGOING] %s %s Status: %d - Body: %s",
                requestcontext.getMethod(),
                requestcontext.getUriInfo().getRequestUri(),
                responseContext.getStatus(),
                responseBody);
    }
}
