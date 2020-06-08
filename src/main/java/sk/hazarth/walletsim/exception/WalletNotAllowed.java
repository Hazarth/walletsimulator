package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class WalletNotAllowed extends AbstractException {

    public WalletNotAllowed(String reason){
        super(String.format("Wallet not allowed: ", reason));
    }

    @Override
    public Integer getCode() {
        return 103;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
