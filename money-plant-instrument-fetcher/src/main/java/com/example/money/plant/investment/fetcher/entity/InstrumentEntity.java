package com.example.money.plant.investment.fetcher.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "instruments")
public class InstrumentEntity {

	@Id
	private String id;

	// TODO: Make this name configurable
	private String name = "USER_INSTRUMENT_LIST";

	private List<InstrumentModel> instrumentList;

	@CreatedDate
	protected Instant created;

	@LastModifiedDate
	protected Instant modified;

}
