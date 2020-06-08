package sk.hazarth.walletsim.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import sk.hazarth.walletsim.domain.TransactionType;
import sk.hazarth.walletsim.domain.Wallet;

import java.math.BigDecimal;

/**
 * Interface describing the actions of a WalletTransaction service.
 */
public interface WalletTransactionService {

    /**
     * Calculates the current balance on the selected {@link Wallet}.
     * @param wallet the wallet to check.
     * @return the current balance on the {@link Wallet}.
     */
    BigDecimal calculateBalanceFor(Wallet wallet);

    /**
     * Creates a new DEPOSIT/WITHDRAWAL transaction for the selected {@link Wallet}.
     * Transaction creation is serialized to prevent double spending
     * @param wallet the wallet to add a transaction to
     * @param type DEPOSIT or WITHDRAWAL from {@link TransactionType}
     * @param amount amount of funds in the transaction
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void createTransactionFor(Wallet wallet, TransactionType type, BigDecimal amount);
}
