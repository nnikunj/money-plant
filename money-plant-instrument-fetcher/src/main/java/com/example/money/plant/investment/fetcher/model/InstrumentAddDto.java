package com.example.money.plant.investment.fetcher.model;

import com.example.money.plant.investment.fetcher.entity.InstrumentModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class InstrumentAddDto {

	@NotNull(message = "At least 1 stock should be present to add")
	@Size(min = 1, message = "At least 1 stock should be present to add")
	private List<String> instrumentModelList;

}
