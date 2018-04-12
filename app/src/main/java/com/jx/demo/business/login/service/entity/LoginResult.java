package com.jx.demo.business.login.service.entity;

import com.jx.demo.network.ResultValidator;

/**
 * 登录结果
 */
public class LoginResult implements ResultValidator {

    private String accessToken;
    private String userId;

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "accessToken='" + accessToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public String checkIsValid() {
        if (accessToken == null || accessToken.trim().length() == 0) {
            return "token为空";
        }
        return null;
    }
}
