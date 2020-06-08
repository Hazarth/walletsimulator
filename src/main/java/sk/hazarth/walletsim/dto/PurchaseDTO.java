package sk.hazarth.walletsim.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PurchaseDTO {

    @DecimalMin(value = "0", message = "The amount of currency cannot be zero or less", inclusive = false)
    private BigDecimal amount;

    @NotNull
    private String currencySymbol;
}
