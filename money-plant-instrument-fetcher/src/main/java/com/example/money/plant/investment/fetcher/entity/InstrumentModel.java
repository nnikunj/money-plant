package com.example.money.plant.investment.fetcher.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InstrumentModel {

	@NotNull(message = "Instrument/Stock Name Should Be Present")
	private String name;

}
