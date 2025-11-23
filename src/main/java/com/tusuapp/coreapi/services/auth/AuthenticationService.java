package com.tusuapp.coreapi.services.auth;

import com.tusuapp.coreapi.models.Country;
import com.tusuapp.coreapi.models.SignUpVerification;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.auth.RegistrationRequest;
import com.tusuapp.coreapi.models.dtos.auth.ResetPasswordDto;
import com.tusuapp.coreapi.repositories.CountryRepo;
import com.tusuapp.coreapi.repositories.SignUpVerificationRepo;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.services.notifications.EmailService;
import com.tusuapp.coreapi.services.notifications.SMSService;
import com.tusuapp.coreapi.utils.OTPUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

/**
 * VerificationService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SignUpVerificationRepo verificationRepo;
    private final EmailService emailService;
    private final SMSService smsService;
    public static final String VERIFICATION_LINK = "https://tusuapp.com/signup/verification/email-verify?token=";
    private final UserInfoRepo userRepo;
    private final UserInfoRepo userInfoRepo;
    private final TutorDetailRepo tutorDetailRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final CountryRepo countryRepo;


    public ResponseEntity<?> verifyEmailLinkClick(String sessionId) {
        SignUpVerification verification = verificationRepo.findById(sessionId).orElseThrow(() -> new EntityNotFoundException("Session not found"));
        verification.setEmailVerified(true);
        User user = verification.getUser();
        user.setIsEmailVerified(true);
        userRepo.save(user);
        verificationRepo.save(verification);
        return ResponseEntity.ok().build();
    }


    public String generateRegisterVerifications(User savedUser) {
        SignUpVerification verification = new SignUpVerification();
        verification.setUser(savedUser);
        verification = verificationRepo.save(verification);
        emailService.sendVerificationEmail(verification.getUser().getEmail(), VERIFICATION_LINK + verification.getSessionId());
        verification.setPhoneOtp(OTPUtil.generateOTP4());
        smsService.sendPhoneOtp(savedUser.getPhone(), verification.getPhoneOtp());
        verificationRepo.save(verification);
        return verification.getSessionId();
    }

    public ResponseEntity<?> registerUser(RegistrationRequest request) {
        String nameCleaned = (request.getFullName() != null)
                ? request.getFullName().replaceAll("\\s+", "")
                : "user";
        String username = nameCleaned.toLowerCase() + "_" + System.currentTimeMillis();
        if (!request.getRole().equalsIgnoreCase("student")
                && !request.getRole().equalsIgnoreCase("tutor")) {
            return ResponseEntity.badRequest().body(Map.of("error", "The requested role is not found."));
        }
        Optional<User> existingUser = userInfoRepo.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "The given email already exists."));
        }
        if ("normal".equalsIgnoreCase(request.getLoginAccess())) {
            Optional<User> phoneUser = userInfoRepo.findByPhone(request.getPhone());
            if (phoneUser.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "The given mobile number already exists."));
            }
        }
        String encodedPassword = "social-login".equalsIgnoreCase(request.getLoginAccess())
                ? encoder.encode(String.valueOf(System.currentTimeMillis()))
                : encoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(username);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhone(request.getPhone());
        user.setProvider(request.getProvider());
        user.setConfirmed("student".equalsIgnoreCase(request.getRole()));
        user.setBlocked(false);
        user.setIsEmailVerified("social-login".equalsIgnoreCase(request.getLoginAccess()));
        user.setIsMobileVerified(false);
        user.setIsActive(true);
        user.setTimeZone(request.getTimezone());
        user.setPermanentDelete(false);
        user.setPassword(encodedPassword);
        user.setRole(request.getRole().equalsIgnoreCase("tutor") ? 4 : 3);
        user.setUuid("tu-" + UUID.randomUUID() + System.currentTimeMillis());

        Optional<Country> country = countryRepo.findById(request.getCountryId());
        if (country.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Country not found"));
        }
        user.setCountry(country.get());
        User savedUser = userInfoRepo.save(user);
        String sessionId = generateRegisterVerifications(savedUser);
        String jwt = jwtService.generateToken(savedUser.getId().toString(), savedUser.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("user", UserDto.fromUser(savedUser));
        response.put("jwt", jwt);
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> verifyPhoneOtp(String otp, String sessionId) {
        SignUpVerification verification = verificationRepo.findById(sessionId).orElseThrow(() -> new EntityNotFoundException("Session not found"));
//        if (!verification.getUser().getId().equals(getCurrentUserId())) {
//            return errorResponse(HttpStatus.NOT_FOUND, "No otp session found");
//        }
        if (!verification.getPhoneOtp().equals(otp)) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Wrong OTP");
        }
        User user = verification.getUser();
        user.setIsMobileVerified(true);
        user = userRepo.save(user);
        verification.setPhoneVerified(true);
        verification = verificationRepo.save(verification);
        return ResponseEntity.ok("OTP Verified");
    }

    public ResponseEntity<?> forgotPassword(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        String jwt = jwtService.generateShortToken(user.getId().toString(), user.getEmail());
        String resetLink = "https://tusuapp.com/accounts/reset-password?token=" + jwt;
        emailService.sendForgotPasswordEmail(email,resetLink);
        return ResponseEntity.ok().build();
    }


}
