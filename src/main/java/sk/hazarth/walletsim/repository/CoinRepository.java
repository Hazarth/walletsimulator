package sk.hazarth.walletsim.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sk.hazarth.walletsim.domain.Coin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CoinRepository extends PagingAndSortingRepository<Coin, Long> {

    List<Coin> findAll();

    Optional<Coin> findOneBySymbol(String symbol);

    @Query(value = "SELECT c.symbol FROM Coin c")
    Set<String> findAllSymbols();

}
