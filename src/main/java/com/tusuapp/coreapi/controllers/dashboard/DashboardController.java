package com.tusuapp.coreapi.controllers.dashboard;


import com.tusuapp.coreapi.services.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {


    @Autowired
    private DashboardService dashboardService;


    @GetMapping("/tutor/dashboard")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getTutorDashboard(){
        return dashboardService.getTutorDashboard();
    }


}

