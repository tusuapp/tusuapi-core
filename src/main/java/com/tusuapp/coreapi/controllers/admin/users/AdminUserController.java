package com.tusuapp.coreapi.controllers.admin.users;

import com.tusuapp.coreapi.services.admin.users.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        return adminUserService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    @org.springframework.web.bind.annotation.PostMapping("/credit-coins")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> creditCoins(@org.springframework.web.bind.annotation.RequestParam Long userId,
            @org.springframework.web.bind.annotation.RequestParam Double amount) {
        return adminUserService.creditCoins(userId, amount);
    }
}
