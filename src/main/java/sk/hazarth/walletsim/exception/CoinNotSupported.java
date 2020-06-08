package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class CoinNotSupported extends AbstractException {

    public CoinNotSupported(String symbol){
        super(String.format("Coin currency with the symbol '%s' is not supported.", symbol));
    }

    @Override
    public Integer getCode() {
        return 201;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
