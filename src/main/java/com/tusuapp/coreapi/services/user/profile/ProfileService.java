package com.tusuapp.coreapi.services.user.profile;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.*;
import com.tusuapp.coreapi.models.dtos.accounts.UpdateProfileDto;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.models.dtos.auth.ResetPasswordDto;
import com.tusuapp.coreapi.repositories.*;
import com.tusuapp.coreapi.services.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.tusuapp.coreapi.utils.SessionUtil.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final BookingRequestRepo bookingRequestRepo;
    private final UserInfoRepo userRepo;
    private final TutorDetailRepo tutorDetailRepo;
    private final CountryRepo countryRepo;
    private final CategoryRepo categoryRepo;
    private final LanguageLocalRepo languageRepo;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public ResponseEntity<?> getUserTotalClassesCount() {
        JSONObject response = new JSONObject();
        long count = bookingRequestRepo.countByStudentIdAndStatus(getCurrentUserId(),
                BookingConstants.STATUS_COMPLETED);
        response.put("totalClasses", count);
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> updateUserProfile(UpdateProfileDto updateDto) {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setFullName(updateDto.getFullName());
        Country country = countryRepo.findById(updateDto.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("No country found"));
        user.setCountry(country);
        user.setTimeZone(updateDto.getTimezone());
        user.setPhone(updateDto.getPhone());
        user.setAddress(updateDto.getAddress());
        user = userRepo.save(user);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    private ResponseEntity<?> getTutorProfile(Long tutorId) {
        User user = userRepo.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<TutorDetails> tutorDetails = tutorDetailRepo.findByUserId(tutorId);
        UserDto userDto = UserDto.fromUser(user);
        userDto.setCompleteProfile(tutorDetails.isPresent());
        JSONObject response = new JSONObject();
        response.put("tutor", userDto);
        response.put("tutorDetails", tutorDetails.get());
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> getTutorProfile() {
        return getTutorProfile(getCurrentUserId());
    }

    public ResponseEntity<?> updateTutorProfile(UpdateProfileDto updateDto) {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        TutorDetails tutorDetails = tutorDetailRepo.findByUserId(getCurrentUserId())
                .orElseThrow(
                        () -> new IllegalArgumentException("No tutor details found, profile might not be completed"));
        if (null != updateDto.getFullName()) {
            user.setFullName(updateDto.getFullName());
        }
        if (updateDto.getPhone() != null && updateDto.getPhone().length() > 5) {
            user.setPhone(updateDto.getPhone());
        }
        // update country
        Country country = countryRepo.findById(updateDto.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("No country found"));
        user.setCountry(country);
        // update languages,discpline and languages
        updatePreferences(tutorDetails, updateDto);
        user.setTimeZone(updateDto.getTimezone());
        user.setAddress(updateDto.getAddress());
        user = userRepo.save(user);
        tutorDetails.setDescription(updateDto.getDescription());
        tutorDetails.setHourlyCharge(updateDto.getHourlyCharge());
        tutorDetails.setExperience(updateDto.getExperience());
        tutorDetails.setGender(updateDto.getGender());
        tutorDetails = tutorDetailRepo.save(tutorDetails);
        JSONObject response = new JSONObject();
        response.put("tutor", UserDto.fromUser(user));
        response.put("tutorDetails", tutorDetails);
        return ResponseEntity.ok(response.toMap());
    }

    private void updatePreferences(TutorDetails tutorDetails, UpdateProfileDto updateDto) {
        List<Category> subjects = categoryRepo.findAllById(updateDto.getSubjects());
        tutorDetails.setSubjects(subjects);
        List<LanguageLocale> languages = languageRepo.findAllById(updateDto.getLanguages());
        tutorDetails.setLanguages(languages);
    }

    public ResponseEntity<?> getTutorProfileWithId(Long id) {
        return getTutorProfile(id);
    }

    public ResponseEntity<?> getCurrentUser() {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid user"));
        UserDto userDto = UserDto.fromUser(user);
        userDto.setRole(new UserDto.Role(isStudent() ? 3 : 4));
        if (isTutor()) {
            Optional<TutorDetails> details = tutorDetailRepo.findByUserId(user.getId());
            if (details.isPresent()) {
                boolean isProfileComplete = details.get().getLanguages().size() > 0
                        && details.get().getSubjects().size() > 0
                        && details.get().getHourlyCharge() != null && details.get().getExperience() != null;
                userDto.setCompleteProfile(isProfileComplete);
                userDto.setTutorApprovalPending(!user.getConfirmed());
            }
        }
        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<?> uploadProfilePhoto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No file provided"));
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only JPEG, PNG, and WebP images are allowed"));
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body(Map.of("error", "File size must not exceed 5 MB"));
        }
        Long userId = getCurrentUserId();
        String ext = file.getContentType().split("/")[1];
        String key = "profile-photos/" + userId + "/" + UUID.randomUUID() + "." + ext;
        try {
            String url = s3Service.uploadFile(file, key);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            user.setImageUrl(url);
            user = userRepo.save(user);
            return ResponseEntity.ok(UserDto.fromUser(user));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload image"));
        }
    }

    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Token expired"));
        if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Passwords do not match"));
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
