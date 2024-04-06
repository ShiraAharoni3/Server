package com.ashcollege.responses;

public class LoginResponse extends BasicResponse
{
    private String token ;

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(boolean success, Integer errorCode, String token) {
        super(success, errorCode);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
