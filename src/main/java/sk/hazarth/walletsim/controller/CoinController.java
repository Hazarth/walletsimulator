package sk.hazarth.walletsim.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hazarth.walletsim.dto.CoinDTO;
import sk.hazarth.walletsim.service.impl.CoinServiceImpl;

@RestController
@RequestMapping("coins")
public class CoinController {

    @Autowired
    private CoinServiceImpl coinServiceImpl;

    @GetMapping
    @ApiOperation("View all available CryptoCurrencies")
    public ResponseEntity<Page<CoinDTO>> listCoins(@PageableDefault(size = 20, sort = "sortOrder") Pageable pageable) {
        return ResponseEntity.ok(coinServiceImpl.getPage(pageable));
    }

}
