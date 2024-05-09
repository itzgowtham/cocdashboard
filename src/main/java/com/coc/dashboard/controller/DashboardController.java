package com.coc.dashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coc.dashboard.dto.FinalResult;
import com.coc.dashboard.dto.MetricData;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.service.DashboardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@PostMapping("/summary")
	public ResponseEntity<Map<String, Object>> summary(@RequestBody PMPMObject pmpmObject) throws MyCustomException {
		log.info("POST request to /summary with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.summary(pmpmObject));
	}

	@GetMapping("/landingPage")
	public ResponseEntity<FinalResult> landingPage() throws MyCustomException {
		log.info("Get request to /landingPage");
		return ResponseEntity.ok(dashboardService.landingPage());
	}

	@GetMapping("/filterOptions")
	public ResponseEntity<Map<String, List<String>>> distinctLobStateMonths() throws MyCustomException {
		log.info("Get request to /filterOptions");
		return ResponseEntity.ok(dashboardService.distinctLobStateMonths());
	}

	@PostMapping("/careCategory")
	public ResponseEntity<Map<String, Map<String, MetricData>>> careCategory(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /careCategory with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.careCategory(pmpmObject));
	}

	@PostMapping("/serviceRegion")
	public ResponseEntity<Map<String, Map<String, MetricData>>> serviceRegion(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /serviceRegion with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.serviceRegion(pmpmObject));
	}

	@PostMapping("/providerSpeciality")
	public ResponseEntity<Map<String, Map<String, MetricData>>> providerSpeciality(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /providerSpeciality with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.providerSpeciality(pmpmObject));
	}

	@PostMapping("/careProvider")
	public ResponseEntity<Map<String, Map<String, MetricData>>> careProvider(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /careProvider with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.careProvider(pmpmObject));
	}

	@PostMapping("/pcpGroup")
	public ResponseEntity<Map<String, MetricData>> pcpGroup(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /pcpGroup with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.pcpGroup(pmpmObject));
	}
	
	@PostMapping("/forecast")
	public ResponseEntity<Map<String, List<Object>>> forcast(@RequestBody PMPMObject pmpmObject)
			throws MyCustomException {
		log.info("POST request to /forecast with PMPMObject: {}", pmpmObject);
		return ResponseEntity.ok(dashboardService.forecast(pmpmObject));
	}
}
