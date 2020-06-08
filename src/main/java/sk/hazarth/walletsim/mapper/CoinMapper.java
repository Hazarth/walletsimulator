package sk.hazarth.walletsim.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sk.hazarth.walletsim.domain.Coin;
import sk.hazarth.walletsim.dto.CoinDTO;
import sk.hazarth.walletsim.feign.data.CoinDetail;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CoinMapper {

    @Mapping(target = "id", ignore = true)
    Coin fromDetail(CoinDetail detail);

    @Mapping(target = "price", expression = "java((coin.getDecimalPlaces() <= 0 ? coin.getPriceUSD().stripTrailingZeros() : coin.getPriceUSD().setScale(coin.getDecimalPlaces())).toString())")
    CoinDTO toDto(Coin coin);
}
