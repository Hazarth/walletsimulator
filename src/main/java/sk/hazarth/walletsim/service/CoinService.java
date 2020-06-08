package sk.hazarth.walletsim.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sk.hazarth.walletsim.domain.Coin;
import sk.hazarth.walletsim.dto.CoinDTO;
import sk.hazarth.walletsim.exception.CoinNotFound;
import sk.hazarth.walletsim.mapper.CoinMapper;
import sk.hazarth.walletsim.repository.CoinRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinMapper coinMapper;

    public Page<CoinDTO> getPage(Pageable pageable, String to) {
        return coinRepository.findAll(pageable).map(coinMapper::toDto);
    }

    public Coin getCoin(Long id) throws CoinNotFound {
        return coinRepository.findById(id).orElseThrow(() -> new CoinNotFound(id));
    }

    public Coin getCoin(String symbol) throws CoinNotFound {
        return coinRepository.findOneBySymbol(symbol).orElseThrow(() -> new CoinNotFound(symbol));
    }

}
