package com.example.money.plant.investment.fetcher.repository;

import com.example.money.plant.investment.fetcher.entity.KiteConnectEntity;
import org.springframework.data.gemfire.repository.query.annotation.Trace;
import org.springframework.data.repository.CrudRepository;

public interface KiteConnectRepository extends CrudRepository<KiteConnectEntity, String> {

	@Trace
	KiteConnectEntity findByUserId(String userId);

}
