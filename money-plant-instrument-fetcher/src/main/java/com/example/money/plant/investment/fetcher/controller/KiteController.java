package com.example.money.plant.investment.fetcher.controller;

import com.example.money.plant.investment.fetcher.response.InstrumentResponse;
import com.example.money.plant.investment.fetcher.response.KiteConnectResponse;
import com.example.money.plant.investment.fetcher.service.ApplicationStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/kite")
public class KiteController {

    @Autowired
    private ApplicationStarter applicationStarter;

    @GetMapping("credential")
    private ResponseEntity<KiteConnectResponse> getKiteCredentials() {
        KiteConnectResponse kiteConnectResponse = applicationStarter.getKiteCredentialsFromCache();
        return ResponseEntity.ok(kiteConnectResponse);
    }

}
