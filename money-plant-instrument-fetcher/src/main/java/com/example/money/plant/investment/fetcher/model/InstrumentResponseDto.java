package com.example.money.plant.investment.fetcher.model;

import com.example.money.plant.investment.fetcher.entity.InstrumentModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentResponseDto {

	private List<InstrumentModel> instrumentModelList;

}
