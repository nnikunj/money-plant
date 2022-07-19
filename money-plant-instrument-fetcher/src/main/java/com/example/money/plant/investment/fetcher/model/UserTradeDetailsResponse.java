package com.example.money.plant.investment.fetcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTradeDetailsResponse {

	private InstrumentResponseDto instrumentDetails;

	private ParametersResponseDto tradeParams;

}
