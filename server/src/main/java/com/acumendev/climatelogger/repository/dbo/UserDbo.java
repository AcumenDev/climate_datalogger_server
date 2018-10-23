package com.acumendev.climatelogger.repository.dbo;


public class UserDbo {
    public final String login;
    public final long id;
    public final String password;
    public final boolean state;

    public UserDbo(String login, long id, String password, boolean state) {
        this.login = login;
        this.id = id;
        this.password = password;
        this.state = state;
    }
}
