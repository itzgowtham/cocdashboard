package com.coc.dashboard.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.FinalResult;
import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.MetricData;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.util.CalculationUtils;
import com.coc.dashboard.util.DateFormat;
import com.coc.dashboard.util.DateFormat.MonthYearComparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataProcessingService {

	@Autowired
	private DateFormat dateFormat;

	@Autowired
	private DataTransformationService dataTransformationService;

	@Autowired
	private CalculationUtils calculationUtils;

	public FinalResult kpiMetrics(List<ResultData> data, String startMonth, String endMonth, String graphType,
			Map<String, Long> targetPercentageMap) throws MyCustomException {
		log.info("Inside DataProcessingService.kpiMetrics() method");
		List<ResultData> finalData = cloneData(data);
		List<ResultData> currentYearMonths = filterYearMonths(finalData, startMonth, endMonth);

		finalData = cloneData(data);
		String prevEndMonth = (StringUtils.isEmpty(startMonth)) ? dateFormat.getPreviousMonths(endMonth, 1)
				: dateFormat.getPreviousMonths(startMonth, 1);
		Integer totalMonths = (StringUtils.isNotEmpty(startMonth)) ? dateFormat.getTotalMonths(startMonth, endMonth)
				: 0;
		String prevStartMonth = (StringUtils.isNotEmpty(startMonth))
				? dateFormat.getPreviousMonths(prevEndMonth, totalMonths - 1)
				: null;
		List<ResultData> previousYearMonths = filterYearMonths(finalData, prevStartMonth, prevEndMonth);

		ResultData currentYearResult = calculateResultData(currentYearMonths);
		ResultData previousYearResult = (graphType.equals(DataConstants.TARGET_VS_ACTUAL))
				? calculateTargetResultData(previousYearMonths, targetPercentageMap)
				: calculateResultData(previousYearMonths);

		Long activeMembers = currentYearResult.getTotalActiveMembers();
		Long endingMembers = (currentYearMonths.size() > 0) ? currentYearMonths.get(0).getTotalActiveMembers() : 0;
		Double pmpmData = calculationUtils.roundToTwoDecimals(currentYearResult.getTotalPricepm() / activeMembers);
		Long prevActiveMembers = previousYearResult.getTotalActiveMembers();
		Long prevEndingMembers = (previousYearMonths.size() > 0) ? previousYearMonths.get(0).getTotalActiveMembers()
				: 0;
		Double prevPmpm = calculationUtils.roundToTwoDecimals(graphType.equals(DataConstants.TARGET_VS_ACTUAL)
				? previousYearResult.getTotalPricepm() / calculateResultData(previousYearMonths).getTotalActiveMembers()
				: previousYearResult.getTotalPricepm() / prevActiveMembers);
		Long prevEndingMembersPercentage = targetPercentageMap
				.getOrDefault(dateFormat.convertIntegertoStringDateFormat(prevEndMonth), 0L);
		prevEndingMembers = graphType.equals(DataConstants.TARGET_VS_ACTUAL)
				? prevEndingMembers + prevEndingMembers * prevEndingMembersPercentage / 100
				: prevEndingMembers;
		FinalResult finalResult = null;
		if (currentYearMonths.size() == 0) {
			throw new MyCustomException("No Data Found");
		}
		if (currentYearMonths.size() == previousYearMonths.size()) {
			finalResult = calculateFinalResult(activeMembers, endingMembers, pmpmData, prevActiveMembers,
					prevEndingMembers, prevPmpm);
		} else {
			finalResult = new FinalResult(activeMembers, endingMembers, pmpmData, 0L, 0L, 0.0, 0.0, 0.0, 0.0);
		}
		log.info("Exiting DataProcessingService.kpiMetrics() method");
		return finalResult;
	}

	private List<ResultData> cloneData(List<ResultData> data) {
		return data.stream().map(ResultData::clone).collect(Collectors.toList());
	}

	private List<ResultData> filterYearMonths(List<ResultData> finalData, String startMonth, String endMonth) {
		return finalData.stream()
				.filter(val -> (StringUtils.isNotEmpty(startMonth))
						? (val.getMonths().compareTo(startMonth) >= 0 && val.getMonths().compareTo(endMonth) <= 0)
						: val.getMonths().compareTo(endMonth) == 0)
				.map(val -> {
					val.setMonths(new DateFormat().convertIntegertoStringDateFormat(val.getMonths()));
					return val;
				}).sorted(Comparator.comparing(ResultData::getMonths, new MonthYearComparator()))
				.collect(Collectors.toList());
	}

	private ResultData calculateResultData(List<ResultData> yearMonths) {
		Long totalActiveMembers = yearMonths.stream().mapToLong(ResultData::getTotalActiveMembers).sum();
		Double totalPricepm = yearMonths.stream().mapToDouble(ResultData::getTotalPricepm).sum();
		return new ResultData(totalActiveMembers, totalPricepm, "");
	}

	private ResultData calculateTargetResultData(List<ResultData> yearMonths, Map<String, Long> targetPercentageMap) {
		return yearMonths.stream().reduce(new ResultData(0L, 0.0, ""), (partialResult, res) -> {
			Long targetPercentage = targetPercentageMap.getOrDefault(res.getMonths(), 0L);
			Long adjustedActiveMembers = res.getTotalActiveMembers()
					+ (res.getTotalActiveMembers() * targetPercentage / 100);
			double adjustedPricepm = res.getTotalPricepm() + (res.getTotalPricepm() * targetPercentage / 100.0);
			return new ResultData(partialResult.getTotalActiveMembers() + adjustedActiveMembers,
					partialResult.getTotalPricepm() + adjustedPricepm, null);
		});
	}

	public Map<String, Map<String, Double>> areaChart(List<ResultData> areaChart, String endMonth, String graphType,
			Map<String, Long> targetPercentageMap) {
		log.info("Inside DataProcessingService.areaChart() method");
		Map<String, Map<String, Double>> finalMap = new LinkedHashMap<>();
		List<ResultData> finalData = cloneData(areaChart);
		List<ResultData> currentYearMonths = calculateYearMonths(finalData, endMonth);

		finalData = cloneData(areaChart);
		String previousEndMonth = dateFormat.getPreviousMonths(endMonth, 1);
		List<ResultData> previousYearMonths = calculateYearMonths(finalData, previousEndMonth);

		if (graphType.equals(DataConstants.TARGET_VS_ACTUAL)) {
			finalMap.put("actual", mapData(currentYearMonths, null));
			finalMap.put("target", mapData(previousYearMonths, targetPercentageMap));
		} else {
			finalMap.put("current", mapData(currentYearMonths, null));
			finalMap.put("previous", mapData(previousYearMonths, null));
		}
		log.info("Exiting DataProcessingService.areaChart() method");
		return finalMap;
	}

	private List<ResultData> calculateYearMonths(List<ResultData> areaChart, String endMonth) {
		String startMonth = dateFormat.getPreviousYearMonth(endMonth);
		return areaChart.stream()
				.filter(val -> val.getMonths().compareTo(startMonth) > 0 && val.getMonths().compareTo(endMonth) <= 0)
				.limit(12).map(val -> {
					val.setMonths(dateFormat.convertIntegertoStringDateFormat(val.getMonths()));
					return val;
				}).sorted(Comparator.comparing(ResultData::getMonths, new MonthYearComparator()))
				.collect(Collectors.toList());
	}

	private Map<String, Double> mapData(List<ResultData> months, Map<String, Long> targetPercentageMap) {
		Map<String, Double> mapData = months.stream().collect(Collectors.toMap(ResultData::getMonths, entry -> {
			double pricePM = entry.getTotalPricepm();
			double activeMembers = entry.getTotalActiveMembers();
			double pmpm = pricePM / activeMembers;
			if (targetPercentageMap != null) {
				Long targetPercentage = targetPercentageMap.getOrDefault(entry.getMonths(), 0L);
				pmpm += pmpm * targetPercentage / 100;
			}
			return calculationUtils.roundToTwoDecimals(pmpm);
		}, (v1, v2) -> v1, LinkedHashMap::new));
		return mapData;
	}

	public FinalResult calculateFinalResult(Long memberMonths, Long endingMembers, Double pmpm, Long prevMemberMonths,
			Long prevEndingMembers, Double prevPmpm) {
		Double memberMonthsPercentageChange = calculationUtils.calculatePercentageChange(memberMonths,
				prevMemberMonths);
		Double endingMembersPercentageChange = calculationUtils.calculatePercentageChange(endingMembers,
				prevEndingMembers);
		Double pmpmPercentageChange = calculationUtils.calculatePercentageChange(pmpm, prevPmpm);
		return new FinalResult(memberMonths, endingMembers, pmpm, prevMemberMonths, prevEndingMembers, prevPmpm,
				memberMonthsPercentageChange, endingMembersPercentageChange, pmpmPercentageChange);
	}

	public Map<String, Map<String, MetricData>> careCategory(List<PMPMDTO> pmpm, Map<String, Long> targetPercentageMap,
			String startMonth, String endMonth, String graphType) {
		log.info("Inside DataProcessingService.careCategory() method");
		Map<String, Map<String, MetricData>> finalMetric = dataTransformationService.inpatientOutpatientMetrics(pmpm,
				targetPercentageMap, startMonth, endMonth, graphType, PMPMDTO::getSpeciality);
		log.info("Exiting DataProcessingService.careCategory() method");
		return finalMetric;
	}

	public Map<String, Map<String, MetricData>> providerSpeciality(List<PMPMDTO> pmpm,
			Map<String, Long> targetPercentageMap, String startMonth, String endMonth, String graphType) {
		log.info("Inside DataProcessingService.providerSpeciality() method");
		Map<String, Map<String, MetricData>> finalMetric = dataTransformationService.inpatientOutpatientMetrics(pmpm,
				targetPercentageMap, startMonth, endMonth, graphType, PMPMDTO::getSpeciality);
		log.info("Exiting DataProcessingService.providerSpeciality() method");
		return finalMetric;
	}

	public Map<String, Map<String, MetricData>> serviceRegion(List<PMPMDTO> pmpm, List<MemberViewDTO> memberView,
			Map<String, Long> targetPercentageMap, String startMonth, String endMonth, String graphType,
			String viewType) {
		log.info("Inside DataProcessingService.serviceRegion() method");
		String prevEndMonth = (StringUtils.isEmpty(startMonth)) ? dateFormat.getPreviousMonths(endMonth, 1)
				: dateFormat.getPreviousMonths(startMonth, 1);
		Integer totalMonths = (StringUtils.isNotEmpty(startMonth)) ? dateFormat.getTotalMonths(startMonth, endMonth)
				: 0;
		String prevStartMonth = (StringUtils.isNotEmpty(startMonth))
				? dateFormat.getPreviousMonths(prevEndMonth, totalMonths - 1)
				: null;
		Map<String, ResultData> currentFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, null, startMonth, endMonth, null);
		Map<String, ResultData> previousFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null,
				prevStartMonth, prevEndMonth, null);
		Map<String, ResultData> currentIpFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, null, startMonth, endMonth, "I");
		Map<String, ResultData> previousIpFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null,
				prevStartMonth, prevEndMonth, "I");
		Map<String, ResultData> currentOpFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, null, startMonth, endMonth, "O");
		Map<String, ResultData> previousOpFinalMetric = dataTransformationService.filterServiceRegionMetric(pmpm,
				memberView, graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null,
				prevStartMonth, prevEndMonth, "O");
		Map<String, MetricData> allFinalMetric = serviceRegionResult(currentFinalMetric, previousFinalMetric, viewType);
		Map<String, MetricData> ipFinalMetric = serviceRegionResult(currentIpFinalMetric, previousIpFinalMetric,
				viewType);
		Map<String, MetricData> opFinalMetric = serviceRegionResult(currentOpFinalMetric, previousOpFinalMetric,
				viewType);
		Map<String, Map<String, MetricData>> finalMetric = new HashMap<>();
		finalMetric.put("all", allFinalMetric);
		finalMetric.put("ip", ipFinalMetric);
		finalMetric.put("op", opFinalMetric);
		log.info("Exiting DataProcessingService.serviceRegion() method");
		return finalMetric;
	}

	private Map<String, MetricData> serviceRegionResult(Map<String, ResultData> currentServiceRegion,
			Map<String, ResultData> targetServiceRegion, String viewType) {
		Set<String> allStates = new HashSet<>(currentServiceRegion.keySet());
		return allStates.stream().collect(Collectors.toMap(state -> state, state -> {
			ResultData currentData = currentServiceRegion.getOrDefault(state, null);
			ResultData targetData = targetServiceRegion.getOrDefault(state, null);
			Double targetValue = 0.0;
			Double actualValue = 0.0;
			if (viewType.equals(DataConstants.EXPENSE_PMPM)) {
				targetValue = (targetData != null) ? targetData.getTotalPricepm() : 0.0;
				actualValue = (currentData != null) ? currentData.getTotalPricepm() : 0.0;
			} else {
				targetValue = (targetData != null) ? targetData.getTotalActiveMembers() : 0.0;
				actualValue = (currentData != null) ? currentData.getTotalActiveMembers() : 0.0;
			}
			Double difference = actualValue - targetValue;
			Double percentageChange = calculationUtils.calculatePercentageChange(actualValue, targetValue);
			return new MetricData(calculationUtils.roundToTwoDecimals(targetValue),
					calculationUtils.roundToTwoDecimals(actualValue), calculationUtils.roundToTwoDecimals(difference),
					percentageChange);
		}, (v1, v2) -> v1, TreeMap::new));
	}

	public Map<String, Map<String, MetricData>> careProvider(List<PMPMDTO> pmpm, Map<String, Long> targetPercentageMap,
			String startMonth, String endMonth, String graphType) {
		log.info("Inside DataProcessingService.careProvider() method");
		Map<String, Map<String, MetricData>> finalMetric = dataTransformationService.inpatientOutpatientMetrics(pmpm,
				targetPercentageMap, startMonth, endMonth, graphType, PMPMDTO::getProvider);
		log.info("Exiting DataProcessingService.careProvider() method");
		return finalMetric;
	}

	public Map<String, MetricData> pcpGroup(List<PMPMDTO> pmpm, Map<String, Long> targetPercentageMap,
			String startMonth, String endMonth, String graphType) {
		log.info("Inside DataProcessingService.pcpGroup() method");
		Map<String, MetricData> finalMetric = dataTransformationService.pcpGroupMetrics(pmpm, targetPercentageMap,
				startMonth, endMonth, graphType);
		log.info("Exiting DataProcessingService.pcpGroup() method");
		return finalMetric;
	}

	public Map<String, List<Object>> forecast(List<Forecast_PMPM> pmpm, List<Forecast_ActiveMembership> member,
			String endMonth) {
		log.info("Inside DataProcessingService.forecast() method");
		String prevEndMonth = dateFormat.getPreviousYearMonth(endMonth);
		Map<String, List<Object>> finalMap = new HashMap<>();
		List<Object> forecast_pmpm = dataTransformationService.filterForecastPmpmMetrics(pmpm, prevEndMonth, endMonth);
		List<Object> forecast_member = dataTransformationService.filterForecastMemberMetrics(member, prevEndMonth,
				endMonth);
		finalMap.put("pmpm", forecast_pmpm);
		finalMap.put("member", forecast_member);
		log.info("Exiting DataProcessingService.forecast() method");
		return finalMap;
	}

}
