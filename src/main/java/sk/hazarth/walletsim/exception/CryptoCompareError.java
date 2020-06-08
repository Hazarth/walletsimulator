package sk.hazarth.walletsim.exception;

import org.springframework.http.HttpStatus;

public class CryptoCompareError extends AbstractException {

    public CryptoCompareError(String reason){
        super(String.format("CryptoCompare error: %s.", reason));
    }

    @Override
    public Integer getCode() {
        return 800;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
