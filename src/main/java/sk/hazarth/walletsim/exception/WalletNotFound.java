package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class WalletNotFound extends AbstractException {

    public WalletNotFound(String id){
        super(String.format("Wallet '%s' was not found.", id));
    }

    @Override
    public Integer getCode() {
        return 100;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
