package sk.hazarth.walletsim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.hazarth.walletsim.feign.data.CoinDetail;
import sk.hazarth.walletsim.feign.data.CryptoCompareResponse;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

@FeignClient(value = "cryptoCompareClient", url = "https://min-api.cryptocompare.com/")
public interface CryptoCompareClient {

    @RequestMapping("data/all/coinlist")
    ResponseEntity<CryptoCompareResponse<Map<String, CoinDetail>>> coinlist();

    @RequestMapping("data/price")
    ResponseEntity<Map<String,Double>> singleSymbolPrice(@RequestParam String fsym, @RequestParam Collection<String> tsyms);

    @RequestMapping("data/pricemulti")
    ResponseEntity<Map<String,Map<String, BigDecimal>>> multiSymbolPrice(@RequestParam Collection<String> fsyms, @RequestParam Collection<String> tsyms);

}
