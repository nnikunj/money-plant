package com.example.money.plant.investment.fetcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "kite")
@Validated
@Data
@Configuration
public class TradeProperties {

	@NotNull
	private String apiKey;

	@NotNull
	private String apiSecret;

	@NotNull
	private String userId;

	private String requestToken;

	private String accessToken;

	// private String tradeInstrumentExchange;
	//
	// private String tradeStocks;
	//
	// private String[] exchange = StringUtils.split(tradeInstrumentExchange, ",");
	//
	// private String[] stocks = StringUtils.split(tradeStocks, ",");

}
