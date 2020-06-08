package sk.hazarth.walletsim.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.dto.WalletDTO;
import sk.hazarth.walletsim.service.WalletTransactionService;

@Mapper(componentModel = "spring")
public abstract class WalletMapper {

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Mapping(target = "uuid", ignore = true)
    abstract public Wallet fromCreateDto(WalletCreateDTO dto);

    @InheritConfiguration
    abstract public Wallet fromUpdateDto(WalletCreateDTO dto, @MappingTarget Wallet entity);

    @Mapping(target = "symbol", source = "coin.symbol")
    @Mapping(target = "currencyName", source = "coin.fullName")
    abstract public WalletDTO toDto(Wallet wallet);

    @AfterMapping
    public void afterMapping(Wallet wallet, @MappingTarget WalletDTO walletDTO){
        walletDTO.setBalance(walletTransactionService.calculateBalanceFor(wallet));
    }

}
