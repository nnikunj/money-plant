package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.config.TradeProperties;
import com.example.money.plant.investment.fetcher.entity.KiteConnectEntity;
import com.example.money.plant.investment.fetcher.repository.KiteConnectRepository;
import com.example.money.plant.investment.fetcher.response.KiteConnectResponse;
import com.example.rmqconnector.RmqUtils;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Log4j2
@Service
public class ApplicationStarter implements ApplicationListener<ApplicationReadyEvent> {

	private final KiteLogger kiteLogger;

	private final TradeProperties tradeProperties;

	private final KiteConnectRepository kiteConnectRepository;


	private final InstrumentDump instrumentDump;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		try {
			TimeUnit.SECONDS.sleep(5);

//			log.info("Creating Queue With Name: [ infy-ltp ]");
//			rmqUtils.createQueueWithName("infy-ltp");

			log.info("__TRYING KITE LOGIN __");
			KiteConnect connect = kiteLogger.createConnection();
			log.info("Creating a Kite Cache Entity");
			log.info("ACCESS TOKEN: [{}]", connect.getAccessToken());
			KiteConnectEntity entity = new KiteConnectEntity(connect.getUserId(), connect.getApiKey(),
					tradeProperties.getApiSecret(), connect.getAccessToken());
			kiteConnectRepository.save(entity);
			log.info("CACHE OBJECT SAVED");

			log.info("FETCHING  SAVED OBJECT ");
			TimeUnit.SECONDS.sleep(5);

			log.info("getting instrument data");
			instrumentDump.getAllInstrumentsData();

		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		catch (Exception e) {
			log.error("Exception Occurred: [{}]", e.getMessage());
			throw new RuntimeException(e);
		}
		catch (KiteException e) {
			log.error("Exception Occurred: [{}]", e.getMessage());
			throw new RuntimeException(e);
		}

	}

	public KiteConnectResponse getKiteCredentialsFromCache(){
		KiteConnectEntity fetchedEntity = kiteConnectRepository.findByUserId(tradeProperties.getUserId());
		log.info("Fetched KITE Entity is : [{}]", fetchedEntity.toString());
		return  new KiteConnectResponse(fetchedEntity.getUserId(),fetchedEntity.getApiKey(),fetchedEntity.getApiSecret(),fetchedEntity.getAccessToken());
	}

}
