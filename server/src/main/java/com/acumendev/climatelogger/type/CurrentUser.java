package com.acumendev.climatelogger.type;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CurrentUser implements UserDetails {

    private final long id;
    private final String userName;
    private final String password;
    private final boolean state;

    public CurrentUser(long id, String userName, String password, boolean state) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public boolean isState() {
        return state;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return state;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return state;
    }

}