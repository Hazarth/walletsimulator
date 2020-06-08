package sk.hazarth.walletsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableAutoConfiguration
@SpringBootApplication
public class WalletSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletSimApplication.class, args);
	}

}
