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

    private T error;

    public AnswerTemplate(T result, T error) {
        this.result = result;
        this.error = error;
    }


    public AnswerTemplate() {

    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
