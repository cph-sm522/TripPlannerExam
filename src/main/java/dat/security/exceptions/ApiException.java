package dat.security.exceptions;

import java.time.LocalDateTime;

public class ApiException extends RuntimeException {

    private int code;
    private LocalDateTime timestamp;

    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public int getCode() {
        return code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
