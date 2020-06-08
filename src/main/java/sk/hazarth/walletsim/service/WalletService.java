package sk.hazarth.walletsim.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.hazarth.walletsim.domain.TransactionType;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.dto.PurchaseDTO;
import sk.hazarth.walletsim.dto.TransferDTO;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.dto.WalletDTO;
import sk.hazarth.walletsim.exception.*;
import sk.hazarth.walletsim.mapper.WalletMapper;
import sk.hazarth.walletsim.repository.WalletRepository;

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
public class WalletService {

    @Autowired
    private CryptoCompareService cryptoCompareService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private CoinService coinService;

    /**
     * Creates a new wallet given the provided {@link WalletDTO}
     * @param dto dto containing necessary fileds to create a new wallet
     * @return a new {@link Wallet} instance
     * @throws FriendlyNameAlreadyExists
     * @throws CoinNotFound
     */
    public Wallet createWallet(WalletCreateDTO dto) throws FriendlyNameAlreadyExists, CoinNotFound {
        validateDto(dto);
        Wallet newWallet = walletMapper.fromCreateDto(dto);
        newWallet.setCoin(coinService.getCoin(dto.getCoinSymbol()));

        return walletRepository.save(newWallet);
    }

    /**
     * Retrieves a single {@link Wallet} instance if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param uuid the uuid of the {@link Wallet}
     * @return an existing {@link Wallet} instance
     * @throws WalletNotFound
     */
    public Wallet getWallet(UUID uuid) throws WalletNotFound {
        return walletRepository.findById(uuid).orElseThrow(() -> new WalletNotFound(uuid.toString()));
    }

    /**
     * Retrieves a single {@link Wallet} instance by its friendly name if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param friendlyName the friendly name of the {@link Wallet}
     * @return an existing {@link Wallet} instance
     * @throws WalletNotFound
     */
    public Wallet findByFriendlyName(String friendlyName) throws WalletNotFound {
        return walletRepository.findOneByFriendlyName(friendlyName).orElseThrow(() -> new WalletNotFound(friendlyName));
    }

    /**
     * Updates a single {@link Wallet} instance if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param uuid the uuid of the {@link Wallet}
     * @return an updated {@link Wallet} instance
     * @throws WalletNotFound
     * @throws FriendlyNameAlreadyExists
     */
    public Wallet updateWallet(UUID uuid, WalletCreateDTO dto) throws WalletNotFound, FriendlyNameAlreadyExists {
        validateDto(dto);
        Wallet wallet = walletMapper.fromUpdateDto(dto, getWallet(uuid));
        return walletRepository.save(wallet);
    }

    /**
     * soft deletes a {@link Wallet} if it exists, throws {@link WalletNotFound} exception otherwise.
     * We do a soft delete to keep {@link sk.hazarth.walletsim.domain.WalletTransaction} history consistent
     * @param uuid uuid of the {@link Wallet}
     * @throws WalletNotFound
     */
    public void deleteWallet(UUID uuid) throws WalletNotFound {
        Wallet wallet = getWallet(uuid);
        wallet.setDeleted(true);
        walletRepository.save(wallet);
    }

    public Wallet purchase(UUID destinationWalletUUID, @Valid PurchaseDTO purchaseDTO) throws AbstractException {
        Wallet wallet = getWallet(destinationWalletUUID);

        BigDecimal price = cryptoCompareService.resolveCoinPairPrice(purchaseDTO.getCurrencySymbol(), wallet.getCoin().getSymbol());
        walletTransactionService.createTransactionFor(wallet, TransactionType.DEPOSIT, purchaseDTO.getAmount().multiply(price));

        return wallet;
    }

    public Wallet transfer(UUID source, @Valid TransferDTO transferDTO) throws AbstractException {
        Wallet sourceWallet = getWallet(source);
        Wallet destinationWallet = getWallet(transferDTO.getDestinationWallet());

        BigDecimal sourceBalance = walletTransactionService.calculateBalanceFor(sourceWallet);

        if(sourceBalance.compareTo(transferDTO.getAmount()) < 0){
            throw new NotEnoughFunds(sourceWallet);
        }

        BigDecimal price = cryptoCompareService.resolveCoinPairPrice(
                sourceWallet.getCoin().getSymbol(),
                destinationWallet.getCoin().getSymbol());

        BigDecimal destAmount = transferDTO.getAmount().multiply(price);

        walletTransactionService.createTransactionFor(sourceWallet, TransactionType.WITHDRAWAL, transferDTO.getAmount());
        walletTransactionService.createTransactionFor(destinationWallet, TransactionType.DEPOSIT, destAmount);

        return sourceWallet;
    }

    private void validateDto(WalletCreateDTO dto) throws FriendlyNameAlreadyExists {
        Optional<Wallet> existing = walletRepository.findOneByFriendlyName(dto.getFriendlyName());
        if (existing.isPresent()) {
            throw new FriendlyNameAlreadyExists(dto.getFriendlyName());
        }


    }
}
