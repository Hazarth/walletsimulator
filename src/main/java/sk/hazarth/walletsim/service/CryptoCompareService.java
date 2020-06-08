package sk.hazarth.walletsim.service;

import sk.hazarth.walletsim.exception.CryptoCompareError;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface CryptoCompareService {

        Map<String, BigDecimal> resolveCoinPairPrices(Collection<String> fromSyms, Collection<String> toSyms);

        BigDecimal resolveCoinPairPrice(String from, String to) throws CryptoCompareError;

        /**
         * generate a cache key for use with this service. This key is the representation of data described by a currency
         * pair.
         * @param from currency symbol
         * @param to currency symbol
         * @return an object representing the cache key this service uses
         */
        String getCacheKey(String from, String to);
}
