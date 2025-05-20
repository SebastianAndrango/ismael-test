package com.ismael.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ResourceTest {
    @Test
    public void ok() {
        String params = """
                {
                    "text1": "string1",
                    "text2": "string2",
                    "text3": "string3",
                    "text4": "string4",
                    "text5": "string5"
                }
                """;

        given()
                .body(params)
                .contentType("application/json")
                .when()
                .post("/api/v1/test")
                .then()
                .statusCode(200)
                .body(is("string1string2string3string4string5"));
    }

}
