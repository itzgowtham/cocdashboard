package com.coc.dashboard.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.FinalResult;
import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.MetricData;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.util.DateFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DashboardService {

	private DateFormat dateFormat;
	private DataAccessService dataAccessService;
	private DataModificationService dataModificationService;
	private DataProcessingService process;

	private void validatePMPMObject(PMPMObject pmpmObject) {
		pmpmObject.setLob(StringUtils.trimToNull(pmpmObject.getLob()));
		pmpmObject.setState(StringUtils.trimToNull(pmpmObject.getState()));
		pmpmObject.setStartMonth(StringUtils.isNotBlank(pmpmObject.getStartMonth())
				? dateFormat.convertStringtoIntegerDateFormat(pmpmObject.getStartMonth())
				: null);
		pmpmObject.setEndMonth(StringUtils.isNotBlank(pmpmObject.getEndMonth())
				? dateFormat.convertStringtoIntegerDateFormat(pmpmObject.getEndMonth())
				: null);
		pmpmObject.setGraphType(StringUtils.defaultIfBlank(pmpmObject.getGraphType(), DataConstants.TARGET_VS_ACTUAL));
		pmpmObject.setViewType(StringUtils.defaultIfBlank(pmpmObject.getViewType(), DataConstants.EXPENSE_PMPM));
	}

	public Map<String, Object> summary(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.summary() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();

		Map<String, Object> resultMap = new LinkedHashMap<>();
		DataPair<List<ResultData>, Map<String, Long>, String> data = fetchData(lob, state);
		List<ResultData> kpiMetrics = data.getFirst();
		Map<String, Long> targetPercentageMap = data.getSecond();
		endMonth = StringUtils.isEmpty(endMonth) ? data.getThird() : endMonth;

		FinalResult result = process.kpiMetrics(kpiMetrics, startMonth, endMonth, graphType, targetPercentageMap);
		Map<String, Map<String, Double>> mapData = process.areaChart(kpiMetrics, endMonth, graphType,
				targetPercentageMap);

		resultMap.put("kpimetrics", result);
		resultMap.put("areaChart", mapData);
		log.info("Exiting DashboardService.summary() method");
		return resultMap;
	}

	public FinalResult landingPage() throws MyCustomException {
		log.info("Inside DashboardService.landingPage() method");
		DataPair<List<ResultData>, Map<String, Long>, String> data = fetchData(null, null);
		List<ResultData> kpiMetrics = data.getFirst();
		String endMonth = "2019-12";
		String startMonth = "2019-11";
		FinalResult result = process.landingPageMetrics(kpiMetrics, startMonth, endMonth);
		log.info("Exiting DashboardService.landingPage() method");
		return result;
	}

	private DataPair<List<ResultData>, Map<String, Long>, String> fetchData(String lob, String state)
			throws MyCustomException {
		DataPair<List<ResultData>, List<TargetPMPM>, String> kpiMetricsFuture = dataAccessService.kpiMetrics(lob,
				state);
		List<ResultData> kpiMetrics = kpiMetricsFuture.getFirst();
		List<TargetPMPM> targetPMPM = kpiMetricsFuture.getSecond();
		String endMonth = kpiMetricsFuture.getThird();
		Map<String, Long> targetPercentageMap = dataModificationService.convertToTargetPercentageMap(targetPMPM);
		return new DataPair<>(kpiMetrics, targetPercentageMap, endMonth);
	}

	public Map<String, List<String>> distinctLobStateMonths() throws MyCustomException {
		log.info("Inside DashboardService.distinctLobStateMonths() method");
		Map<String, List<String>> distinctData = dataAccessService.distinctLobStateMonths();
		log.info("Exiting DashboardService.distinctLobStateMonths() method");
		return distinctData;
	}

	public Map<String, Map<String, MetricData>> careCategory(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.careCategory() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();
		DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> data = dataAccessService.careCategory(lob, state);
		Map<String, Long> targetPercentageMap = getTargetPercentageMap(data.getSecond());
		Map<String, Map<String, MetricData>> finalData = process.careCategory(data.getFirst(), targetPercentageMap,
				startMonth, endMonth, graphType);
		log.info("Exiting DashboardService.careCategory() method");
		return finalData;
	}

	public Map<String, Map<String, MetricData>> providerSpeciality(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.providerSpeciality() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();
		DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> data = dataAccessService.providerSpeciality(lob, state);
		Map<String, Long> targetPercentageMap = getTargetPercentageMap(data.getSecond());
		Map<String, Map<String, MetricData>> finalData = process.providerSpeciality(data.getFirst(),
				targetPercentageMap, startMonth, endMonth, graphType);
		log.info("Exiting DashboardService.providerSpeciality() method");
		return finalData;
	}

	public Map<String, Map<String, MetricData>> serviceRegion(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.serviceRegion() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();
		String viewType = pmpmObject.getViewType();
		DataPair<List<PMPMDTO>, List<MemberViewDTO>, List<TargetPMPM>> data = dataAccessService.serviceRegion(lob,
				state);
		List<PMPMDTO> pmpm = data.getFirst();
		List<MemberViewDTO> memberView = data.getSecond();
		Map<String, Long> targetPercentageMap = getTargetPercentageMap(data.getThird());
		Map<String, Map<String, MetricData>> finalData = process.serviceRegion(pmpm, memberView, targetPercentageMap,
				startMonth, endMonth, graphType, viewType);
		log.info("Exiting DashboardService.serviceRegion() method");
		return finalData;
	}

	public Map<String, Long> getTargetPercentageMap(List<TargetPMPM> targetPMPM) {
		return targetPMPM.stream().collect(Collectors.toMap(TargetPMPM::getMonths, TargetPMPM::getTargetPercentage));
	}

	public Map<String, Map<String, MetricData>> careProvider(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.careProvider() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();
		DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> data = dataAccessService.careProvider(lob, state);
		Map<String, Long> targetPercentageMap = getTargetPercentageMap(data.getSecond());
		Map<String, Map<String, MetricData>> finalData = process.careProvider(data.getFirst(), targetPercentageMap,
				startMonth, endMonth, graphType);
		log.info("Exiting DashboardService.careProvider() method");
		return finalData;
	}

	public Map<String, MetricData> pcpGroup(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.pcpGroup() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob();
		String state = pmpmObject.getState();
		String startMonth = pmpmObject.getStartMonth();
		String endMonth = pmpmObject.getEndMonth();
		String graphType = pmpmObject.getGraphType();
		DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> data = dataAccessService.pcpGroup(lob, state);
		Map<String, Long> targetPercentageMap = getTargetPercentageMap(data.getSecond());
		Map<String, MetricData> finalData = process.pcpGroup(data.getFirst(), targetPercentageMap, startMonth, endMonth,
				graphType);
		log.info("Exiting DashboardService.pcpGroup() method");
		return finalData;
	}

	public Map<String, List<Object>> forecast(PMPMObject pmpmObject) throws MyCustomException {
		log.info("Inside DashboardService.forecast() method");
		validatePMPMObject(pmpmObject);
		String lob = pmpmObject.getLob() == null ? "All" : pmpmObject.getLob();
		String state = pmpmObject.getState() == null ? "All" : pmpmObject.getState();
		String endMonth = pmpmObject.getEndMonth();
		DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> data = dataAccessService.forecast(lob,
				state);
		Map<String, List<Object>> finalData = process.forecast(data.getFirst(), data.getSecond(), endMonth);
		log.info("Exiting DashboardService.forecast() method");
		return finalData;
	}

}