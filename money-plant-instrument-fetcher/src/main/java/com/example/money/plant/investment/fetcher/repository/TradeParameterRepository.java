package com.example.money.plant.investment.fetcher.repository;

import com.example.money.plant.investment.fetcher.entity.ParameterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeParameterRepository extends MongoRepository<ParameterEntity, String> {

	ParameterEntity findParameterEntityByName(String name);

}
