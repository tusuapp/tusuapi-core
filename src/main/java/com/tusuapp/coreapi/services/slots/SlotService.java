package com.tusuapp.coreapi.services.slots;

import com.tusuapp.coreapi.models.TutorSlot;
import com.tusuapp.coreapi.models.dtos.bookings.CreateSlotDto;
import com.tusuapp.coreapi.repositories.TutorSlotRepo;
import com.tusuapp.coreapi.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserTimeZone;
import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.*;


@Service
public class SlotService {

    @Autowired
    private TutorSlotRepo tutorSlotRepo;


    public ResponseEntity<?> createSlot(CreateSlotDto createSlotDto) {
        TutorSlot slot = new TutorSlot();
        slot.setFromDatetime(getUtcDateTime(createSlotDto.getFromDateTime()));
        slot.setToDatetime(getUtcDateTime(createSlotDto.getToDateTime()));

        List<TutorSlot> overLappingSlots = tutorSlotRepo.findOverlappingSlots(getCurrentUserId(),
                slot.getFromDatetime(),
                slot.getToDatetime());
        if(!overLappingSlots.isEmpty()){
            return errorResponse(HttpStatus.BAD_REQUEST,"Overlapping slot found");
        }

        slot.setTutorId(getCurrentUserId());
        slot.setIsBooked(false);
        slot.setCreatedAt(LocalDateTime.now());
        slot.setCreatedBy(getCurrentUserId());
        slot = tutorSlotRepo.save(slot);
        transformTutorSlotFromUTC(slot);
        return ResponseEntity.ok(slot);
    }

    public ResponseEntity<?> getTutorSlots(Long tutorId, String dateStr) {
        ZoneId userZone = ZoneId.of(getCurrentUserTimeZone());
        LocalDate localDate = LocalDate.parse(dateStr);
        ZonedDateTime startOfDayUserTz = localDate.atStartOfDay(userZone);
        ZonedDateTime endOfDayUserTz = localDate.plusDays(1).atStartOfDay(userZone).minusNanos(1);
        Instant utcStart = startOfDayUserTz.toInstant();
        Instant utcEnd = endOfDayUserTz.toInstant();
        LocalDateTime utcStartLdt = LocalDateTime.ofInstant(utcStart, ZoneOffset.UTC);
        LocalDateTime utcEndLdt = LocalDateTime.ofInstant(utcEnd, ZoneOffset.UTC);
        List<TutorSlot> slots = tutorSlotRepo.findAllByTutorIdAndFromDatetimeBetween(
                tutorId, utcStartLdt, utcEndLdt
        );
        transformTutorSlotsFromUTC(slots);
        return ResponseEntity.ok(slots);
    }

}
