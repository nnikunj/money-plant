package com.example.money.plant.investment.fetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.example" })
public class MoneyPlantInstrumentFetcher {

	public static void main(String[] args) {
		SpringApplication.run(MoneyPlantInstrumentFetcher.class);
	}

}
