package com.example.money.plant.investment.fetcher.entity;

import com.zerodhatech.models.Instrument;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "instrument_list")
@NoArgsConstructor
public class InstrumentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public long instrument_token;
	public long exchange_token;
	public String trading_symbol;

	public String name;

	public String instrument_type;
	public String segment;
	public String exchange;

	public InstrumentEntity(long instrument_token, long exchange_token, String trading_symbol, String name, String instrument_type, String segment, String exchange) {
		this.instrument_token = instrument_token;
		this.exchange_token = exchange_token;
		this.trading_symbol = trading_symbol;
		this.name = name;
		this.instrument_type = instrument_type;
		this.segment = segment;
		this.exchange = exchange;
	}

}
