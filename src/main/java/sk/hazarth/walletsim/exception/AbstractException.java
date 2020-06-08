package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

public abstract class AbstractException extends Exception{

    public AbstractException(String message){
        super(message);
    }

    public Timestamp getTimestamp() {
        return Timestamp.from(Instant.now());
    }

    abstract public Integer getCode();
    abstract public HttpStatus getHttpStatus();
}
