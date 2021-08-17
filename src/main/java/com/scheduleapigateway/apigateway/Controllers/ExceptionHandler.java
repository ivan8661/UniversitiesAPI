package com.scheduleapigateway.apigateway.Controllers;

import com.scheduleapigateway.apigateway.Exceptions.ErrorResult;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

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
    protected ResponseEntity<ErrorResult> handleUserException(UserException us) {
        int code = us.getId();
        return switch (code) {
            case 400 -> new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.BAD_REQUEST);
            case 403 -> new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.FORBIDDEN);
            case 404 -> new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_FOUND);
            case 406 -> new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_ACCEPTABLE);
            default -> new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResult> handleFoundException(NoHandlerFoundException exception) {
        return new ResponseEntity<>(new ErrorResult(HttpStatus.NOT_IMPLEMENTED.value(), "501", "501", "нет такоВа ендпоинта + " + Arrays.toString(exception.getStackTrace())), HttpStatus.NOT_IMPLEMENTED);
        }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerErrorException.class)
    protected ResponseEntity<ErrorResult> handleInternalErrorException(ServerErrorException exception) {
        return new ResponseEntity<>(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal_server_error", "INTERNAL_SERVER_ERROR", Arrays.toString(exception.getStackTrace())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchFieldException.class)
    protected ResponseEntity<ErrorResult> handleNoSuchFieldException(NoSuchFieldException exception) {
        return new ResponseEntity<>(new ErrorResult(404, "NOT_FOUND", "Field " + exception.getMessage() + "  doesn't exist", ""),  HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResult> handleBadRequestException(HttpClientErrorException exception) {
        return new ResponseEntity<>(new ErrorResult(400, "BAD_REQUEST", exception.getMessage(), Arrays.toString(exception.getStackTrace())), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResult> handleOtherException(Exception ex) {
        return new ResponseEntity<>(new ErrorResult(500, "internal_server_error", ex.getMessage(), Arrays.toString(ex.getStackTrace())), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


