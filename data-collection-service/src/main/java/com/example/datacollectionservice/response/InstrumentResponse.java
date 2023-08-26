package com.example.datacollectionservice.response;

import lombok.Data;

@Data
public class InstrumentResponse {
    public long instrument_token;
    public String trading_symbol;
}
