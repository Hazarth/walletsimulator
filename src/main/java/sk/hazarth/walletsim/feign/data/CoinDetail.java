package sk.hazarth.walletsim.feign.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class CoinDetail {
    @JsonProperty("Id")
    private Integer id;

    @JsonProperty("ImageUrl")
    private String imageUrl;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Symbol")
    private String symbol;

    @JsonProperty("CoinName")
    private String coinName;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("DecimalPlaces")
    private Integer decimalPlaces;

    @JsonProperty("SortOrder")
    private Long sortOrder;

    @JsonProperty("IsTrading")
    private boolean isTrading;

    @JsonProperty("TotalCoinSupply")
    private String totalCoinSupply;
}
