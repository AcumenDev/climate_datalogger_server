package com.acumendev.climatelogger.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Exception.class)
    public Object error(Exception e) {
        LOGGER.error("Ошибка обработки запроса", e);
        return "Что-то пошло не так, мы уже работаем над этим.";
    }
}
