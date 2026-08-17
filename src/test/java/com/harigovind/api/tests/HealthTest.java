package com.harigovind.api.tests;

import com.harigovind.api.client.BookingClient;
import com.harigovind.api.config.ApiConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Service health")
class HealthTest {
    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("API health endpoint is available within the response-time limit")
    void healthEndpointIsAvailable() {
        Response response = bookingClient.ping();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.time()).isLessThan(ApiConfig.responseTimeLimitMs());
    }
}
