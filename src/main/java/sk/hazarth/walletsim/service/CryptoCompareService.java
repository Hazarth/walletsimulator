package sk.hazarth.walletsim.service;

import sk.hazarth.walletsim.exception.CryptoCompareError;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * A high-level layer that handles communication with the CryptoCompare API, caching of data
 * and updating of necessary values
 */
public interface CryptoCompareService {

    /**
     * Resolve the price for a single coin pair
     * E.g.:
     * BTC -> EUR
     * EUR -> BTC
     *
     * @param from the currency to use as a source
     * @param to the destination currency
     * @return the amount of <i>to</i> currency for a single <i>from</i> currency
     * @throws CryptoCompareError
     */
    BigDecimal resolveCoinPairPrice(String from, String to) throws CryptoCompareError;

    /**
     * generate a cache key for use with this service. This key is the representation of data described by a currency
     * pair.
     *
     * @param from currency symbol
     * @param to   currency symbol
     * @return an object representing the cache key this service uses
     */
    String getCacheKey(String from, String to);
}
