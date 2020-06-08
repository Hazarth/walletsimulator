package sk.hazarth.walletsim.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name="WALLET_TRANSACTIONS")
public class WalletTransaction {

    @Id
    @GeneratedValue(generator = "sk.hazarth.walletsim.helper.UUIDGenerator")
    private UUID id;

    @NotNull
    @ManyToOne
    private Wallet wallet;

    @NotNull
    private String currencyCode;

    private String currencyFullName;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @NotNull
    @Column(nullable = false, precision = 36, scale = 18)
    private BigDecimal amount;
}
