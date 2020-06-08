package sk.hazarth.walletsim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.hazarth.walletsim.domain.Coin;
import sk.hazarth.walletsim.dto.CoinDTO;
import sk.hazarth.walletsim.exception.CoinNotFound;

/**
 * An interface describing functionality necessary for a CoinService
 */
public interface CoinService {

    /**
     * Retrieve a paginated result of available cryptocurrency coins in the form
     * of a {@link Page<CoinDTO>}.
     * @param pageable pagination details
     * @return {@link Page<CoinDTO>}
     */
    Page<CoinDTO> getPage(Pageable pageable);

    /**
     * Get a single entity instance of {@link Coin}by its ID if it exists, exception is thrown otherwise
     * @param id id of the coin
     * @return {@link Coin} entity
     * @throws CoinNotFound
     */
    Coin getCoin(Long id) throws CoinNotFound;

    /**
     * Get a single entity instance of {@link Coin} by its symbol if it exists, exception is thrown otherwise
     * @param symbol the currency symbol
     * @return {@link Coin} entity
     * @throws CoinNotFound
     */
    Coin getCoin(String symbol) throws CoinNotFound;

}
