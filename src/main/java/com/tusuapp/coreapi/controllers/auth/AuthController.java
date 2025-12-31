package com.tusuapp.coreapi.controllers.auth;

import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.auth.RegistrationRequest;
import com.tusuapp.coreapi.models.dtos.auth.ResetPasswordDto;
import com.tusuapp.coreapi.repositories.CountryRepo;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.services.auth.AuthenticationService;
import com.tusuapp.coreapi.services.auth.JwtService;
import com.tusuapp.coreapi.services.notifications.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private TutorDetailRepo tutorDetailRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        String identifier = body.get("identifier");
        String password = body.get("password");

        // Basic validation
        if (!StringUtils.hasText(identifier) || !StringUtils.hasText(password)) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("message", "Identifier and password are required"));
        }

        // Check for application-name header
        String applicationName = request.getHeader("application-name");
        if (!StringUtils.hasText(applicationName)) {
            return ResponseEntity.status(401).body(Map.of("message", "Please provide your application name."));
        }

        // Fetch user
        Optional<User> userOpt;
        if (identifier.contains("@")) {
            userOpt = userInfoRepo.findByEmailIgnoreCaseAndProvider(identifier, "local");
        } else {
            userOpt = userInfoRepo.findByUsernameAndProvider(identifier, "local");
        }

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }

        User user = userOpt.get();
        String role = user.getRole() == 4 ? "TUTOR" : "STUDENT";
        if (!applicationName.equalsIgnoreCase(role)) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized user."));
        }

        if (user.getPermanentDelete()) {
            return ResponseEntity.status(404).body(Map.of("message", "This account deleted."));
        }

        Map<String, Object> returnResult = new HashMap<>();
        returnResult.put("identifier", identifier);
        returnResult.put("is_mobile_verified", user.getIsMobileVerified());
        returnResult.put("blocked", user.getBlocked());
        returnResult.put("confirmed", user.getConfirmed());

        // if (!user.getConfirmed()) {
        // returnResult.put("message", "Waiting for admin approval");
        // return ResponseEntity.status(403).body(returnResult);
        // }

        if (user.getBlocked()) {
            returnResult.put("message", "Your account is blocked. Please contact our administration");
            return ResponseEntity.status(401).body(returnResult);
        }

        if (!encoder.matches(password, user.getPassword())) {
            returnResult.put("message", "Identifier or password invalid.");
            returnResult.remove("is_mobile_verified");
            returnResult.remove("blocked");
            returnResult.remove("confirmed");
            return ResponseEntity.status(401).body(returnResult);
        }

        String jwt = jwtService.generateToken(user.getId().toString(), user.getEmail());

        // Attach timezone details
        Map<String, Object> response = new HashMap<>();
        UserDto userDto = UserDto.fromUser(user);
        if (userDto.getRole().getName().equalsIgnoreCase("Tutor")) {
            Optional<TutorDetails> tutorDetails = tutorDetailRepo.findByUserId(user.getId());
            userDto.setCompleteProfile(tutorDetails.isPresent());
        }
        response.put("user", userDto);
        response.put("timezone", user.getTimeZone());
        response.put("timezone_offset", user.getTimeZoneOffset());
        response.put("jwt", jwt);
        response.put("chatLogin", new HashMap<>());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        return authenticationService.registerUser(request);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmailOtp(@RequestParam String token) {
        return authenticationService.verifyEmailLinkClick(token);
    }

    @PostMapping("/otp/verify-phone")
    public ResponseEntity<?> verifyPhoneOtp(@RequestParam String session, @RequestParam String otp) {
        return authenticationService.verifyPhoneOtp(otp, session);
    }

    @PostMapping("/otp/verify-email")
    public ResponseEntity<?> verifyEmailOtp(@RequestParam String session, @RequestParam String otp) {
        return authenticationService.verifyEmailOtp(otp, session);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return authenticationService.forgotPassword(email);
    }

}
