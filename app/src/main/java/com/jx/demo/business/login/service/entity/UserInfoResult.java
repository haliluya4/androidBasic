package com.jx.demo.business.login.service.entity;

import com.jx.demo.network.ResultValidator;

public class UserInfoResult implements ResultValidator {

    private String id;
    private String username;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String checkIsValid() {
        if (name == null || name.trim().length() == 0) {
            return "昵称为空";
        }
        return null;
    }
}
