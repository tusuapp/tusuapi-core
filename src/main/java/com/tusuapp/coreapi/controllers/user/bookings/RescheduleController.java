package com.tusuapp.coreapi.controllers.user.bookings;


import com.tusuapp.coreapi.models.dtos.bookings.RescheduleBookingDto;
import com.tusuapp.coreapi.services.user.bookings.RescheduleService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.tusuapp.coreapi.services.user.bookings.RescheduleService.REQUESTED;

@RestController
@RequestMapping("/user/classes/bookings/reschedule")
public class RescheduleController {


    @Autowired
    private RescheduleService rescheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> rescheduleBooking(@RequestBody RescheduleBookingDto rescheduleDto) {
        return rescheduleService.rescheduleBooking(rescheduleDto);
    }

    @GetMapping
    public ResponseEntity<?> getRescheduleRequests(@RequestParam(required = false) String status) {
        return rescheduleService.getPendingReschedules(status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> updateRescheduleRequest(@PathVariable("id") Long id, @RequestParam String status) {
        return rescheduleService.updateRescheduleRequest(id,status);
    }

}
