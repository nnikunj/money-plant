package com.example.money.plant.investment.fetcher.entity;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TradeParameters {

    @NotNull(message = "Parameter Name can't be Null")
    public String parameterName;
}
