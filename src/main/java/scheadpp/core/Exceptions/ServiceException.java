package scheadpp.core.Exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonIgnoreProperties({"httpStatus", "stackTrace", "localizedMessage", "suppressed", "cause", "type"})
public class ServiceException extends Exception implements ErrorResponseAnswer {

    private HttpStatus status;
    private String code;
    private Object data;
    private String message;

    public void setResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    public ResponseEntity<String> getResponse() {
        return response;
    }

    private ResponseEntity<String> response;

    public ServiceException(HttpStatus status, JSONObject object) {
        this.status = status;
        code = object.optString("code");
        message = object.optString("message");
        if( object.optJSONObject("data") != null )
            data = object.optJSONObject("data");
    }

    @Override
    public int getId() {
        return status.value();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}
