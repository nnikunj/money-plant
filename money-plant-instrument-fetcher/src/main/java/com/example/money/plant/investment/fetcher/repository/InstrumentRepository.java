package com.example.money.plant.investment.fetcher.repository;

import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface InstrumentRepository extends MongoRepository<InstrumentEntity, String> {

	InstrumentEntity findInstrumentEntityByName(@Param("name") String name);

}
