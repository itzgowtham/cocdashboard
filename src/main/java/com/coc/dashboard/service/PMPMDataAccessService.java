package com.coc.dashboard.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.repository.ForecastPMPMRepository;
import com.coc.dashboard.repository.PMPMCategoryRepository;
import com.coc.dashboard.repository.PMPMPcpRepository;
import com.coc.dashboard.repository.PMPMRepository;

@Service
@AllArgsConstructor
public class PMPMDataAccessService {

	private PMPMRepository pmpmRepository;
	private PMPMCategoryRepository pmpmCategoryRepository;
	private PMPMPcpRepository pmpmPcpRepository;
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
		return CompletableFuture.completedFuture(pmpmRepository.findSummaryRecords(lob, state));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> serviceRegion(String lob, String state) {
		return CompletableFuture.completedFuture(pmpmRepository.findServiceRegion(lob, state));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findCareCategory(String lob, String state) {
		return CompletableFuture.completedFuture(pmpmCategoryRepository.findCareCategory(lob, state));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findProviderSpeciality(String lob, String state) {
		return CompletableFuture.completedFuture(pmpmRepository.findProviderSpeciality(lob, state));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findCareProvider(String lob, String state) {
		return CompletableFuture.completedFuture(pmpmRepository.findCareProviders(lob, state));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> findPcpGroup(String lob, String state) {
		return CompletableFuture.completedFuture(pmpmPcpRepository.findPcpGroup(lob, state));
	}

	@Async
	public CompletableFuture<List<Forecast_PMPM>> findForecast(String lob, String state) {
		List<Forecast_PMPM> objs = forecastRepository.findByLobAndStatecode(lob, state);
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<TopSpeciality>> findTopSpecialities(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture.completedFuture(pmpmRepository.findTopSpecialities(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopProvider>> findTopProviders(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture.completedFuture(pmpmRepository.findTopProviders(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopProvider>> findTopProvidersBySpeciality(String lob, String state,
			String startMonth, String endMonth, String speciality) {
		return CompletableFuture.completedFuture(
				pmpmRepository.findTopProvidersBySpeciality(lob, state, startMonth, endMonth, speciality));
	}

	@Async
	public CompletableFuture<List<TopProvider>> findCareCategoryTopProviders(String lob, String state,
			String startMonth, String endMonth, String category) {
		return CompletableFuture.completedFuture(
				pmpmCategoryRepository.findTopProvidersByCategory(lob, state, startMonth, endMonth, category));
	}

	@Async
	public CompletableFuture<List<TopProvider>> findPcpTopSpeciality(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture.completedFuture(pmpmPcpRepository.findTopProviders(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopProvider>> findPcpTopProvidersBySpeciality(String lob, String state,
			String startMonth, String endMonth, String speciality) {
		return CompletableFuture.completedFuture(
				pmpmPcpRepository.findTopProvidersBySpeciality(lob, state, startMonth, endMonth, speciality));
	}

	@Async
	public CompletableFuture<List<String>> distinctCategory() {
		return CompletableFuture.completedFuture(pmpmCategoryRepository.findDistinctCategories());
	}

	@Async
	public CompletableFuture<List<String>> distinctSpeciality() {
		return CompletableFuture.completedFuture(pmpmRepository.findDistinctSpecialities());
	}
	
	@Async
	public CompletableFuture<List<String>> distinctProvider(String lob, String state, String endMonth) {
		return CompletableFuture.completedFuture(pmpmRepository.findDistinctProviders(lob, state, endMonth));
	}

	@Async
	public CompletableFuture<List<String>> distinctSpecialityPcp() {
		return CompletableFuture.completedFuture(pmpmPcpRepository.findDistinctSpecialities());
	}
	
	@Async
	public CompletableFuture<List<String>> distinctProviderPcp(String lob, String state, String endMonth) {
		return CompletableFuture.completedFuture(pmpmPcpRepository.findDistinctProviders(lob, state, endMonth));
	}

	@Async
	public CompletableFuture<List<PMPMDTO>> serviceRegionDetails(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture
				.completedFuture(pmpmRepository.findServiceRegionDetails(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<Long> findCareCategoryProvidersCount(String lob, String state, String startMonth,
			String endMonth, String category) {
		return CompletableFuture
				.completedFuture(pmpmCategoryRepository.findProvidersCount(lob, state, startMonth, endMonth, category));
	}
}
