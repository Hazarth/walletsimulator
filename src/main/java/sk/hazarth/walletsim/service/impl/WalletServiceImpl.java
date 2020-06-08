package sk.hazarth.walletsim.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.hazarth.walletsim.domain.TransactionType;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.dto.PurchaseDTO;
import sk.hazarth.walletsim.dto.TransferDTO;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.exception.*;
import sk.hazarth.walletsim.mapper.WalletMapper;
import sk.hazarth.walletsim.repository.WalletRepository;
import sk.hazarth.walletsim.service.CryptoCompareService;
import sk.hazarth.walletsim.service.WalletService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages {@link Wallet} CRUD operations
 */
@Log4j2
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    @Autowired
    private CryptoCompareService cryptoCompareService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private WalletTransactionServiceImpl walletTransactionServiceImpl;

    @Autowired
    private CoinServiceImpl coinServiceImpl;

    @Override
    public Wallet createWallet(WalletCreateDTO dto) throws FriendlyNameAlreadyExists, CoinNotFound {
        validateDto(dto);
        Wallet newWallet = walletMapper.fromCreateDto(dto);
        newWallet.setCoin(coinServiceImpl.getCoin(dto.getCoinSymbol()));

        return walletRepository.save(newWallet);
    }

    @Override
    public Wallet getWallet(UUID uuid) throws WalletNotFound {
        return walletRepository.findById(uuid).orElseThrow(() -> new WalletNotFound(uuid.toString()));
    }

    @Override
    public Wallet findByFriendlyName(String friendlyName) throws WalletNotFound {
        return walletRepository.findOneByFriendlyName(friendlyName).orElseThrow(() -> new WalletNotFound(friendlyName));
    }

    @Override
    public Wallet updateWallet(UUID uuid, WalletCreateDTO dto) throws WalletNotFound, FriendlyNameAlreadyExists {
        validateDto(dto);
        Wallet wallet = walletMapper.fromUpdateDto(dto, getWallet(uuid));
        return walletRepository.save(wallet);
    }

    @Override
    public void deleteWallet(UUID uuid) throws WalletNotFound {
        Wallet wallet = getWallet(uuid);
        wallet.setDeleted(true);
        walletRepository.save(wallet);
    }

    @Override
    public Wallet purchase(UUID destinationWalletUUID, @Valid PurchaseDTO purchaseDTO) throws AbstractException {
        Wallet wallet = getWallet(destinationWalletUUID);

        BigDecimal price = cryptoCompareService.resolveCoinPairPrice(purchaseDTO.getCurrencySymbol(), wallet.getCoin().getSymbol());
        walletTransactionServiceImpl.createTransactionFor(wallet, TransactionType.DEPOSIT, purchaseDTO.getAmount().multiply(price));

        return wallet;
    }

    @Override
    public Wallet transfer(UUID source, @Valid TransferDTO transferDTO) throws AbstractException {
        if(source.equals(transferDTO.getDestinationWallet())){
            throw new WalletNotAllowed("Destination wallet cannot be the same as source wallet.");
        }

        Wallet sourceWallet = getWallet(source);
        Wallet destinationWallet = getWallet(transferDTO.getDestinationWallet());

        BigDecimal sourceBalance = walletTransactionServiceImpl.calculateBalanceFor(sourceWallet);

        if(sourceBalance.compareTo(transferDTO.getAmount()) < 0){
            throw new NotEnoughFunds(sourceWallet);
        }

        BigDecimal price = cryptoCompareService.resolveCoinPairPrice(
                sourceWallet.getCoin().getSymbol(),
                destinationWallet.getCoin().getSymbol());

        BigDecimal destAmount = transferDTO.getAmount().multiply(price);

        walletTransactionServiceImpl.createTransactionFor(sourceWallet, TransactionType.WITHDRAWAL, transferDTO.getAmount());
        walletTransactionServiceImpl.createTransactionFor(destinationWallet, TransactionType.DEPOSIT, destAmount);

        return sourceWallet;
    }

    private void validateDto(WalletCreateDTO dto) throws FriendlyNameAlreadyExists {
        Optional<Wallet> existing = walletRepository.findOneByFriendlyName(dto.getFriendlyName());
        if (existing.isPresent()) {
            throw new FriendlyNameAlreadyExists(dto.getFriendlyName());
        }


    }
}
