package com.example.money.plant.investment.fetcher.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class InstrumentModel {

	@NotNull(message = "Instrument/Stock Name Should Be Present")
	private String name;

	// @Override
	// public boolean equals(Object o) {
	// if (this == o) return true;
	// if (o == null || getClass() != o.getClass()) return false;
	// InstrumentModel that = (InstrumentModel) o;
	// return Objects.equals(name, that.name);
	// }
	//
	// @Override
	// public int hashCode() {
	// return Objects.hash(name);
	// }

}
