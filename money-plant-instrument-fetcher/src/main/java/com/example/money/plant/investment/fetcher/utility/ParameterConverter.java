package com.example.money.plant.investment.fetcher.utility;

import com.example.money.plant.investment.fetcher.entity.ParameterEntity;
import com.example.money.plant.investment.fetcher.entity.TradeParameters;
import com.example.money.plant.investment.fetcher.model.ParametersAddDto;
import com.example.money.plant.investment.fetcher.model.ParametersResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ParameterConverter {

    public TradeParameters convertToTrade(String trade){
        TradeParameters param = new TradeParameters();
        param.setParameterName(trade.toUpperCase());
        return param;
    }


    public List<TradeParameters> convertMultipleObjectsToParam(ParametersAddDto dto) {
        List<TradeParameters> tradeParametersList = new ArrayList<>();
        dto.getTradeParameterList().forEach( trade -> tradeParametersList.add(convertToTrade(trade)));
        return  tradeParametersList;
    }

    public ParametersResponseDto convertToParamResponseModel(Optional<ParameterEntity> entity) {
        if(entity.isPresent()){
            return new ParametersResponseDto(entity.get().getTradeParametersList());
        }
        return new ParametersResponseDto();
    }
}
