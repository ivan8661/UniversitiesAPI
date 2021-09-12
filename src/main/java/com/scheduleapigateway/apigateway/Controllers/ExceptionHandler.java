package com.scheduleapigateway.apigateway.Controllers;

import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleUserException(UserException ex) {
        return new ResponseEntity<>(new AnswerTemplate<>(null, ex), ex.getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleFoundException(NoHandlerFoundException exception) {
        UserException ex = new UserException(UserExceptionType.ENDPOINT_NOT_FOUND, exception);
        return handleUserException(ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerErrorException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleInternalErrorException(ServerErrorException exception) {
        // Костыль чтоб не словить пустой ответ из-за ошибки сериализации
        HashMap<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("message", exception.getMessage());
        debugInfo.put("stackTrace", exception.getStackTrace());

        UserException ex = new UserException(UserExceptionType.SERVER_ERROR, debugInfo);
        return handleUserException(ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchFieldException.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleNoSuchFieldException(NoSuchFieldException exception) {
        UserException ex = new UserException(UserExceptionType.OBJECT_NOT_FOUND, exception);
        return handleUserException(ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleBadRequestException(HttpClientErrorException exception) {
        // Костыль чтоб не словить пустой ответ из-за ошибки сериализации
        HashMap<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("message", exception.getMessage());
        debugInfo.put("stackTrace", exception.getStackTrace());

        UserException ex = new UserException(UserExceptionType.VALIDATION_ERROR, debugInfo);
        return handleUserException(ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<AnswerTemplate<Object>> handleOtherException(Exception exception) {
        // Костыль чтоб не словить пустой ответ из-за ошибки сериализации
        HashMap<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("message", exception.getMessage());
        debugInfo.put("stackTrace", exception.getStackTrace());

        UserException ex = new UserException(UserExceptionType.SERVER_ERROR, debugInfo);
        return handleUserException(ex);
    }
}


