package com.tusuapp.coreapi.controllers.slots;


import com.tusuapp.coreapi.models.dtos.bookings.CreateSlotDto;
import com.tusuapp.coreapi.services.slots.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@RestController
@RequestMapping("/slots")
public class SlotsController {

    @Autowired
    private SlotService slotService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createSlot(@RequestBody CreateSlotDto createSlotDto) {
        return slotService.createSlot(createSlotDto);
    }

    @GetMapping
    public ResponseEntity<?> getSlots(@RequestParam(required = false) Long tutorId, @RequestParam String date){
        if(tutorId == null){
            tutorId = getCurrentUserId();
        }
        return slotService.getTutorSlots(tutorId,date);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteSlot(@RequestParam Long slotId){
        return slotService.deleteSlot(slotId);
    }

}
