package com.tusuapp.coreapi.utils.converters;


import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.RescheduleRequest;
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

    public static void transformRescheduleFromUTC(RescheduleRequest request) {
        request.setProposedDateTime(getLocalDateTime(request.getProposedDateTime()));
        request.setCreatedAt(getLocalDateTime(request.getCreatedAt()));
        request.setUpdatedAt(getLocalDateTime(request.getUpdatedAt()));
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

    public static ZonedDateTime getZonedUserTime(LocalDateTime utcTime) {
        ZoneId targetZone = ZoneId.of(getCurrentUserTimeZone());
        ZonedDateTime utcZoned = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime converted = utcZoned.withZoneSameInstant(targetZone);
        return converted;
    }

    public static LocalDateTime getUtcDateTime(LocalDateTime tzDateTime) {
        System.out.println(getCurrentUserTimeZone());
        ZoneId targetZone = ZoneId.of(getCurrentUserTimeZone());
        ZonedDateTime zonedDateTime = tzDateTime.atZone(targetZone);
        System.out.println("zonedDateTime " + zonedDateTime);
        ZonedDateTime utcZoned = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("Converted " + utcZoned);
        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime getCurrentUTCTime() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
    }

}
