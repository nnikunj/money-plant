package com.example.money.plant.investment.fetcher.service;

import com.example.money.plant.investment.fetcher.entity.ParameterEntity;
import com.example.money.plant.investment.fetcher.entity.TradeParameters;
import com.example.money.plant.investment.fetcher.model.ParametersAddDto;
import com.example.money.plant.investment.fetcher.model.ParametersResponseDto;
import com.example.money.plant.investment.fetcher.repository.TradeParameterRepository;
import com.example.money.plant.investment.fetcher.utility.ParameterConverter;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class TradeParameterService {

    private final TradeParameterRepository tradeParameterRepository;
    private final ParameterConverter parameterConverter;

    private final String tradeParamListObject = "TRADE_PARAMETER_LIST";

    public void addTradeParameter(ParametersAddDto dto){
        log.info("Received trade parameter addition request.");
        log.info("Checking if the parameter already exists.");

        Optional<ParameterEntity> searchEntity = Optional.ofNullable(tradeParameterRepository.findParameterEntityByName(tradeParamListObject));
        if(searchEntity.isEmpty()){
            log.warn("First time parameter addition");
            createParameterEntity();
            searchEntity = Optional.ofNullable(tradeParameterRepository.findParameterEntityByName(tradeParamListObject));
        }

        List<TradeParameters> newParamList = parameterConverter.convertMultipleObjectsToParam(dto);

        if(searchEntity.isPresent()){

            List<TradeParameters> tradeParametersList = searchEntity.get().getTradeParametersList();

            try{
                newParamList.removeAll(tradeParametersList);
                Set<TradeParameters> temp = new HashSet<>(newParamList);
                newParamList.clear();
                newParamList.addAll(temp);
                log.info("Adding the following Parameters for Calculation: [ " + newParamList + " ]");
                tradeParametersList.addAll(newParamList);
                searchEntity.get().setTradeParametersList(tradeParametersList);
                tradeParameterRepository.save(searchEntity.get());
            }
            catch (Exception e) {
                log.error("Unable to add Instrument due to : ", e);
                throw new RuntimeException("Trade Parameter Addition Failure");
            }
        }
        else throw new RuntimeException("Trade Parameters Creation Error: ");
    }

    private void createParameterEntity() {
        ParameterEntity entity = new ParameterEntity();
        entity.setTradeParametersList(new ArrayList<>());
        tradeParameterRepository.save(entity);
        log.warn("Parameter List Created");
    }

    public void removeTradeParam(String tradeName){
        TradeParameters param = new TradeParameters();
        param.setParameterName(tradeName.toUpperCase());

        log.info("Removing Instrument: " + tradeName.toUpperCase());
        Optional<ParameterEntity> searchEntity = Optional.ofNullable(tradeParameterRepository.findParameterEntityByName(tradeParamListObject));
        if(searchEntity.isEmpty()){
            throw new RuntimeException("Trying to Delete on Application Startup");
        }

        List<TradeParameters> parametersList = searchEntity.get().getTradeParametersList();
        log.info(" Before Removal the instrument list is : " + parametersList);
        parametersList.remove(param);
        log.info(" After Removal the instrument list is : " + parametersList);
        searchEntity.get().setTradeParametersList(parametersList);
        tradeParameterRepository.save(searchEntity.get());
        log.info("Successfully Deleted: " + tradeName.toUpperCase());
    }

    public ParametersResponseDto getTradeParamList(){

        Optional<ParameterEntity> entity = Optional.ofNullable(tradeParameterRepository.findParameterEntityByName(tradeParamListObject));
        if(entity.isEmpty()){
            log.warn("First time parameter addition");
            createParameterEntity();
            entity = Optional.ofNullable(tradeParameterRepository.findParameterEntityByName(tradeParamListObject));
        }
        log.info("Fetching Present Instrument List");
        return parameterConverter.convertToParamResponseModel(entity);
    }



}
