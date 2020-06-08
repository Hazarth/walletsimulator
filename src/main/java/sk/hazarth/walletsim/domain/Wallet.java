package sk.hazarth.walletsim.domain;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="WALLETS")
@Where(clause = "not deleted")
public class Wallet {

    @Id
    @GeneratedValue(generator = "sk.hazarth.walletsim.helper.UUIDGenerator")
    private UUID uuid;

    @NotNull(message = "Wallet friendly name cannot be empty.")
    @Size(min=3, max=30, message = "Wallet friendly name must have at least 3 and at most 30 characters")
    @Column(nullable = false)
    private String friendlyName;

    @NotNull(message = "Wallet currency code cannot be empty.")
    @ManyToOne(targetEntity = Coin.class)
    private Coin coin;

    @OneToMany(mappedBy = "wallet")
    private List<WalletTransaction> transactions;

    @NotNull
    private Boolean deleted = false;

}
