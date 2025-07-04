package com.tusuapp.coreapi.utils.converters;


import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.TutorSlot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserTimeZone;

public class TimeZoneConverter {


    public static void transformTutorSlotsFromUTC(List<TutorSlot> slots) {
        slots.forEach(slot -> {
            slot.setFromDatetime(getLocalDateTime(slot.getFromDatetime()));
            slot.setToDatetime(getLocalDateTime(slot.getToDatetime()));
        });
    }

    public static void transformBookingRequestsFromUTC(List<BookingRequest> requests) {
        requests.forEach(req -> {
            req.setStartTime(getLocalDateTime(req.getStartTime()));
            req.setEndTime(getLocalDateTime(req.getEndTime()));
        });
    }

    public static void transformTutorSlotFromUTC(TutorSlot slot) {
        slot.setFromDatetime(getLocalDateTime(slot.getFromDatetime()));
        slot.setToDatetime(getLocalDateTime(slot.getToDatetime()));
    }

    public static void transformBookingReqFromUTC(BookingRequest slot) {
        slot.setStartTime(getLocalDateTime(slot.getStartTime()));
        slot.setEndTime(getLocalDateTime(slot.getEndTime()));
    }

    private static LocalDateTime getLocalDateTime(LocalDateTime utcTime) {
        ZoneId targetZone = ZoneId.of(getCurrentUserTimeZone());
        ZonedDateTime utcZoned = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime converted = utcZoned.withZoneSameInstant(targetZone);
        return converted.toLocalDateTime();
    }

    public static LocalDateTime getUtcDateTime(LocalDateTime tzDateTime) {
        ZoneId targetZone = ZoneId.of(getCurrentUserTimeZone());
        ZonedDateTime zonedDateTime = tzDateTime.atZone(targetZone);
        ZonedDateTime utcZoned = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime getCurrentUTCTime() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
    }

}
