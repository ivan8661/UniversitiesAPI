package com.scheduleapigateway.apigateway.Exceptions;


public class UserException extends Exception {
    private int id;
    private String code;
    private String message;
    private String data;

    public UserException(int id, String code, String message, String data) {
        this.id = id;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}


