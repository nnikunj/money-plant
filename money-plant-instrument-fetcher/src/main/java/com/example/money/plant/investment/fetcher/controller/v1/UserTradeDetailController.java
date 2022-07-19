package com.example.money.plant.investment.fetcher.controller.v1;


import com.example.money.plant.investment.fetcher.model.UserTradeDetailsResponse;
import com.example.money.plant.investment.fetcher.service.UserTradeDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/all")
@RequiredArgsConstructor
@Log4j2
public class UserTradeDetailController {

    private final UserTradeDetailService userTradeDetailService;

    @GetMapping
    public ResponseEntity<UserTradeDetailsResponse> getUserTradeDetails(){
        log.info("User Trade Details Request Call;");
        UserTradeDetailsResponse userTradeDetails = userTradeDetailService.getUserTradeDetails();
        return ResponseEntity.ok().body(userTradeDetails);
    }
}
