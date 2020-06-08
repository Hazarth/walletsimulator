package sk.hazarth.walletsim.service;

import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.dto.PurchaseDTO;
import sk.hazarth.walletsim.dto.TransferDTO;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.dto.WalletDTO;
import sk.hazarth.walletsim.exception.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Interface describing necessary actions of a WalletService
 */
public interface WalletService {
    /**
     * Creates a new wallet given the provided {@link WalletDTO}
     * @param dto dto containing necessary fileds to create a new wallet
     * @return a new {@link Wallet} instance
     * @throws FriendlyNameAlreadyExists
     * @throws CoinNotFound
     */
    Wallet createWallet(WalletCreateDTO dto) throws FriendlyNameAlreadyExists, CoinNotFound;

    /**
     * Retrieves a single {@link Wallet} instance if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param uuid the uuid of the {@link Wallet}
     * @return an existing {@link Wallet} instance
     * @throws WalletNotFound
     */
    Wallet getWallet(UUID uuid) throws WalletNotFound;

    /**
     * Retrieves a single {@link Wallet} instance by its friendly name if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param friendlyName the friendly name of the {@link Wallet}
     * @return an existing {@link Wallet} instance
     * @throws WalletNotFound
     */
    Wallet findByFriendlyName(String friendlyName) throws WalletNotFound;

    /**
     * Updates a single {@link Wallet} instance if it exists, throws an {@link WalletNotFound} exception otherwise
     * @param uuid the uuid of the {@link Wallet}
     * @return an updated {@link Wallet} instance
     * @throws WalletNotFound
     * @throws FriendlyNameAlreadyExists
     */
    Wallet updateWallet(UUID uuid, WalletCreateDTO dto) throws WalletNotFound, FriendlyNameAlreadyExists;

    /**
     * soft deletes a {@link Wallet} if it exists, throws {@link WalletNotFound} exception otherwise.
     * We do a soft delete to keep {@link sk.hazarth.walletsim.domain.WalletTransaction} history consistent
     * @param uuid uuid of the {@link Wallet}
     * @throws WalletNotFound
     */
    void deleteWallet(UUID uuid) throws WalletNotFound;

    /**
     * Deposits currency to the selected destination wallet using the specified amount of another currency.
     *
     * E.g.:
     * Buy 500 Eur worth of BTC (dictated by the wallet)
     * @param destinationWalletUUID the uuid of the destination {@link Wallet} entity
     * @param purchaseDTO the transaction details
     * @return returns an updated state of the destination {@link Wallet} entity
     * @throws CryptoCompareError
     * @throws WalletNotFound
     */
    Wallet purchase(UUID destinationWalletUUID, @Valid PurchaseDTO purchaseDTO) throws AbstractException;

    /**
     * Transfers funds from the source wallet to the destination wallet.
     * @param source the uuid of the source {@link Wallet} entity.
     * @param transferDTO the transaction details holding the destination and amount of currency to transfer
     * @return new state of the destination {@link Wallet} entity.
     * @throws CryptoCompareError
     * @throws WalletNotFound
     */
    Wallet transfer(UUID source, @Valid TransferDTO transferDTO) throws AbstractException;
}
