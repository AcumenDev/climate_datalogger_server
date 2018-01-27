package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.repository.UserRepository;
import com.acumendev.climatelogger.repository.dbo.UserDbo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDbo userDbo = userRepository.getUserByLogin(username);
        return new CurrentUser(userDbo.getId(), userDbo.getLogin(), userDbo.getPassword(), userDbo.isState());
    }
}