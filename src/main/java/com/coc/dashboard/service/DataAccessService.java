package com.coc.dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
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
		log.info("Inside DataAccessService.distinctLobStateMonths() method");
		CompletableFuture<List<String>> lobFuture = pmpm.distinctLob();
		CompletableFuture<List<String>> stateFuture = pmpm.distinctState();
		CompletableFuture<List<String>> monthFuture = pmpm.distinctMonths();
		Map<String, List<String>> distinctData = dataModificationService.distinctLobStateMonths(lobFuture, stateFuture,
				monthFuture);
		log.info("Exiting DataAccessService.distinctLobStateMonths() method");
		return distinctData;
	}

	@Async
	public CompletableFuture<DataPair<List<ResultData>, String, Object>> kpiMetrics(String lob, String state)
			throws MyCustomException {
		log.info("Inside DataAccessService.kpiMetrics() method");
		CompletableFuture<List<PMPMDTO>> pmpmListFuture = pmpm.kpiMetrics(lob, state);
		CompletableFuture<List<MemberViewDTO>> memberViewListFuture = memberView.kpiMetrics(lob, state);
		DataPair<List<ResultData>, String, Object> finalData = dataModificationService.summaryPage(pmpmListFuture,
				memberViewListFuture);
		log.info("Exiting DataAccessService.kpiMetrics() method");
		return CompletableFuture.completedFuture(finalData);
	}

	@Async
	public CompletableFuture<List<TargetPMPM>> findAll() {
		List<TargetPMPM> data = targetPMPMRepository.findAll();
		return CompletableFuture.completedFuture(data);
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
		}
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
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
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception occured while reading the data from db: "+e.getMessage());
			throw new MyCustomException("Exception occured while reading the data from db");
		}
	}
}
