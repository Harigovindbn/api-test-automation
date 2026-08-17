package com.harigovind.api.tests;

import com.harigovind.api.client.BookingClient;
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
@Feature("Booking contracts")
class BookingContractTest {
    private static final int UNKNOWN_BOOKING_ID = 99_999_999;
    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Booking IDs satisfy the published response contract")
    void bookingIdsMatchSchema() {
        bookingClient.getBookingIds()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("schemas/booking-ids-schema.json"));
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Unknown booking ID returns HTTP 404")
    void unknownBookingReturnsNotFound() {
        Response response = bookingClient.getBooking(UNKNOWN_BOOKING_ID);

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.asString()).containsIgnoringCase("not found");
    }
}
