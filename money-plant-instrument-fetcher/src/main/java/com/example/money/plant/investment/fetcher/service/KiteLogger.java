package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.config.TradeProperties;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
@Log4j2
@RequiredArgsConstructor
public class KiteLogger {

	private final TradeProperties tradeProperties;

	public KiteConnect createConnection() throws Exception, KiteException {
		// log.info("__INIT__ Creating a connection with Kite");
		//
		// KiteConnect kiteConnect = new KiteConnect("5fj59n3ybp8myx8d");
		// kiteConnect.setUserId("FWZ560");
		// log.info("__KITE Basic Properties Set __ ");
		// String url = kiteConnect.getLoginURL();
		// log.info("LOGIN URL: [{}]", url);
		// User user = kiteConnect.generateSession("rZNHmtBE6UlqaCCFubUdv0wVBeUEv0Ej",
		// "47vuu1bgx0byozj6nvck0yar3h0g1q22");
		// kiteConnect.setAccessToken(user.accessToken);
		// kiteConnect.setPublicToken(user.publicToken);

		log.info("__INIT__ Creating a connection with Kite");
		KiteConnect kiteConnect = new KiteConnect(tradeProperties.getApiKey());
		kiteConnect.setUserId(tradeProperties.getUserId());
		log.info("__KITE Basic Properties Set __ ");
		String loginUrl = kiteConnect.getLoginURL();
		log.info("Login_URL is - [{}]", loginUrl);

		// TODO: ADD LOGIC TO CHECK FROM GEMFIRE CACHE
		if (Objects.nonNull(tradeProperties.getAccessToken()) && !tradeProperties.getAccessToken().isEmpty()
				&& !tradeProperties.getAccessToken().isBlank()
				&& StringUtils.hasText(tradeProperties.getAccessToken())) {
			log.info("ACCESS Token is already present, returning Connection");
			kiteConnect.setAccessToken(tradeProperties.getAccessToken());
			return kiteConnect;
		}
		log.info("Trade Properties are : [{},{},{}]", tradeProperties.getApiKey(), tradeProperties.getApiSecret(),
				tradeProperties.getAccessToken());
		try {
			User user = kiteConnect.generateSession(tradeProperties.getRequestToken(), tradeProperties.getApiSecret());
			log.info("__ LOGGED IN USER IS __ : [{}]", user.shortName);
			log.info("__ ACCESS TOKEN IS __ : [{}]", user.accessToken);
			kiteConnect.setPublicToken(user.publicToken);
			kiteConnect.setAccessToken(user.accessToken);
			tradeProperties.setAccessToken(user.accessToken);
			log.info("__Kite Connection Created Successfully__");
			return kiteConnect;
		}
		catch (Exception exception) {
			log.error("Error Creating A Kite Connection. Request Token Issue ");

			log.warn("Fetch Request Token from Login URL and pass it in Properties File To Continue");

			throw new Exception(exception.getMessage());
		}
	}

}
