package com.example.money.plant.investment.fetcher.model;

import com.example.money.plant.investment.fetcher.entity.TradeParameters;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParametersResponseDto {

	private List<TradeParameters> tradeParametersList;

}
