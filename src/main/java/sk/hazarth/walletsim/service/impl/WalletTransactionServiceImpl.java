package sk.hazarth.walletsim.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import sk.hazarth.walletsim.domain.TransactionType;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.domain.WalletTransaction;
import sk.hazarth.walletsim.repository.WalletTransactionRepository;
import sk.hazarth.walletsim.service.WalletTransactionService;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Slf4j
@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public BigDecimal calculateBalanceFor(Wallet wallet){
        BigDecimal result = walletTransactionRepository.getBalance(wallet);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createTransactionFor(Wallet wallet, TransactionType type, BigDecimal amount) {

        WalletTransaction transaction = new WalletTransaction();

        transaction.setType(type);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setCurrencyCode(wallet.getCoin().getSymbol());
        transaction.setCurrencyFullName(wallet.getCoin().getSymbol());

        walletTransactionRepository.save(transaction);
    }
}
