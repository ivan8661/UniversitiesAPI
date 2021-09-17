package com.scheduleapigateway.apigateway.Controllers;

import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * @author Poltorakov
 * Global handlerException class that intercept UserExeptions and getting formatted answer (example):
 * {
 *     error {
 *         id: 404,
 *         code: "INTERNAL_MOTHERBOARD_ERROR",
 *         message: "hello there, general Kenobi!",
 *         data "empty almost always"
 *     }
 * }
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleUserException(UserException ex) {
        return new ResponseEntity<>(new AnswerTemplate<>(null, ex), ex.getHttpStatus());
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleServiceException(ServiceException ex) {
        return new ResponseEntity<>(new AnswerTemplate<>(null, ex), ex.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleFoundException(NoHandlerFoundException exception) {
        SchedCoreApplication.getLogger().error("No Handler [" + exception.getHttpMethod() + "] " + exception.getRequestURL() + "\n Message: " + exception.getMessage());
        return handleUserException(new UserException(UserExceptionType.ENDPOINT_NOT_FOUND, exception));
    }

    @ExceptionHandler(ServerErrorException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleInternalErrorException(ServerErrorException exception) {
        SchedCoreApplication.getLogger().error("ServerError: \n   Method: " + exception.getHandlerMethod() + "\n   Message: " + exception.getMessage());
        return handleUserException(new UserException(UserExceptionType.SERVER_ERROR, exception));
    }

    @ExceptionHandler(NoSuchFieldException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleNoSuchFieldException(NoSuchFieldException exception) {
        return handleUserException(new UserException(UserExceptionType.OBJECT_NOT_FOUND, exception));
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleBadRequestException(HttpClientErrorException exception) {
        return handleUserException(new UserException(UserExceptionType.VALIDATION_ERROR, exception));
    }



    @ExceptionHandler(Exception.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleOtherException(Exception exception) {
        return handleUserException(new UserException(UserExceptionType.SERVER_ERROR, exception));
    }
}

