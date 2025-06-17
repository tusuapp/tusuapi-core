package com.tusuapp.coreapi.utils.converters;


import com.tusuapp.coreapi.models.TutorSlot;

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

    public static void transformTutorSlotFromUTC(TutorSlot slot) {
        slot.setFromDatetime(getLocalDateTime(slot.getFromDatetime()));
        slot.setToDatetime(getLocalDateTime(slot.getToDatetime()));
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

}
