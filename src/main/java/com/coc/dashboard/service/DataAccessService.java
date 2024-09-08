package com.coc.dashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopMemberProvider;
import com.coc.dashboard.dto.TopMemberSpeciality;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.repository.TargetPMPMRepository;

import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.internal.apache.tika.utils.StringUtils;

@Slf4j
@Service
public class DataAccessService {

	@Autowired
	private DataModificationService dataModificationService;

	@Autowired
	private PMPMDataAccessService pmpm;

	@Autowired
	private MemberViewDataAccessService memberView;

	@Autowired
	private TargetPMPMRepository targetPMPMRepository;

	public Map<String, List<String>> distinctLobStateMonths() throws MyCustomException {
		try {
			log.info("Inside DataAccessService.distinctLobStateMonths() method");
			CompletableFuture<List<String>> lobFuture = pmpm.distinctLob();
			CompletableFuture<List<String>> stateFuture = pmpm.distinctState();
			CompletableFuture<List<String>> monthFuture = pmpm.distinctMonths();
			List<String> lobs = lobFuture.get();
			List<String> states = stateFuture.get();
			List<String> months = monthFuture.get();
			Map<String, List<String>> distinctData = dataModificationService.distinctLobStateMonths(lobs, states,
					months);
			log.info("Exiting DataAccessService.distinctLobStateMonths() method");
			return distinctData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<ResultData>, List<TargetPMPM>, String> kpiMetrics(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.kpiMetrics() method");
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.kpiMetrics(lob, state);
			CompletableFuture<List<MemberViewDTO>> memberViewListFuture = memberView.kpiMetrics(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<MemberViewDTO> memberViewList = memberViewListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			DataPair<List<ResultData>, List<TargetPMPM>, String> finalData = dataModificationService
					.summaryPage(pmpmList, memberViewList, targetPercentage);
			log.info("Exiting DataAccessService.kpiMetrics() method");
			return finalData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	@Async
	public CompletableFuture<List<TargetPMPM>> findAll() {
		List<TargetPMPM> data = targetPMPMRepository.findAll();
		return CompletableFuture.completedFuture(data);
	}

	public DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> careCategory(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.careCategory() method");
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.findCareCategory(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			log.info("Exiting DataAccessService.careCategory() method");
			return new DataPair<>(pmpmList, targetPercentage, null);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<PMPMDTO>, List<MemberViewDTO>, List<TargetPMPM>> serviceRegion(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.serviceRegion() method");
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.serviceRegion(lob, state);
			CompletableFuture<List<MemberViewDTO>> memberViewListFuture = memberView.serviceRegion(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<MemberViewDTO> memberViewList = memberViewListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			log.info("Exiting DataAccessService.serviceRegion() method");
			return new DataPair<>(pmpmList, memberViewList, targetPercentage);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> providerSpeciality(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.providerSpeciality() method");
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.findProviderSpeciality(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			log.info("Exiting DataAccessService.providerSpeciality() method");
			return new DataPair<>(pmpmList, targetPercentage, null);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> careProvider(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.careProvider() method");
			state = StringUtils.isEmpty(state) ? pmpm.distinctState().get().get(1) : state;
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.findCareProvider(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			log.info("Exiting DataAccessService.careProvider() method");
			return new DataPair<>(pmpmList, targetPercentage, null);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> pcpGroup(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.pcpGroup() method");
			CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.findPcpGroup(lob, state);
			CompletableFuture<List<TargetPMPM>> targetPercentageFuture = findAll();
			List<PMPMDTO> pmpmList = pmpmListFuture.get();
			List<TargetPMPM> targetPercentage = targetPercentageFuture.get();
			log.info("Exiting DataAccessService.pcpGroup() method");
			return new DataPair<>(pmpmList, targetPercentage, null);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> forecast(String lob, String state)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.forecast() method");
			CompletableFuture<List<Forecast_PMPM>> pmpmFuture = pmpm.findForecast(lob, state);
			CompletableFuture<List<Forecast_ActiveMembership>> memberFuture = memberView.findForecast(lob, state);
			List<Forecast_PMPM> pmpm = pmpmFuture.get();
			List<Forecast_ActiveMembership> member = memberFuture.get();
			log.info("Exiting DataAccessService.forecast() method");
			return new DataPair<>(pmpm, member, null);
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public Map<String, Object> careCategoryDetails(String lob, String state, String startMonth, String endMonth,
			String category) throws MyCustomException {
		try {
			log.info("Inside DataAccessService.careCategoryDetails() method");
			CompletableFuture<List<TopProvider>> topProvidersBySpeciality = pmpm.findCareCategoryTopProviders(lob,
					state, startMonth, endMonth, category);
			CompletableFuture<List<TopMember>> topMembersBySpeciality = memberView.findCareCategoryTopMembers(lob,
					state, startMonth, endMonth, category);
			CompletableFuture<Long> totalProviders = pmpm.findCareCategoryProvidersCount(lob, state, startMonth,
					endMonth, category);
			CompletableFuture<Long> totalMembers = memberView.findCareCategoryMembersCount(lob, state, startMonth,
					endMonth, category);
			CompletableFuture<List<String>> distinctCategory = pmpm.distinctCategory();
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("topMembersPerCareCategory", topMembersBySpeciality.get());
			mapData.put("topProvidersPerCareCategory", topProvidersBySpeciality.get());
			mapData.put("membersCount", totalMembers.get());
			mapData.put("providersCount", totalProviders.get());
			mapData.put("distinctCategory", distinctCategory.get());
			log.info("Exiting DataAccessService.careCategoryDetails() method");
			return mapData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public Map<String, Object> serviceRegionDetails(String lob, String state, String startMonth, String endMonth)
			throws MyCustomException {
		try {
			log.info("Inside DataAccessService.serviceRegionDetails() method");
			CompletableFuture<List<PMPMDTO>> pmpmList = pmpm.serviceRegionDetails(lob, state, startMonth, endMonth);
			CompletableFuture<List<TopProvider>> topProviders = pmpm.findTopProviders(lob, state, startMonth, endMonth);
			CompletableFuture<List<TopMember>> topMembers = memberView.findServiceRegionTopMembers(lob, state,
					startMonth, endMonth);
			CompletableFuture<List<MemberViewDTO>> memberViewList = memberView.serviceRegion(lob, state);
			Map<String, Object> mapData = dataModificationService.serviceRegionDetails(pmpmList.get(),
					topProviders.get(), memberViewList.get(), topMembers.get(), startMonth, endMonth);
			log.info("Exiting DataAccessService.serviceRegionDetails() method");
			return mapData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public Map<String, Object> providerSpecialityDetails(String lob, String state, String startMonth, String endMonth,
			String speciality) throws MyCustomException {
		try {
			log.info("Inside DataAccessService.providerSpecialityDetails() method");
			CompletableFuture<List<TopSpeciality>> topSpeciality = pmpm.findTopSpecialities(lob, state, startMonth,
					endMonth);
			CompletableFuture<List<TopProvider>> topProvidersBySpeciality = pmpm.findTopProvidersBySpeciality(lob,
					state, startMonth, endMonth, speciality);
			CompletableFuture<List<TopMemberSpeciality>> topMembers = memberView.findTopProviderMembers(lob, state,
					startMonth, endMonth);
			CompletableFuture<List<TopMember>> topMembersBySpeciality = memberView.findTopMembersBySpeciality(lob,
					state, startMonth, endMonth, speciality);
			CompletableFuture<List<String>> distinctSpeciality = pmpm.distinctSpeciality();
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("topSpecialitiesByCost", topSpeciality.get());
			mapData.put("topProvidersPerSpeciality", topProvidersBySpeciality.get());
			mapData.put("membersPerSpeciality", topMembers.get());
			mapData.put("topMembersPerSpeciality", topMembersBySpeciality.get());
			mapData.put("distinctSpeciality", distinctSpeciality.get());
			log.info("Exiting DataAccessService.providerSpecialityDetails() method");
			return mapData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public Map<String, Object> careProviderDetails(String lob, String state, String startMonth, String endMonth,
			String speciality, String providerName) throws MyCustomException {
		try {
			log.info("Inside DataAccessService.careProviderDetails() method");
			CompletableFuture<List<TopProvider>> topProviders = pmpm.findTopProviders(lob, state, startMonth, endMonth);
			CompletableFuture<List<TopProvider>> topProvidersBySpeciality = pmpm.findTopProvidersBySpeciality(lob,
					state, startMonth, endMonth, speciality);
			CompletableFuture<List<TopMemberProvider>> topMembersByProvider = memberView.findTopMembersByProvider(lob,
					state, startMonth, endMonth);
			CompletableFuture<List<TopMember>> topMembersBySpeciality = memberView.findTopMembersByProvider(lob,
					state, startMonth, endMonth, providerName);
			CompletableFuture<List<String>> distinctSpeciality = pmpm.distinctSpeciality();
			CompletableFuture<List<String>> distinctProvider = pmpm.distinctProvider(lob, state, endMonth);
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("topMembersPerProvider", topMembersByProvider.get());
			mapData.put("top10ProvidersByCost", topProviders.get());
			mapData.put("top10MembersByCostForEachSpeciality", topMembersBySpeciality.get());
			mapData.put("top10ProvidersByCostForEachSpeciality", topProvidersBySpeciality.get());
			mapData.put("distinctSpeciality", distinctSpeciality.get());
			mapData.put("distinctProvider", distinctProvider.get());
			log.info("Exiting DataAccessService.careProviderDetails() method");
			return mapData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}

	public Map<String, Object> pcpGroupDetails(String lob, String state, String startMonth, String endMonth,
			String speciality, String providerName) throws MyCustomException {
		try {
			log.info("Inside DataAccessService.pcpGroupDetails() method");
			CompletableFuture<List<TopProvider>> topSpeciality = pmpm.findPcpTopSpeciality(lob, state, startMonth,
					endMonth);
			CompletableFuture<List<TopProvider>> topProvidersBySpeciality = pmpm.findPcpTopProvidersBySpeciality(lob,
					state, startMonth, endMonth, speciality);
			CompletableFuture<List<TopMemberProvider>> topMembers = memberView.findPcpGroupTopMembers(lob, state,
					startMonth, endMonth);
			CompletableFuture<List<TopMember>> topMembersBySpeciality = memberView
					.findPcpGroupTopMembersBySpeciality(lob, state, startMonth, endMonth, providerName);
			CompletableFuture<List<String>> distinctSpeciality = pmpm.distinctSpecialityPcp();
			CompletableFuture<List<String>> distinctProvider = pmpm.distinctProviderPcp(lob, state, endMonth);
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("topPcpByCost", topSpeciality.get());
			mapData.put("topPcpByCostForEachSpeciality", topProvidersBySpeciality.get());
			mapData.put("membersPerPcpGroup", topMembers.get());
			mapData.put("topMembersByCostForEachPcp", topMembersBySpeciality.get());
			mapData.put("distinctSpeciality", distinctSpeciality.get());
			mapData.put("distinctProvider", distinctProvider.get());
			log.info("Exiting DataAccessService.pcpGroupDetails() method");
			return mapData;
		} catch (Exception e) {
			log.error("Exception occurred while reading the data from db: " + e.getMessage());
			throw new MyCustomException("Exception occurred while reading the data from db");
		}
	}
}
