package com.tusuapp.coreapi.cron;

import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.SignUpVerification;
import com.tusuapp.coreapi.repositories.SignUpVerificationRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static com.tusuapp.coreapi.utils.converters.TimeZoneConverter.getCurrentUTCTime;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

/**
 * OTPResetter created by Rithik S(coderithik@gmail.com)
 **/
@Component
@RequiredArgsConstructor
public class OTPResetter {

    private final SignUpVerificationRepo verificationRepo;
    private final UserInfoRepo userRepo;



    @Scheduled(fixedDelay = 30 *  1000)
    public void deleteAccountAbandoned() {
        List<SignUpVerification> requests = verificationRepo.findAllByCreatedAtBeforeAndIsEmailVerifiedFalseOrIsPhoneVerifiedFalse(getCurrentUTCTime());;
        List<Long> usersToDelete = requests.stream().map(v->v.getUser().getId()).toList();
        userRepo.deleteAllById(usersToDelete);
        System.out.println("Deleted " + usersToDelete.size() + " accounts");
    }
}
