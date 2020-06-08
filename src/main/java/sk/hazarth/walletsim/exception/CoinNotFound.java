package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class CoinNotFound extends AbstractException {

    public CoinNotFound(Long id){
        super(String.format("Coin with the id '%d' was not found.", id));
    }

    public CoinNotFound(String symbol){
        super(String.format("Coin with the symbol '%s' was not found.", symbol));
    }

    @Override
    public Integer getCode() {
        return 200;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
