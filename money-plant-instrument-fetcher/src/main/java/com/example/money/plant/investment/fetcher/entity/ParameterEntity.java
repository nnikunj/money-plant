package com.example.money.plant.investment.fetcher.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "trade_parameters")
public class ParameterEntity {

	@Id
	private String id;

	// TODO: Make this name configurable
	private String name = "TRADE_PARAMETER_LIST";

	private List<TradeParameters> tradeParametersList;

	@CreatedDate
	private Instant created;

	@LastModifiedDate
	private Instant modified;

}
