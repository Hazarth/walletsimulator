package sk.hazarth.walletsim.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sk.hazarth.walletsim.domain.Coin;
import sk.hazarth.walletsim.exception.CryptoCompareError;
import sk.hazarth.walletsim.feign.CryptoCompareClient;
import sk.hazarth.walletsim.feign.data.CoinDetail;
import sk.hazarth.walletsim.feign.data.CryptoCompareResponse;
import sk.hazarth.walletsim.helper.CryptoCompareHelper;
import sk.hazarth.walletsim.mapper.CoinMapper;
import sk.hazarth.walletsim.repository.CoinRepository;
import sk.hazarth.walletsim.service.CryptoCompareService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CryptoCompareServiceImpl implements CryptoCompareService {
    private Cache<String,BigDecimal> priceCache;

    @Value("${wallet-sim.app-currency:USD}")
    private String appCurrency;

    @Value("${wallet-sim.cache-expiration:5000}")
    private Integer cacheExpirationMillis = 5_000;

    @Autowired
    private CryptoCompareClient cryptoCompareClient;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private CoinRepository coinRepository;

    @PostConstruct
    public void init() throws CryptoCompareError {
        ResponseEntity<CryptoCompareResponse<Map<String, CoinDetail>>> response = cryptoCompareClient.coinlist();

        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            List<Coin> coins = response.getBody().getData().values().stream()
                    .filter(CoinDetail::isTrading)
                    .sorted(Comparator.comparingLong(CoinDetail::getSortOrder))
                    .limit(200)
                    .map(coinMapper::fromDetail)
                    .collect(Collectors.toList());

            coinRepository.saveAll(coins);
        }else{
            throw new CryptoCompareError("Cannot reach CryptoCompare API.");
        }

        priceCache = CacheBuilder.newBuilder().
                expireAfterWrite(cacheExpirationMillis, TimeUnit.MILLISECONDS)
                .initialCapacity(100)
                .maximumSize(300)
                .build();

        fetchCoinPrices();
    }

    public BigDecimal resolveCoinPairPrice(String from, String to) throws CryptoCompareError {
        Assert.notNull(from, "parameter from cannot be null.");
        Assert.notNull(to, "parameter to cannot be null.");

        //early exit if from and to are the same
        if (from.equalsIgnoreCase(to)) {
            return BigDecimal.ONE;
        }

        String conversionKey = getCacheKey(from, to);

        BigDecimal result = priceCache.getIfPresent(conversionKey);

        if(result == null){
            try {
                ResponseEntity<Map<String, Double>> responseEntity = cryptoCompareClient.singleSymbolPrice(from, Collections.singleton(to));

                if(responseEntity.getStatusCode() == HttpStatus.OK){
                    if(responseEntity.getBody() != null && responseEntity.getBody().containsKey(to)) {
                        result = BigDecimal.valueOf(responseEntity.getBody().get(to));
                        priceCache.put(conversionKey, result);
                    }else{
                        throw new CryptoCompareError("conversion not supported");
                    }
                }else{
                    throw new CryptoCompareError("Cannot reach CryptoCompare API.");
                }
            }catch (Exception e){
                return BigDecimal.ZERO;
            }
        }

        return result;
    }

    public String getCacheKey(String from, String to){
        return String.format("%s->%s",from, to);
    }

    /**
     * Keeps the most recently used pairs up-to-date for better user experience
     */
    @Scheduled(fixedDelayString = "#{ ${wallet-sim.fetch-cache:60000} }")
    void fetchCoinPrices() {

        Map<String, Coin> map = coinRepository.findAll().stream().collect(Collectors.toMap(Coin::getSymbol, Function.identity()));

        for(Collection<String> partition : CryptoCompareHelper.partitionParamCollection(map.keySet(), 300)){
            ResponseEntity<Map<String, Map<String, BigDecimal>>> results = cryptoCompareClient.multiSymbolPrice(partition,Collections.singleton(appCurrency));

            if(results.getStatusCode() == HttpStatus.OK) {
                if (results.getBody() != null) {
                    results.getBody().forEach((from, value) -> {
                        BigDecimal price = value.get(appCurrency);
                        map.get(from).setPriceUSD(price);

                        String cacheKey = getCacheKey(from, appCurrency);
                        priceCache.put(cacheKey, price);
                    });
                }
            }else {
                log.error("An error occurred while fetching CryptoCompare API values: {}", results.toString());
            }

        }

        coinRepository.saveAll(map.values());
    }
}
