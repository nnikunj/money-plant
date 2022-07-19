package com.example.money.plant.investment.fetcher.utility;

import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import com.example.money.plant.investment.fetcher.entity.InstrumentModel;
import com.example.money.plant.investment.fetcher.model.InstrumentAddDto;
import com.example.money.plant.investment.fetcher.model.InstrumentResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InstrumentConverter {

	public InstrumentModel convertToInstrument(String name) {
		InstrumentModel result = new InstrumentModel();
		result.setName(name);
		return result;
	}

	public List<InstrumentModel> convertMultipleValuesToInstrumentList(InstrumentAddDto instruments) {
		List<InstrumentModel> result = new ArrayList<>();
		instruments.getInstrumentModelList().forEach(entity -> result.add(convertToInstrument(entity)));
		return result;
	}

	public InstrumentResponseDto convertInstrumentEntityToModel(Optional<InstrumentEntity> entity) {
		if (entity.isPresent()) {
			List<InstrumentModel> list = entity.get().getInstrumentList();
			return new InstrumentResponseDto(list);
		}
		return new InstrumentResponseDto();
	}

}
