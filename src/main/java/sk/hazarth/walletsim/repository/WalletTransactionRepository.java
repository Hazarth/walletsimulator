package sk.hazarth.walletsim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.domain.WalletTransaction;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {

    @Query(value = "SELECT sum(CASE t.type WHEN 'DEPOSIT' THEN t.amount ELSE -t.amount END) FROM WalletTransaction t where wallet = :wlt")
    BigDecimal getBalance(@Param("wlt") Wallet wallet);

}
