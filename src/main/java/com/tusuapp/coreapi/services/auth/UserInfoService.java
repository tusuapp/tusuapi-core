package com.tusuapp.coreapi.services.auth;

import com.tusuapp.coreapi.constants.AccountConstants;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.security.models.TusuUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepo repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findById(Long.parseLong(username));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with id: " + username);
        }

        return new TusuUserDetail(
                user.get().getId(),
                user.get().getPassword(),
                user.get().getTimeZone(),
                getAuthorities(user.get())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singleton(
                new SimpleGrantedAuthority(
                        user.getRole() == AccountConstants.ROLE_TUTOR ? "ROLE_TUTOR" : "ROLE_STUDENT"
                )
        );
    }

}