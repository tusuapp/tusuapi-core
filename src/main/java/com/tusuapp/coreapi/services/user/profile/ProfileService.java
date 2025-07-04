package com.tusuapp.coreapi.services.user.profile;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.Country;
import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UpdateProfileDto;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.CountryRepo;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@Service
public class ProfileService {

    @Autowired
    private BookingRequestRepo bookingRequestRepo;

    @Autowired
    private UserInfoRepo userRepo;

    @Autowired
    private TutorDetailRepo tutorDetailRepo;

    @Autowired
    private CountryRepo countryRepo;


    public ResponseEntity<?> getUserTotalClassesCount() {
        JSONObject response = new JSONObject();
        long count = bookingRequestRepo.countByStudentIdAndStatus(getCurrentUserId(), BookingConstants.STATUS_COMPLETED);
        response.put("totalClasses", count);
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> updateUserProfile(UpdateProfileDto updateDto) {
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        user.setFullName(updateDto.getFullName());
        Country country = countryRepo.findById(updateDto.getCountryId())
                .orElseThrow(()->new IllegalArgumentException("No country found"));
        user.setCountry(country);
        user.setTimeZone(updateDto.getTimezone());
        user.setPhone(updateDto.getPhone());
        user.setAddress(updateDto.getAddress());
        user = userRepo.save(user);
        return ResponseEntity.ok(user);
    }

    private ResponseEntity<?> getTutorProfile(Long tutorId) {
        User user = userRepo.findById(tutorId)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        TutorDetails tutorDetails = tutorDetailRepo.findByUserId(tutorId)
                .orElseThrow(()->new IllegalArgumentException("No tutor details found, profile might not be completed"));
        JSONObject response = new JSONObject();
        response.put("tutor",UserDto.fromUser(user));
        response.put("tutorDetails",tutorDetails);
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> getTutorProfile() {
        return getTutorProfile(getCurrentUserId());
    }

    public ResponseEntity<?> updateTutorProfile(UpdateProfileDto updateDto) {
        System.out.println(updateDto.getCountryId());
        User user = userRepo.findById(getCurrentUserId())
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        TutorDetails tutorDetails = tutorDetailRepo.findByUserId(getCurrentUserId())
                .orElseThrow(()->new IllegalArgumentException("No tutor details found, profile might not be completed"));

        user.setFullName(updateDto.getFullName());
        user.setPhone(updateDto.getPhone());
        Country country = countryRepo.findById(updateDto.getCountryId())
                .orElseThrow(()->new IllegalArgumentException("No country found"));
        user.setCountry(country);
        user.setTimeZone(updateDto.getTimezone());
        user.setAddress(updateDto.getAddress());
        user = userRepo.save(user);

        tutorDetails.setDescription(updateDto.getDescription());
        tutorDetails.setHourlyCharge(updateDto.getHourlyCharge());

        tutorDetails = tutorDetailRepo.save(tutorDetails);

        JSONObject response = new JSONObject();
        response.put("tutor",UserDto.fromUser(user));
        response.put("tutorDetails",tutorDetails);
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> getTutorProfileWithId(Long id) {
        return getTutorProfile(id);
    }
}
