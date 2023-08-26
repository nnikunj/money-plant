package com.example.money.plant.investment.fetcher.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentResponse {
    public long instrument_token;
    public String trading_symbol;
}
