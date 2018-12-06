package com.acumendev.climatelogger.service.auth;

import com.acumendev.climatelogger.repository.UserRepository;
import com.acumendev.climatelogger.repository.dbo.UserDbo;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDbo userDbo = userRepository.getUserByLogin(username);
        return new CurrentUser(userDbo.id, userDbo.login, userDbo.password, userDbo.state);
    }
}