package com.tusuapp.coreapi.controllers.auth;


import com.tusuapp.coreapi.models.Country;
import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.auth.RegistrationRequest;
import com.tusuapp.coreapi.repositories.CountryRepo;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
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
import java.util.UUID;

import static com.tusuapp.coreapi.utils.SessionUtil.*;

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

    @GetMapping("/user")
    public ResponseEntity<?> getAuthUser() {
        User user = userInfoRepo.findById(getCurrentUserId()).get();
        UserDto userDto = UserDto.fromUser(user);
        userDto.setRole(new UserDto.Role(isStudent() ? 3 : 4));
        if (isTutor()) {
            Optional<TutorDetails> details = tutorDetailRepo.findByUserId(user.getId());
            userDto.setCompleteProfile(details.isPresent());
        }
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) throws MessagingException, UnsupportedEncodingException {

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

        if (!user.getConfirmed()) {
            returnResult.put("message", "Waiting for admin approval");
            return ResponseEntity.status(403).body(returnResult);
        }

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
        response.put("user", UserDto.fromUser(user));
        response.put("timezone", user.getTimeZone());
        response.put("timezone_offset", user.getTimeZoneOffset());
        response.put("jwt", jwt);
        response.put("chatLogin", new HashMap<>());
        emailService.sendEmail("tubeviral88@gmail.com", "OTP", "hello");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
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
            Optional<User> phoneUser = userInfoRepo.findByPhone(Long.valueOf(request.getPhone()));
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
        user.setPhone(Long.parseLong(request.getPhone()));
        user.setProvider(request.getProvider());
        user.setConfirmed("student".equalsIgnoreCase(request.getRole()));
        user.setBlocked(false);
        user.setIsEmailVerified("social-login".equalsIgnoreCase(request.getLoginAccess()));
        user.setIsMobileVerified(false);
        user.setIsActive(true);
        user.setTimeZone(request.getTimezone());
        user.setPermanentDelete(false);
        user.setPassword(encodedPassword);
        user.setRole(request.getRole().equalsIgnoreCase("tutor") ? 3 : 4);
        user.setUuid("tu-" + UUID.randomUUID() + System.currentTimeMillis());

        Optional<Country> country = countryRepo.findById(request.getCountryId());
        if(country.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "Country not found"));
        }
        user.setCountry(country.get());
        User savedUser = userInfoRepo.save(user);
        String jwt = jwtService.generateToken(savedUser.getId().toString(), savedUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("user", UserDto.fromUser(savedUser));
        response.put("jwt", jwt);
        return ResponseEntity.ok(response);
    }

}
