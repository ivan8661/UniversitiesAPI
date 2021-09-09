package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheduleapigateway.apigateway.Exceptions.UserException;

/**
 * Class for template of answer by the view:
 * {
 *  result: {}
 *  error: {}
 *  }
 * @author Poltorakov
 * @param <T> Generic for different objects
 */
public class AnswerTemplate<T>{


    private T result;

    private UserException error;

    public AnswerTemplate(T result, UserException error) {
        this.result = result;
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public UserException getError() {
        return error;
    }
}
