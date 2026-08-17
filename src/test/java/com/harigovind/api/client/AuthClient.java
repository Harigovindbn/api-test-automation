package com.harigovind.api.client;

import com.harigovind.api.model.AuthRequest;
import com.harigovind.api.spec.ApiSpecifications;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/** Client for Restful Booker authentication operations. */
public final class AuthClient {
    public Response authenticate(AuthRequest credentials) {
        return given()
                .spec(ApiSpecifications.request())
                .body(credentials)
                .when()
                .post("/auth");
    }
}
