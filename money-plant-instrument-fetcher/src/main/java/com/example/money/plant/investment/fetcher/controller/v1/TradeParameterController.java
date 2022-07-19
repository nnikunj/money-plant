package com.example.money.plant.investment.fetcher.controller.v1;


import com.example.money.plant.investment.fetcher.model.ParametersAddDto;
import com.example.money.plant.investment.fetcher.model.ParametersResponseDto;
import com.example.money.plant.investment.fetcher.service.TradeParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
@Log4j2
public class TradeParameterController {

    private final TradeParameterService tradeParameterService;

    @GetMapping("/param")
    public ResponseEntity<ParametersResponseDto> getParamList(){
        log.info("Trade Parameters Details Call: ");
        ParametersResponseDto result = tradeParameterService.getTradeParamList();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/trade")
    public ResponseEntity<String> addTradeParam(@RequestBody @Valid ParametersAddDto dto){
        log.info("Trade Parameters Addition Call: ");
        tradeParameterService.addTradeParameter(dto);
        return ResponseEntity.ok().body("Trade Parameter List: [ " + dto + " ] Added Successfully");
    }

    @DeleteMapping("/trade")
    public ResponseEntity<String> deleteTradeParam(@RequestBody String paramName){
        log.info("Trade Parameters Addition Call: ");
        tradeParameterService.removeTradeParam(paramName);
        return ResponseEntity.ok().body("Trade Parameter : " + paramName + " removed successfully");
    }

}
