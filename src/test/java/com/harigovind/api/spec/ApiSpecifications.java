package com.harigovind.api.spec;

import com.harigovind.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/** Shared request configuration used by every API client. */
public final class ApiSpecifications {
    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private ApiSpecifications() {
    }

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.baseUrl())
                .addHeader("Accept", "application/json")
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
