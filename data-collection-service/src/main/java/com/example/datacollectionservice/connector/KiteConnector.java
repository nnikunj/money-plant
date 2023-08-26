package com.example.datacollectionservice.connector;

import com.example.datacollectionservice.configuration.KiteProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.*;
import org.json.JSONObject;
import com.zerodhatech.models.Margin;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
@Log4j2
@RequiredArgsConstructor
public class KiteConnector {

	private final KiteProperties kiteProperties;

	// This class will be used to initiate a kite connection on service startup

	public KiteConnect createConnection() throws Exception, KiteException {
		log.info("__INIT__ Creating a connection with Kite");
		KiteConnect kiteConnect = new KiteConnect(kiteProperties.getApiKey());
		kiteConnect.setUserId(kiteProperties.getUserId());
		log.info("__KITE Basic Properties Set __ ");
		String loginUrl = kiteConnect.getLoginURL();
		log.info("Login_URL is - [{}]", loginUrl);

		if (Objects.nonNull(kiteProperties.getAccessToken()) && !kiteProperties.getAccessToken().isEmpty()
				&& !kiteProperties.getAccessToken().isBlank() && StringUtils.hasText(kiteProperties.getAccessToken())) {
			log.info("ACCESS Token is already present, returning Connection");
			kiteConnect.setAccessToken(kiteProperties.getAccessToken());
			return kiteConnect;
		}

		try {
			User user = kiteConnect.generateSession(kiteProperties.getRequestToken(), kiteProperties.getApiSecret());
			log.info("__ LOGGED IN USER IS __ : [{}]", user.shortName);
			log.info("__ ACCESS TOKEN IS __ : [{}]", user.accessToken);
			kiteConnect.setAccessToken(user.accessToken);
			kiteProperties.setAccessToken(user.accessToken);
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
