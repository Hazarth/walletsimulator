package sk.hazarth.walletsim;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import sk.hazarth.walletsim.domain.Coin;
import sk.hazarth.walletsim.domain.Wallet;
import sk.hazarth.walletsim.dto.WalletCreateDTO;
import sk.hazarth.walletsim.repository.CoinRepository;
import sk.hazarth.walletsim.repository.WalletRepository;
import sk.hazarth.walletsim.service.CryptoCompareService;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WalletSimApplication.class)
public class WalletSimTest {
    private static final String DTO_SYMBOL_FIELD = "symbol";

    @LocalServerPort
    private int port;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CoinRepository coinRepository;

    private Wallet existingWallet = new Wallet();

    @Before
    public void setup(){

        existingWallet.setDeleted(false);
        existingWallet.setFriendlyName("ExistingWallet");
        existingWallet.setCoin(coinRepository.findOneBySymbol("BTC").get());

        walletRepository.save(existingWallet);
    }

    @After
    public void teardown() {
        walletRepository.delete(existingWallet);
    }

    @Test
    public void CreateWallet_OK_Test() {
        WalletCreateDTO createDTO = new WalletCreateDTO();
        createDTO.setCoinSymbol("BTC");
        createDTO.setFriendlyName("MyFirstWallet");

        given().port(port)
                .body(createDTO)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .post("/wallet")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(DTO_SYMBOL_FIELD, Matchers.is("BTC"))
                .body("uuid", Matchers.notNullValue())
                .body("friendlyName", Matchers.is("MyFirstWallet"))
                .body("balance", Matchers.is(0));
    }

    @Test
    public void CreateWallet_UniqueNameFailure_Test() {
        WalletCreateDTO createDTO = new WalletCreateDTO();
        createDTO.setCoinSymbol("BTC");
        createDTO.setFriendlyName(existingWallet.getFriendlyName());

        given().port(port)
                .body(createDTO)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .post("/wallet")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void GetWallet_NonExisting_Test() {
        given().port(port)
                .get("/wallet/00000000-0000-0000-0000-000000000000")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void GetWallet_Existing_Test() {
        given().port(port)
                .pathParam("uuid", existingWallet.getUuid())
                .get("/wallet/{uuid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(DTO_SYMBOL_FIELD, Matchers.is(existingWallet.getCoin().getSymbol()))
                .body("uuid", Matchers.is(existingWallet.getUuid().toString()))
                .body("friendlyName", Matchers.is(existingWallet.getFriendlyName()))
                .body("balance", Matchers.is(0));
    }

    @Test
    public void UpdateWallet_OK_Test() {
        WalletCreateDTO createDTO = new WalletCreateDTO();
        createDTO.setCoinSymbol("LTC");
        createDTO.setFriendlyName("RenamedWallet");

        given().port(port)
                .body(createDTO)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .pathParam("uuid", existingWallet.getUuid())
                .put("/wallet/{uuid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(DTO_SYMBOL_FIELD, Matchers.is("BTC"))
                .body("uuid", Matchers.is(existingWallet.getUuid().toString()))
                .body("friendlyName", Matchers.is("RenamedWallet"))
                .body("balance", Matchers.is(0));
    }

    @Test
    public void deleteWallet_OK_Test() {
        given().port(port)
                .pathParam("uuid", existingWallet.getUuid())
                .delete("/wallet/{uuid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given().port(port)
                .pathParam("uuid", existingWallet.getUuid())
                .get("/wallet/{uuid}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}
