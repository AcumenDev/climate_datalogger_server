package com.acumendev.climatelogger.utils;

import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static CurrentUser getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CurrentUser) {
            return (CurrentUser) principal;
        } else {
            throw new SecurityException("Пользователь не найден");
        }
    }

}
