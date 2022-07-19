package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.model.InstrumentResponseDto;
import com.example.money.plant.investment.fetcher.model.ParametersResponseDto;
import com.example.money.plant.investment.fetcher.model.UserTradeDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserTradeDetailService {

	private final InstrumentFetcherService instrumentFetcherService;

	private final TradeParameterService tradeParameterService;

	public UserTradeDetailsResponse getUserTradeDetails() {
		log.info("User Trade Initial Details Request: ");

		UserTradeDetailsResponse result = new UserTradeDetailsResponse();

		InstrumentResponseDto instrumentDetails = instrumentFetcherService.getInstrumentList();

		ParametersResponseDto parametersDetails = tradeParameterService.getTradeParamList();

		result.setInstrumentDetails(instrumentDetails);
		result.setTradeParams(parametersDetails);

		return result;
	}

}
