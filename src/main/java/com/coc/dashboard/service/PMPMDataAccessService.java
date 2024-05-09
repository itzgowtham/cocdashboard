package com.coc.dashboard.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.repository.ForecastPMPMRepository;
import com.coc.dashboard.repository.PMPMCategoryRepository;
import com.coc.dashboard.repository.PMPMPcpRepository;
import com.coc.dashboard.repository.PMPMRepository;

@Service
public class PMPMDataAccessService {

	@Autowired
	private PMPMRepository pmpmRepository;
	
	@Autowired
	private PMPMCategoryRepository pmpmCategoryRepository;
	
	@Autowired
	private PMPMPcpRepository pmpmPcpRespository;
	
	@Autowired
	private ForecastPMPMRepository forecastRepository;

	@Async
	public CompletableFuture<List<String>> distinctLob() {
		return CompletableFuture.completedFuture(pmpmRepository.findDistinctLob());
	}

	@Async
	public CompletableFuture<List<String>> distinctState() {
		return CompletableFuture.completedFuture(pmpmRepository.findDistinctState());
	}

	@Async
	public CompletableFuture<List<String>> distinctMonths() {
		return CompletableFuture.completedFuture(pmpmRepository.findDistinctMonths());
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> kpiMetrics(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findSummaryByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmRepository.findSummaryByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findSummaryByState(state);
		} else {
			objs = pmpmRepository.findAllSummaryRecords();
		}
		return CompletableFuture.completedFuture(objs);
	}
	
	@Async
	public CompletableFuture<List<PMPMDTO>> serviceRegion(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findServiceRegionByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmRepository.findServiceRegionByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findServiceRegionByState(state);
		} else {
			objs = pmpmRepository.findAllByServiceRegion();
		}
		return CompletableFuture.completedFuture(objs);
	}
	
	@Async
	public CompletableFuture<List<PMPMDTO>> findCareCategory(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = pmpmCategoryRepository.findCareCategoryByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmCategoryRepository.findCareCategoryByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = pmpmCategoryRepository.findCareCategoryByState(state);
		} else {
			objs = pmpmCategoryRepository.findAllCareCategory();
		}
		return CompletableFuture.completedFuture(objs);
	}
	
	@Async
	public CompletableFuture<List<PMPMDTO>> findProviderSpeciality(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findProviderSpecialityByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmRepository.findProviderSpecialityByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = pmpmRepository.findProviderSpecialityByState(state);
		} else {
			objs = pmpmRepository.findAllProviderSpeciality();
		}
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findCareProvider(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmRepository.findCareProvider(lob, state);
		} else {
			objs = pmpmRepository.findCareProvider(state);
		}
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findPcpGroup(String lob, String state) {
		List<PMPMDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = pmpmPcpRespository.findPcpGroupByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = pmpmPcpRespository.findPcpGroupByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = pmpmPcpRespository.findPcpGroupByState(state);
		} else {
			objs = pmpmPcpRespository.findAllPcpGroup();
		}
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<Forecast_PMPM>> findForecast(String lob, String state) {
		List<Forecast_PMPM> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = forecastRepository.findByLobAndStatecode(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = forecastRepository.findByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = forecastRepository.findByStatecode(state);
		} else {
			objs = forecastRepository.findAll();
		}
		return CompletableFuture.completedFuture(objs);
	}
}
