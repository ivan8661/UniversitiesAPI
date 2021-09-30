package scheadpp.core.Exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@JsonIgnoreProperties({"httpStatus", "stackTrace", "localizedMessage", "suppressed", "cause", "type"})
public class UserException extends Exception implements ErrorResponseAnswer {

    @JsonIgnore
    private UserExceptionType type;

    private String message;
    private Object data;

    public UserException(UserExceptionType type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public UserException(UserExceptionType type, String message) {
        this.type = type;
        this.message = message;
        this.data = getStackTrace();
    }

    public UserException(UserExceptionType type, Object data) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.data = data;
    }

    // Костыль чтоб не словить пустой ответ из-за ошибки сериализации
    public UserException(UserExceptionType type, Exception exception) {
        this.type = type;
        this.message = exception.getLocalizedMessage();
        HashMap<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("message", exception.getMessage());
        debugInfo.put("stackTrace", exception.getStackTrace());
        debugInfo.put("class", exception.getClass().toString());
        this.data = debugInfo;
    }

    public UserException(UserExceptionType type) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.data = getStackTrace();
    }

    public int getId() {
        return type.getHTTPCode();
    }

    public String getCode() {
        return type.getCode();
    }

    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        if ( message != null && !message.isEmpty()) {
            return message;
        } else {
            return type.getDefaultMessage();
        }
    }

    public HttpStatus getHttpStatus() {
        return type.getHttpStatus();
    }
}


