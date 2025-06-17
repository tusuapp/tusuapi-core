package com.tusuapp.coreapi.services.user.profile;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;


@Service
public class ProfileService {

    @Autowired
    private BookingRequestRepo bookingRequestRepo;


    public ResponseEntity<?> getFullProfile(){
        return null;
    }


    public ResponseEntity<?> getUserTotalClassesCount() {
        JSONObject response = new JSONObject();
        long count = bookingRequestRepo.countByStudentIdAndStatus(getCurrentUserId(), BookingConstants.STATUS_COMPLETED);
        response.put("totalClasses", count);
        return ResponseEntity.ok(response.toMap());
    }

}
