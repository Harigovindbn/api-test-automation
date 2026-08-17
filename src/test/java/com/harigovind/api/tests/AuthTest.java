package com.harigovind.api.tests;

import com.harigovind.api.client.AuthClient;
import com.harigovind.api.config.ApiConfig;
import com.harigovind.api.model.AuthRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Authentication")
class AuthTest {
    private final AuthClient authClient = new AuthClient();

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Valid credentials return an authentication token")
    void validCredentialsReturnToken() {
        Response response = authClient.authenticate(
                new AuthRequest(ApiConfig.username(), ApiConfig.password()));

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/auth-token-schema.json"));
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Invalid credentials are rejected without returning a token")
    void invalidCredentialsAreRejected() {
        Response response = authClient.authenticate(
                new AuthRequest("invalid-user", "invalid-password"));

        response.then().statusCode(200).contentType(ContentType.JSON);
        assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials");
        assertThat(response.jsonPath().getString("token")).isNull();
    }
}
