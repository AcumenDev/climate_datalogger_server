package com.acumendev.climatelogger.repository.dbo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDbo {
    private String login;
    private long id;
    private String password;
    private boolean state;
}
