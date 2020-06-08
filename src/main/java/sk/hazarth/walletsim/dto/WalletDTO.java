package sk.hazarth.walletsim.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletDTO {

    private UUID uuid;
    private String friendlyName;
    private String symbol;
    private String currencyName;
    private BigDecimal balance;

}
