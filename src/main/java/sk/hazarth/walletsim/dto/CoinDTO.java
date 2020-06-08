package sk.hazarth.walletsim.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinDTO {

    private String imageUrl;

    private String name;

    private String symbol;

    private String coinName;

    private String fullName;

    private Integer decimalPlaces;

    private String price;

}
