package com.coc.dashboard.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.repository.ForecastMemberRepository;
import com.coc.dashboard.repository.MemberRepository;

@Service
public class MemberViewDataAccessService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ForecastMemberRepository forecastRepository;

	@Async
	public CompletableFuture<List<MemberViewDTO>> kpiMetrics(String lob, String state) {
		List<MemberViewDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = memberRepository.findSummaryByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = memberRepository.findSummaryByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = memberRepository.findSummaryByState(state);
		} else {
			objs = memberRepository.findSummaryAllRecords();
		}
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<MemberViewDTO>> serviceRegion(String lob, String state) {
		List<MemberViewDTO> objs = null;
		if (StringUtils.isNotEmpty(lob) && StringUtils.isNotEmpty(state)) {
			objs = memberRepository.findServiceRegionByLobAndState(lob, state);
		} else if (StringUtils.isNotEmpty(lob)) {
			objs = memberRepository.findServiceRegionByLob(lob);
		} else if (StringUtils.isNotEmpty(state)) {
			objs = memberRepository.findServiceRegionByState(state);
		} else {
			objs = memberRepository.findAllByServiceRegion();
		}
		return CompletableFuture.completedFuture(objs);
	}

	@Async
	public CompletableFuture<List<Forecast_ActiveMembership>> findForecast(String lob, String state) {
		List<Forecast_ActiveMembership> objs = null;
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
