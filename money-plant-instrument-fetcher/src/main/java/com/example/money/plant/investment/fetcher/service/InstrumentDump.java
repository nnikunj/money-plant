package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.config.TradeProperties;
import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import com.example.money.plant.investment.fetcher.entity.KiteConnectEntity;
import com.example.money.plant.investment.fetcher.repository.InstrumentRepository;
import com.example.money.plant.investment.fetcher.repository.KiteConnectRepository;
import com.example.money.plant.investment.fetcher.response.InstrumentResponse;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class InstrumentDump {

    private final TradeProperties tradeProperties;

    private final KiteConnectRepository kiteConnectRepository;

    private final InstrumentRepository instrumentRepository;

    public List<Instrument> getAllInstrumentsData() throws KiteException, Exception {
        log.info("__INIT__ GETTING INSTRUMENT DATA ");
        KiteConnectEntity fetchedEntity = kiteConnectRepository.findByUserId(tradeProperties.getUserId());

        KiteConnect connect = new KiteConnect(tradeProperties.getApiKey());
        connect.setUserId(tradeProperties.getUserId());
        connect.setAccessToken(fetchedEntity.getAccessToken());

        List<Instrument> instruments = connect.getInstruments("NSE");
        log.info("Instruments Size is : [{}]", instruments.size());
        log.info("Dumping Data To DB: ");

        instruments.forEach(instrument -> {
            instrumentRepository.save(new InstrumentEntity(instrument.getInstrument_token(),
                    instrument.getExchange_token(),
                    instrument.getTradingsymbol(),instrument.getName(),instrument.getInstrument_type(),instrument.getSegment(),
                    instrument.getExchange()
                    ));
        });
        return instruments;
    }

    public List<InstrumentResponse> getInstrumentDataBySymbol(String symbol) {
        return instrumentRepository.findInstrumentDataBySymbol(symbol);
    }
}
