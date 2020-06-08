package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;
import sk.hazarth.walletsim.domain.Wallet;

public class NotEnoughFunds extends AbstractException {

    public NotEnoughFunds(Wallet wallet){
        super(String.format("Wallet '%s' doesn't have enough funds for this operation.", wallet.getFriendlyName()));
    }

    @Override
    public Integer getCode() {
        return 102;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
