package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import com.example.money.plant.investment.fetcher.entity.InstrumentModel;
import com.example.money.plant.investment.fetcher.model.InstrumentAddDto;
import com.example.money.plant.investment.fetcher.model.InstrumentResponseDto;
import com.example.money.plant.investment.fetcher.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class InstrumentFetcherService {

	private final InstrumentRepository instrumentRepository;

	private final String instrumentObjectName = "USER_INSTRUMENT_LIST";

	public InstrumentResponseDto getInstrumentList() {
		InstrumentEntity entity = instrumentRepository.findInstrumentEntityByName(instrumentObjectName);
		log.info("Fetching Present Instrument List");
		InstrumentResponseDto result = (InstrumentResponseDto) entity.getInstrumentList();
		return result;
	}

	public void addInstrument(InstrumentAddDto instruments) {
		log.info("Add Instrument Request");
		List<InstrumentModel> newInstruments = instruments.getInstrumentModelList();
		newInstruments.forEach(instrumentModel -> instrumentModel.setName(instrumentModel.getName().toUpperCase()));
		InstrumentEntity entity = instrumentRepository.findInstrumentEntityByName(instrumentObjectName);
		List<InstrumentModel> alreadyPresentInstruments = entity.getInstrumentList();

		try {
			newInstruments.removeAll(alreadyPresentInstruments);
			Set<InstrumentModel> temp = new HashSet<>(newInstruments);
			newInstruments.clear();
			newInstruments.addAll(temp);
			log.info("Adding the following elements to Instrument List: ", newInstruments);
			alreadyPresentInstruments.addAll(newInstruments);
			entity.setInstrumentList(alreadyPresentInstruments);
			instrumentRepository.save(entity);

		}
		catch (Exception e) {
			log.error("Unable to add Instrument due to : ", e);
			throw new RuntimeException("Instrument Addition Failure");
		}
	}

	public void removeInstrument(String instrumentObjectName) {
		String search = instrumentObjectName.toUpperCase();

		log.info("Removing Instrument: ", search);

		InstrumentEntity entity = instrumentRepository.findInstrumentEntityByName(instrumentObjectName);
		List<InstrumentModel> alreadyPresentInstruments = entity.getInstrumentList();

		alreadyPresentInstruments.remove(search);

		entity.setInstrumentList(alreadyPresentInstruments);

		instrumentRepository.save(entity);

	}

}
