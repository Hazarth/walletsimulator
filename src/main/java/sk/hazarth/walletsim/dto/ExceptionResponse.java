package sk.hazarth.walletsim.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.hazarth.walletsim.exception.AbstractException;

@Data
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private Integer code;

    public ExceptionResponse(AbstractException ex){
        message = ex.getMessage();
        code = ex.getCode();
    }
}
