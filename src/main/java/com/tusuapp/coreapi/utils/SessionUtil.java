package com.tusuapp.coreapi.utils;

import com.tusuapp.coreapi.security.models.TusuUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;


public class SessionUtil {
    public static Long getCurrentUserId(){
        TusuUserDetail user = (TusuUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(user.getUsername());
    }

    public static String getCurrentUserTimeZone(){
        TusuUserDetail user = (TusuUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getTimezone();
    }

    public static boolean isStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return true;
        }

         Optional<String> item = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst();
        System.out.println(item.get());
        return true;
    }

}
