package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class FriendlyNameAlreadyExists extends AbstractException {

    public FriendlyNameAlreadyExists(String name){
        super(String.format("Wallet with the name %s already exists.", name));
    }

    @Override
    public Integer getCode() {
        return 101;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
