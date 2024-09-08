package com.coc.dashboard.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopMemberProvider;
import com.coc.dashboard.dto.TopMemberSpeciality;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.repository.CareCategoryDetailsRepository;
import com.coc.dashboard.repository.ForecastMemberRepository;
import com.coc.dashboard.repository.MemberRepository;
import com.coc.dashboard.repository.PcpGroupDetailsRepository;
import com.coc.dashboard.repository.ProviderSpecialityDetailsRepository;
import com.coc.dashboard.repository.ServiceAreaRegionDetailsRepository;

@Service
public class MemberViewDataAccessService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ForecastMemberRepository forecastRepository;

	@Autowired
	private CareCategoryDetailsRepository careCategoryDetailsRepository;

	@Autowired
	private ServiceAreaRegionDetailsRepository serviceAreaRegionDetailsRepository;

	@Autowired
	private ProviderSpecialityDetailsRepository providerSpecialityDetailsRepository;

	@Autowired
	private PcpGroupDetailsRepository pcpGroupDetailsRepository;

	@Async
	public CompletableFuture<List<MemberViewDTO>> kpiMetrics(String lob, String state) {
		return CompletableFuture.completedFuture(memberRepository.findSummaryRecords(lob, state));
	}

	@Async
	public CompletableFuture<List<MemberViewDTO>> serviceRegion(String lob, String state) {
		return CompletableFuture.completedFuture(memberRepository.findServiceRegion(lob, state));
	}

	@Async
	public CompletableFuture<List<Forecast_ActiveMembership>> findForecast(String lob, String state) {
		return CompletableFuture.completedFuture(forecastRepository.findByLobAndStatecode(lob, state));
	}

	@Async
	public CompletableFuture<List<TopMember>> findCareCategoryTopMembers(String lob, String state, String startMonth,
			String endMonth, String category) {
		return CompletableFuture.completedFuture(
				careCategoryDetailsRepository.findTopMembersByCategory(lob, state, startMonth, endMonth, category));
	}

	@Async
	public CompletableFuture<List<TopMember>> findServiceRegionTopMembers(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture
				.completedFuture(serviceAreaRegionDetailsRepository.findTopMembers(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopMember>> findTopMembers(String lob, String state, String startMonth,
			String endMonth) {
		return CompletableFuture
				.completedFuture(providerSpecialityDetailsRepository.findTopMembers(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopMemberSpeciality>> findTopProviderMembers(String lob, String state,
			String startMonth, String endMonth) {
		return CompletableFuture.completedFuture(
				providerSpecialityDetailsRepository.findTopProviderMembers(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopMember>> findTopMembersBySpeciality(String lob, String state, String startMonth,
			String endMonth, String speciality) {
		return CompletableFuture.completedFuture(providerSpecialityDetailsRepository.findTopMembersBySpeciality(lob,
				state, startMonth, endMonth, speciality));
	}

	@Async
	public CompletableFuture<List<TopMemberProvider>> findTopMembersByProvider(String lob, String state,
			String startMonth, String endMonth) {
		return CompletableFuture.completedFuture(
				providerSpecialityDetailsRepository.findTopMembersByProvider(lob, state, startMonth, endMonth));
	}
	
	@Async
	public CompletableFuture<List<TopMember>> findTopMembersByProvider(String lob, String state,
			String startMonth, String endMonth, String providerName) {
		return CompletableFuture.completedFuture(
				providerSpecialityDetailsRepository.findTopMembersByProvider(lob, state, startMonth, endMonth, providerName));
	}

	@Async
	public CompletableFuture<List<TopMemberProvider>> findPcpGroupTopMembers(String lob, String state,
			String startMonth, String endMonth) {
		return CompletableFuture
				.completedFuture(pcpGroupDetailsRepository.findTopMembers(lob, state, startMonth, endMonth));
	}

	@Async
	public CompletableFuture<List<TopMember>> findPcpGroupTopMembersBySpeciality(String lob, String state,
			String startMonth, String endMonth, String providerName) {
		return CompletableFuture.completedFuture(
				pcpGroupDetailsRepository.findTopMembersBySpeciality(lob, state, startMonth, endMonth, providerName));
	}

	@Async
	public CompletableFuture<Long> findCareCategoryMembersCount(String lob, String state, String startMonth,
			String endMonth, String category) {
		return CompletableFuture.completedFuture(
				careCategoryDetailsRepository.findMembersCount(lob, state, startMonth, endMonth, category));
	}

	@Async
	public CompletableFuture<Long> findMembersCount(String lob, String state, String startMonth, String endMonth,
			String speciality) {
		return CompletableFuture.completedFuture(
				providerSpecialityDetailsRepository.findMembersCount(lob, state, startMonth, endMonth, speciality));
	}
}
