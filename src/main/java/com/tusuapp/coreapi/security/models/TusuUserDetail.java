package com.tusuapp.coreapi.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TusuUserDetail implements UserDetails {

    private final Long id;
    private final String password;
    private final String timezone;
    private final Collection<? extends GrantedAuthority> authorities;

    public TusuUserDetail(Long id, String password, String timezone,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.timezone = timezone;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
