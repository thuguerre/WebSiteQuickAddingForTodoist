package com.thug.model;

public class GetAccessTokenRequest {

    private String code;

    private String state;

    public GetAccessTokenRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
