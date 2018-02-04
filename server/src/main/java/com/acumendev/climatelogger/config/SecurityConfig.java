package com.acumendev.climatelogger.config;

import com.acumendev.climatelogger.service.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Created by vladimir akummail@gmail.com on 1/3/18.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/")
                .failureUrl("/authentication/login.html?failed")
                .loginProcessingUrl("/authentication/process")
                .successHandler(new RedirectHandler("/lk/index.html"))
                .and()
                .authorizeRequests()
                .antMatchers("/lk/**", "/api/**").authenticated().and()
                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder(5));

        //.withUser(CurrentUser.withDefaultPasswordEncoder().username("akum").password("akum").roles("USER")
    }

    private static class RedirectHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        RedirectHandler(String url) {
            super.setDefaultTargetUrl(url);
        }
    }
}
