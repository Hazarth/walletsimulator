package sk.hazarth.walletsim.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sk.hazarth.walletsim.dto.ExceptionResponse;
import sk.hazarth.walletsim.exception.AbstractException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ExceptionResponse> handleApplicationExceptions(AbstractException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleExceptions(Exception ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(0);
        response.setMessage(getUsefulMessage(ex));
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getUsefulMessage(Throwable ex) {
        if(ex instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> violationSet = ((ConstraintViolationException) ex).getConstraintViolations();
            for(ConstraintViolation<?> violation : violationSet){
                return violation.getMessage();
            }
        }else if (ex instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }

        if(ex.getCause() != null){
            return getUsefulMessage(ex.getCause());
        }

        return ex.getMessage();
    }

}
