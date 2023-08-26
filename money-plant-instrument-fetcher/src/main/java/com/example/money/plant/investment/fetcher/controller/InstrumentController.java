package com.example.money.plant.investment.fetcher.controller;

import com.example.money.plant.investment.fetcher.repository.InstrumentRepository;
import com.example.money.plant.investment.fetcher.response.InstrumentResponse;
import com.example.money.plant.investment.fetcher.service.InstrumentDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/instruments")
public class InstrumentController {

    @Autowired
    private InstrumentDump instrumentDump;

    @GetMapping("{symbol}")
    private ResponseEntity<List<InstrumentResponse>> getInstrumentDataBySymbol(@PathVariable("symbol") String symbol) {
        List<InstrumentResponse> instrumentResponse = instrumentDump.getInstrumentDataBySymbol(symbol);
        return ResponseEntity.ok(instrumentResponse);
    }
}
