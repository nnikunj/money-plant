package com.example.money.plant.investment.fetcher.repository;

import com.example.money.plant.investment.fetcher.entity.InstrumentEntity;
import com.example.money.plant.investment.fetcher.response.InstrumentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InstrumentRepository extends JpaRepository<InstrumentEntity, Long> {

    List<InstrumentEntity> findByName(String name);

    @Query("SELECT new com.example.money.plant.investment.fetcher.response.InstrumentResponse(ie.instrument_token, ie.trading_symbol) FROM instrument_list ie WHERE ie.trading_symbol LIKE %:symbol%")
    List<InstrumentResponse> findInstrumentDataBySymbol(String symbol);

}
