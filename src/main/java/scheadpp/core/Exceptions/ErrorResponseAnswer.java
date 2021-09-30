package scheadpp.core.Exceptions;

import org.springframework.http.HttpStatus;

public interface ErrorResponseAnswer {
    int getId();
    String getCode();
    Object getData();
    String getMessage();
    HttpStatus getHttpStatus();
}
