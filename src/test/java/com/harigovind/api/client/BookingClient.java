package com.harigovind.api.client;

import com.harigovind.api.model.Booking;
import com.harigovind.api.spec.ApiSpecifications;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Client that keeps HTTP details out of test scenarios. */
public final class BookingClient {
    public Response ping() {
        return given()
                .spec(ApiSpecifications.request())
                .when()
                .get("/ping");
    }

    public Response getBookingIds() {
        return given()
                .spec(ApiSpecifications.request())
                .when()
                .get("/booking");
    }

    public Response getBooking(int bookingId) {
        return given()
                .spec(ApiSpecifications.request())
                .pathParam("bookingId", bookingId)
                .when()
                .get("/booking/{bookingId}");
    }

    public Response createBooking(Booking booking) {
        return given()
                .spec(ApiSpecifications.request())
                .body(booking)
                .when()
                .post("/booking");
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        return given()
                .spec(ApiSpecifications.request())
                .pathParam("bookingId", bookingId)
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .put("/booking/{bookingId}");
    }

    public Response partiallyUpdateBooking(
            int bookingId, Map<String, Object> fields, String token) {
        return given()
                .spec(ApiSpecifications.request())
                .pathParam("bookingId", bookingId)
                .header("Cookie", "token=" + token)
                .body(fields)
                .when()
                .patch("/booking/{bookingId}");
    }

    public Response deleteBooking(int bookingId, String token) {
        return given()
                .spec(ApiSpecifications.request())
                .pathParam("bookingId", bookingId)
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/{bookingId}");
    }
}
