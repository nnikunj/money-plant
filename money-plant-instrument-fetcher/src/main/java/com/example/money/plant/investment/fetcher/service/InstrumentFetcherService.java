package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import com.example.money.plant.investment.fetcher.entity.InstrumentModel;
import com.example.money.plant.investment.fetcher.model.InstrumentAddDto;
import com.example.money.plant.investment.fetcher.model.InstrumentResponseDto;
import com.example.money.plant.investment.fetcher.repository.InstrumentRepository;
import com.example.money.plant.investment.fetcher.utility.InstrumentConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class InstrumentFetcherService {

	private final InstrumentRepository instrumentRepository;

	private final InstrumentConverter instrumentConverter;

	private final String instrumentObjectName = "USER_INSTRUMENT_LIST";

	public InstrumentResponseDto getInstrumentList() {
		Optional<InstrumentEntity> entity = Optional
				.ofNullable(instrumentRepository.findInstrumentEntityByName(instrumentObjectName));

		if (entity.isEmpty()) {
			log.warn("First Time Entry: Creating Instrument Object");
			createEntity();
			entity = Optional.ofNullable(instrumentRepository.findInstrumentEntityByName(instrumentObjectName));
		}
		log.info("Fetching Present Instrument List");
		return instrumentConverter.convertInstrumentEntityToModel(entity);
	}

	public void createEntity() {
		InstrumentEntity entity = new InstrumentEntity();
		entity.setInstrumentList(new ArrayList<>());
		instrumentRepository.save(entity);
		log.warn("First Time Entry: Created Instrument Object");
	}

	public void addInstrument(InstrumentAddDto instruments) {
		log.info("Add Instrument Request");
		List<InstrumentModel> newInstruments = instrumentConverter.convertMultipleValuesToInstrumentList(instruments);
		newInstruments.forEach(instrumentModel -> instrumentModel.setName(instrumentModel.getName().toUpperCase()));
		log.info("Checking if Instrument Object exists or not");

		Optional<InstrumentEntity> entity = Optional
				.ofNullable(instrumentRepository.findInstrumentEntityByName(instrumentObjectName));

		if (entity.isEmpty()) {
			log.warn("First Time Entry: Creating Instrument Object");
			createEntity();
			entity = Optional.ofNullable(instrumentRepository.findInstrumentEntityByName(instrumentObjectName));
		}

		if (entity.isPresent()) {
			List<InstrumentModel> alreadyPresentInstruments = entity.get().getInstrumentList();

			try {
				newInstruments.removeAll(alreadyPresentInstruments);
				Set<InstrumentModel> temp = new HashSet<>(newInstruments);
				newInstruments.clear();
				newInstruments.addAll(temp);
				log.info("Adding the following elements to Instrument List: " + newInstruments);
				alreadyPresentInstruments.addAll(newInstruments);
				entity.get().setInstrumentList(alreadyPresentInstruments);
				instrumentRepository.save(entity.get());

			}
			catch (Exception e) {
				log.error("Unable to add Instrument due to : ", e);
				throw new RuntimeException("Instrument Addition Failure");
			}
		}
		else throw new RuntimeException("Trade Parameters Creation Error: ");
	}

	public void removeInstrument(String stockName) {
		String search = stockName.toUpperCase();
		InstrumentModel newModel = new InstrumentModel();
		newModel.setName(search);

		log.info("Removing Instrument: " + search);

		Optional<InstrumentEntity> entity = Optional
				.ofNullable(instrumentRepository.findInstrumentEntityByName(instrumentObjectName));

		if (entity.isEmpty()) {
			throw new RuntimeException("Trying to Delete on Application Startup");
		}

		List<InstrumentModel> alreadyPresentInstruments = entity.get().getInstrumentList();
		log.info(" Before Removal the instrument list is : " + alreadyPresentInstruments);
		alreadyPresentInstruments.remove(newModel);
		log.info(" After Removal the instrument list is : " + alreadyPresentInstruments);

		entity.get().setInstrumentList(alreadyPresentInstruments);
		instrumentRepository.save(entity.get());

	}

}
