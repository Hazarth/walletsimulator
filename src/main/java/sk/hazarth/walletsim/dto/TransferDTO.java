package sk.hazarth.walletsim.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferDTO {
    @NotNull
    private UUID destinationWallet;

    @DecimalMin(value = "0", message = "The amount of currency cannot be zero or less", inclusive = false)
    private BigDecimal amount;
}
