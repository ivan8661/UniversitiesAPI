package com.scheduleapigateway.apigateway.Exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResult {
    @JsonProperty("result")
    private String res = null;
    @JsonProperty("error")
    private ExError exError;
    public ErrorResult(int id, String code, String message, String data) {
        exError = new ExError(id, code, message, data);
    }
}
