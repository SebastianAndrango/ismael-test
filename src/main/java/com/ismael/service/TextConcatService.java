package com.ismael.service;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ismael.exception.GenericException;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TextConcatService {

    private static final Pattern FORBIDDEN_PATTERN = Pattern.compile(
            "(?i).*(\\b(SELECT|INSERT|UPDATE|DELETE|DROP|TRUNCATE|ALTER|EXEC|UNION|OR|AND)\\b|['\";#`]|--).*");

    @Inject
    EventBus event;

    /**
     * Concatenates an array of string params
     * @param params
     * @return
      */
    public String concat(String[] params) {
        String result = Arrays.stream(params)
                .peek(this::ensureValid)
                .collect(Collectors.joining());

        event.send("concatenate-text", result);
        return result;
    }

    /**
     * Validates a string param to ensure that it is not null or blank, 
     * and that it does not contain SQL code or dangerous characters.
     * @param param
      */
    private void ensureValid(String param) {
        if (param == null || param.isBlank()) {
            throw new GenericException("All params must be non-null and non-blanck.");
        }
        if (FORBIDDEN_PATTERN.matcher(param).matches()) {
            throw new GenericException("Params contains forbidden content");
        }
    }

}
