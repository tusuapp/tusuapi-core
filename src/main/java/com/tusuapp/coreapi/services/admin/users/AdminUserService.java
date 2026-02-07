package com.tusuapp.coreapi.services.admin.users;

import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserInfoRepo userInfoRepo;
    private final com.tusuapp.coreapi.services.user.CreditService creditService;

    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<User> users = userInfoRepo.findAll(pageable);
        Page<UserDto> userDtos = users.map(UserDto::fromUser);
        return ResponseEntity.ok(userDtos);
    }

    private final com.tusuapp.coreapi.repositories.UserWalletRepo userWalletRepo;

    public ResponseEntity<?> getUserById(Long id) {
        User user = userInfoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        UserDto dto = UserDto.fromUser(user);
        com.tusuapp.coreapi.models.UserWallet wallet = userWalletRepo.findByUserId(id).orElse(null);
        if (wallet != null) {
            dto.setCoinBalance(wallet.getBalance());
        } else {
            dto.setCoinBalance(0.0);
        }
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<?> creditCoins(Long userId, Double amount) {
        // Validate user exists
        userInfoRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        creditService.addAdminCredits(userId, amount);
        return ResponseEntity.ok().build();
    }
}
