package sk.hazarth.walletsim.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "COINS")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    @NotNull(message = "Name cannot be null.")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Coin symbol cannot be null.")
    @Column(unique = true, nullable = false)
    @Size(min=1, max=10, message = "Currency codes are expected not to be longer than 10 characters")
    private String symbol;

    @NotNull(message = "Coin name cannot be null.")
    @Column(nullable = false)
    private String coinName;

    @NotNull(message = "Coin full name cannot be null.")
    @Column(nullable = false)
    private String fullName;

    @NotNull(message = "The amount of coin decimal places cannot be null.")
    @Column(nullable = false)
    private Integer decimalPlaces;

    @Column(nullable = false, precision = 36, scale = 18)
    private BigDecimal priceUSD = BigDecimal.ZERO;

    @NotNull
    @Column(nullable = false)
    private Long sortOrder;

}
