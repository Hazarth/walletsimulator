package sk.hazarth.walletsim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.hazarth.walletsim.dto.PurchaseDTO;
import sk.hazarth.walletsim.dto.TransferDTO;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.dto.WalletDTO;
import sk.hazarth.walletsim.exception.AbstractException;
import sk.hazarth.walletsim.mapper.WalletMapper;
import sk.hazarth.walletsim.service.WalletService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletMapper walletMapper;

    @PostMapping
    public ResponseEntity<WalletDTO> createWallet(@RequestBody WalletCreateDTO walletDto) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.createWallet(walletDto)));
    }

    @GetMapping("/search")
    public ResponseEntity<WalletDTO> getWalletByFriendlyName(@RequestParam String friendlyName) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.findByFriendlyName(friendlyName)));
    }

    @GetMapping("{uuid}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable UUID uuid) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.getWallet(uuid)));
    }

    @PutMapping("{uuid}")
    public ResponseEntity<WalletDTO> updateWallet(@PathVariable UUID uuid, @RequestBody WalletCreateDTO walletDTO) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.updateWallet(uuid, walletDTO)));
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteWallet(@PathVariable UUID uuid) throws AbstractException {
        walletService.deleteWallet(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{uuid}/buy")
    public ResponseEntity<WalletDTO> buyCoins(@PathVariable UUID uuid, @Valid @RequestBody PurchaseDTO purchaseDTO) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.purchase(uuid, purchaseDTO)));
    }

    @PostMapping("{uuid}/transfer")
    public ResponseEntity<WalletDTO> sendCoins(@PathVariable UUID uuid, @RequestBody TransferDTO transferDTO) throws AbstractException {
        return ResponseEntity.ok(walletMapper.toDto(walletService.transfer(uuid, transferDTO)));
    }

}
