package com.example.datacollectionservice.collector;

import com.example.datacollectionservice.configuration.KiteProperties;
import com.example.datacollectionservice.connector.KiteConnector;
import com.example.datacollectionservice.response.InstrumentResponse;
import com.example.datacollectionservice.response.KiteConnectResponse;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnTicks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class KiteDataCollector {

	private final KiteProperties kiteProperties;

	private final KiteConnector kiteConnector;

	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(fixedDelayString = "${datacollection.poleTime:10}", initialDelayString = "${datacollection.initDelay:5}",
			timeUnit = TimeUnit.SECONDS)
	public void dataCollector() throws Exception, KiteException {
		ResponseEntity<KiteConnectResponse> kiteResponseEntity = restTemplate
				.getForEntity("http://localhost:8080/api/kite/credential",KiteConnectResponse.class);
//		log.info("__INIT__ FETCHING DATA FROM KITE BROKER PLATFORM __ ");
//		KiteConnect connector = kiteConnector.createConnection();
		KiteConnectResponse kiteConnectResponse = kiteResponseEntity.getBody();
		log.info("ACCESS Token is [{}]", kiteConnectResponse.getAccessToken());
		// KiteConnect connector = kiteConnector.createConnection();
		KiteTicker tickerProvider = new KiteTicker(kiteConnectResponse.getAccessToken(), kiteConnectResponse.getApiKey());

		ResponseEntity<List<InstrumentResponse>> responseEntity = restTemplate
				.exchange("http://localhost:8080/api/instruments/INFY", HttpMethod.GET, null,  new ParameterizedTypeReference<List<InstrumentResponse>>() {
				});

		List<InstrumentResponse> instrumentResponse = responseEntity.getBody();
		ArrayList<Long> tickerToken = new ArrayList<>();
		tickerToken.addAll(instrumentResponse.stream().map(e->e.instrument_token).collect(Collectors.toList()));
		log.info("Ticker Tokens [{}]", tickerToken);
		Tick ongcTick = new Tick();

		tickerProvider.setOnConnectedListener(new OnConnect() {
			@Override
			public void onConnected() {
				tickerProvider.subscribe(tickerToken);
				tickerProvider.setMode(tickerToken, KiteTicker.modeFull);
			}
		});

		tickerProvider.setOnTickerArrivalListener(new OnTicks() {
			@Override
			public void onTicks(ArrayList<Tick> ticks) {
				log.info("INSIDE TICK FETCH");
				NumberFormat formatter = new DecimalFormat();
				System.out.println("ticks size " + ticks.size());
				if (ticks.size() > 0) {
					System.out.println("last price " + ticks.get(0).getLastTradedPrice());
					System.out.println("open price " + formatter.format(ticks.get(0).getOpenPrice()));
					System.out.println("high price " + formatter.format(ticks.get(0).getHighPrice()));
					System.out.println("low price " + formatter.format(ticks.get(0).getLowPrice()));
					System.out.println("close price " + formatter.format(ticks.get(0).getClosePrice()));
					System.out.println("traded volumes " + formatter.format(ticks.get(0).getVolumeTradedToday()));

				}
			}
		});

		tickerProvider.setTryReconnection(true);
		// maximum retries and should be greater than 0
		tickerProvider.setMaximumRetries(10);
		// set maximum retry interval in seconds
		tickerProvider.setMaximumRetryInterval(30);

		/**
		 * connects to com.zerodhatech.com.zerodhatech.ticker server for getting live
		 * quotes
		 */
		tickerProvider.connect();

		/**
		 * You can check, if websocket connection is open or not using the following
		 * method.
		 */
		boolean isConnected = tickerProvider.isConnectionOpen();
		System.out.println(isConnected);

		/**
		 * set mode is used to set mode in which you need tick for list of tokens. Ticker
		 * allows three modes, modeFull, modeQuote, modeLTP. For getting only last traded
		 * price, use modeLTP For getting last traded price, last traded quantity, average
		 * price, volume traded today, total sell quantity and total buy quantity, open,
		 * high, low, close, change, use modeQuote For getting all data with depth, use
		 * modeFull
		 */
		tickerProvider.setMode(tickerToken, KiteTicker.modeFull);

		// // Unsubscribe for a token.
		// tickerProvider.unsubscribe(tickerToken);
		//
		// // After using com.zerodhatech.com.zerodhatech.ticker, close websocket
		// connection.
		// tickerProvider.disconnect();
	}

}
