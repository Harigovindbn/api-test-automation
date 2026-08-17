package com.harigovind.api.tests;

import com.harigovind.api.client.AuthClient;
import com.harigovind.api.client.BookingClient;
import com.harigovind.api.config.ApiConfig;
import com.harigovind.api.model.AuthRequest;
import com.harigovind.api.model.Booking;
import com.harigovind.api.model.CreateBookingResponse;
import com.harigovind.api.util.TestDataFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Booking lifecycle")
class BookingCrudTest {
    private final BookingClient bookingClient = new BookingClient();
    private final AuthClient authClient = new AuthClient();

    @Test
    @Tag("smoke")
    @Tag("regression")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Create, read, update, patch, and delete a booking")
    void completeBookingLifecycle() {
        Booking originalBooking = TestDataFactory.newBooking();
        Booking updatedBooking = TestDataFactory.updatedBooking();
        Map<String, Object> patch = TestDataFactory.partialUpdate();
        String token = authenticate();
        int bookingId = -1;
        boolean deleted = false;

        try {
            Response createResponse = Allure.step(
                    "Create a unique booking",
                    () -> bookingClient.createBooking(originalBooking));

            createResponse.then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body(matchesJsonSchemaInClasspath("schemas/create-booking-schema.json"));

            CreateBookingResponse created = createResponse.as(CreateBookingResponse.class);
            bookingId = created.bookingid();
            assertThat(bookingId).isPositive();
            assertThat(created.booking()).isEqualTo(originalBooking);

            int persistedBookingId = bookingId;
            Response getResponse = Allure.step(
                    "Read the created booking",
                    () -> bookingClient.getBooking(persistedBookingId));
            getResponse.then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
            assertThat(getResponse.as(Booking.class)).isEqualTo(originalBooking);

            Response updateResponse = Allure.step(
                    "Replace the booking",
                    () -> bookingClient.updateBooking(
                            persistedBookingId, updatedBooking, token));
            updateResponse.then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
            assertThat(updateResponse.as(Booking.class)).isEqualTo(updatedBooking);

            Response patchResponse = Allure.step(
                    "Partially update the booking",
                    () -> bookingClient.partiallyUpdateBooking(
                            persistedBookingId, patch, token));
            patchResponse.then()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
            assertThat(patchResponse.jsonPath().getString("additionalneeds"))
                    .isEqualTo("Airport pickup");
            assertThat(patchResponse.jsonPath().getInt("totalprice"))
                    .isEqualTo(725);

            Response persistedResponse = Allure.step(
                    "Verify the server persisted the patch",
                    () -> bookingClient.getBooking(persistedBookingId));
            persistedResponse.then().statusCode(200);
            assertThat(persistedResponse.jsonPath().getString("additionalneeds"))
                    .isEqualTo("Airport pickup");
            assertThat(persistedResponse.jsonPath().getInt("totalprice")).isEqualTo(725);

            Response deleteResponse = Allure.step(
                    "Delete the booking",
                    () -> bookingClient.deleteBooking(persistedBookingId, token));
            assertThat(deleteResponse.statusCode()).isEqualTo(201);
            deleted = true;

            Response deletedResponse = Allure.step(
                    "Verify the booking no longer exists",
                    () -> bookingClient.getBooking(persistedBookingId));
            assertThat(deletedResponse.statusCode()).isEqualTo(404);
        } finally {
            if (bookingId > 0 && !deleted) {
                bookingClient.deleteBooking(bookingId, token);
            }
        }
    }

    private String authenticate() {
        Response response = authClient.authenticate(
                new AuthRequest(ApiConfig.username(), ApiConfig.password()));
        response.then().statusCode(200);

        String token = response.jsonPath().getString("token");
        assertThat(token).as("authentication token").isNotBlank();
        return token;
    }
}
