package com.example.money.plant.investment.fetcher.controller.v1;

import com.example.money.plant.investment.fetcher.model.InstrumentAddDto;
import com.example.money.plant.investment.fetcher.model.InstrumentResponseDto;
import com.example.money.plant.investment.fetcher.service.InstrumentFetcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
public class InstrumentFetcherController {

	private final InstrumentFetcherService instrumentFetcherService;

	@GetMapping("/instrument")
	public ResponseEntity<InstrumentResponseDto> getInstrumentList() {

		InstrumentResponseDto result = instrumentFetcherService.getInstrumentList();

		return ResponseEntity.ok().body(result);

	}

	@PostMapping("/instrument")
	public ResponseEntity<?> addInstrument(@RequestBody @Valid InstrumentAddDto dto) {
		instrumentFetcherService.addInstrument(dto);

		return ResponseEntity.ok().body("Instrument List: [ " + dto + " ] Added successfully");
	}

	@DeleteMapping("/instrument")
	public ResponseEntity<?> removeInstrument(String stockName) {
		instrumentFetcherService.removeInstrument(stockName);
		return ResponseEntity.ok().body("Instrument : " + stockName + " removed successfully");
	}

}
