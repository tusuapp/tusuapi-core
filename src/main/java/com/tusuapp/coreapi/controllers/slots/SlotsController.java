package com.tusuapp.coreapi.controllers.slots;


import com.tusuapp.coreapi.models.dtos.bookings.CreateSlotDto;
import com.tusuapp.coreapi.services.slots.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getSlots(@RequestParam Long tutorId, @RequestParam String date){
        return slotService.getTutorSlots(tutorId,date);
    }


}
