package com.acumendev.climatelogger.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CurrentUser implements UserDetails {

    private final long id;
    private final String login;
    private final String password;
    private final boolean state;

    public CurrentUser(long id, String login, String password, boolean state) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
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
        return login;
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

    @Override
    public String getName() {
        return login;
    }
}