package com.tusuapp.coreapi.services.auth;

import com.tusuapp.coreapi.constants.AccountConstants;
import com.tusuapp.coreapi.models.TusuAdmin;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.TusuAdminRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.security.models.TusuUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * AdminInfoService created by Rithik S(coderithik@gmail.com)
 **/
@Component
public class AdminInfoService implements UserDetailsService {

    @Autowired
    private TusuAdminRepo adminRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<TusuAdmin> user = adminRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with id: " + email);
        }

        return new TusuUserDetail(
                user.get().getId(),
                user.get().getPassword(),
                user.get().getTimeZone(),
                getAuthorities()
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(
                        "ROLE_ADMIN"
                )
        );
    }

}
