package com.scheduleapigateway.apigateway.Controllers;

import com.scheduleapigateway.apigateway.Exceptions.ErrorResponseAnswer;
import com.scheduleapigateway.apigateway.Exceptions.UserException;

/**
 * Class for template of answer by the view:
 * {
 *  result: {}
 *  error: {}
 *  }
 *
 * @author Poltorakov
 * @param <T> Generic for different objects
 */
public class AnswerTemplate<T> {

    final private T result;

    final private ErrorResponseAnswer error;

    public AnswerTemplate(T result, ErrorResponseAnswer error) {
        this.result = result;
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public ErrorResponseAnswer getError() {
        return error;
    }
}
