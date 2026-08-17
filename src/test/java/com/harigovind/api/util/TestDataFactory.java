package com.harigovind.api.util;

import com.harigovind.api.model.Booking;
import com.harigovind.api.model.BookingDates;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/** Creates isolated test data so CRUD scenarios do not depend on shared records. */
public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static Booking newBooking() {
        LocalDate checkin = LocalDate.now().plusDays(14);
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        return new Booking(
                "Hari-" + suffix,
                "Automation",
                420,
                true,
                new BookingDates(checkin.toString(), checkin.plusDays(3).toString()),
                "Breakfast");
    }

    public static Booking updatedBooking() {
        LocalDate checkin = LocalDate.now().plusDays(30);
        return new Booking(
                "Harigovind",
                "Nair",
                650,
                false,
                new BookingDates(checkin.toString(), checkin.plusDays(5).toString()),
                "Late checkout");
    }

    public static Map<String, Object> partialUpdate() {
        return Map.of("additionalneeds", "Airport pickup", "totalprice", 725);
    }
}
