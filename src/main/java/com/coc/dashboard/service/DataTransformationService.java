package com.coc.dashboard.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.MetricData;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.util.CalculationUtils;
import com.coc.dashboard.util.DateFormat;

@Service
public class DataTransformationService {

	@Autowired
	private DateFormat dateFormat;

	@Autowired
	private CalculationUtils calculationUtils;

	public Map<String, Map<String, MetricData>> inpatientOutpatientMetrics(List<PMPMDTO> pmpm,
			Map<String, Long> targetPercentageMap, String startMonth, String endMonth, String graphType,
			Function<PMPMDTO, String> keyExtractor) {
		String prevEndMonth = (StringUtils.isEmpty(startMonth)) ? dateFormat.getPreviousMonths(endMonth, 1)
				: dateFormat.getPreviousMonths(startMonth, 1);
		Integer totalMonths = (StringUtils.isNotEmpty(startMonth)) ? dateFormat.getTotalMonths(startMonth, endMonth)
				: 0;
		String prevStartMonth = (StringUtils.isNotEmpty(startMonth))
				? dateFormat.getPreviousMonths(prevEndMonth, totalMonths - 1)
				: null;
		Map<String, Double> currentInpatient = filterMetric(pmpm, null, startMonth, endMonth, "I", keyExtractor);
		Map<String, Double> targetInpatient = filterMetric(pmpm,
				graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null, prevStartMonth,
				prevEndMonth, "I", keyExtractor);
		Map<String, Double> currentOutpatient = filterMetric(pmpm, null, startMonth, endMonth, "O", keyExtractor);
		Map<String, Double> targetOutpatient = filterMetric(pmpm,
				graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null, prevStartMonth,
				prevEndMonth, "O", keyExtractor);
		Map<String, Map<String, MetricData>> finalMetric = new HashMap<>();
		finalMetric.put("InPatient", finalMetric(currentInpatient, targetInpatient));
		finalMetric.put("OutPatient", finalMetric(currentOutpatient, targetOutpatient));
		return finalMetric;
	}

	public Map<String, Double> filterMetric(List<PMPMDTO> pmpm, Map<String, Long> targetPercentageMap,
			String startMonth, String endMonth, String patientType, Function<PMPMDTO, String> keyExtractor) {
		List<PMPMDTO> finalPmpm = pmpm.stream().map(PMPMDTO::clone).collect(Collectors.toList());
		return finalPmpm.stream()
				.filter(val -> StringUtils.isNotEmpty(startMonth)
						? (val.getMonths().compareTo(startMonth) >= 0 && val.getMonths().compareTo(endMonth) <= 0)
						: val.getMonths().compareTo(endMonth) == 0)
				.filter(val -> (patientType != null) ? val.getPatientType().compareTo(patientType) == 0 : true)
				.map(val -> {
					val.setSpeciality(val.getSpeciality() != null ? val.getSpeciality() : "Others");
					Double totalPricepm = (val.getTotalPricepm() == null) ? 0.0 : val.getTotalPricepm();
					if (ObjectUtils.isNotEmpty(targetPercentageMap)) {
						Long targetPercentageValue = targetPercentageMap.getOrDefault(val.getMonths(), 0L);
						totalPricepm += totalPricepm * targetPercentageValue / 100;
					}
					val.setTotalPricepm(totalPricepm);
					return val;
				}).collect(Collectors.toMap(keyExtractor, val -> val, (prev, cur) -> {
					prev.setTotalPricepm(prev.getTotalPricepm() + cur.getTotalPricepm());
					prev.setMemberCount(prev.getMemberCount() + cur.getMemberCount());
					return prev;
				})).values().stream()
				.collect(Collectors.toMap(keyExtractor, val -> val.getTotalPricepm() / val.getMemberCount()));
	}

	private Map<String, MetricData> finalMetric(Map<String, Double> currentCareCategory,
			Map<String, Double> targetCareCategory) {
		Set<String> allData = new HashSet<>(currentCareCategory.keySet());
		return allData.stream().collect(Collectors.toMap(data -> data, data -> {
			Double targetValue = targetCareCategory.getOrDefault(data, 0.0);
			Double actualValue = currentCareCategory.getOrDefault(data, 0.0);
			Double difference = actualValue - targetValue;
			Double percentageChange = calculationUtils.calculatePercentageChange(actualValue, targetValue);
			return new MetricData(calculationUtils.roundToTwoDecimals(targetValue),
					calculationUtils.roundToTwoDecimals(actualValue), calculationUtils.roundToTwoDecimals(difference),
					percentageChange);
		}, (v1, v2) -> v1, TreeMap::new));
	}

	public Map<String, ResultData> filterServiceRegionMetric(List<PMPMDTO> pmpm, List<MemberViewDTO> memberView,
			Map<String, Long> targetPercentageMap, String startMonth, String endMonth, String patientType) {
		List<PMPMDTO> finalPmpm = pmpm.stream().map(PMPMDTO::clone).collect(Collectors.toList());
		List<MemberViewDTO> finalMemberView = memberView.stream().map(MemberViewDTO::clone)
				.collect(Collectors.toList());
		Map<String, Long> memberViewMap = filterServiceRegionMemberViewMap(finalMemberView, startMonth, endMonth);

		finalPmpm = finalPmpm.stream().map(val -> {
			val.setState(val.getState() == null ? "Others" : val.getState());
			return val;
		}).filter(val -> (StringUtils.isNotEmpty(startMonth))
				? val.getMonths().compareTo(startMonth) >= 0 && val.getMonths().compareTo(endMonth) <= 0
				: val.getMonths().compareTo(endMonth) == 0)
				.filter(val -> (patientType != null) ? val.getPatientType().compareTo(patientType) == 0 : true)
				.collect(Collectors.toList());

		if (StringUtils.isEmpty(patientType)) {
			finalPmpm = finalPmpm.stream().map(val -> {
				val.setTotalPricepm(val.getTotalPricepm() != null ? val.getTotalPricepm() : 0.0);
				return val;
			}).collect(Collectors.groupingBy(val -> val.getMonths() + "_" + val.getState(),
					Collectors.collectingAndThen(Collectors.reducing((p1, p2) -> {
						p1.setTotalPricepm(p1.getTotalPricepm() + p2.getTotalPricepm());
						p1.setMemberCount(p1.getMemberCount() + p2.getMemberCount());
						p1.setPatientType(null);
						return p1;
					}), result -> result.orElse(new PMPMDTO())))).values().stream().collect(Collectors.toList());
		}

		return finalPmpm.stream().collect(Collectors.toMap(PMPMDTO::getState, val -> {
			long activeMembers = memberViewMap.getOrDefault(val.getState(), 0L);
			double pricepm = (val.getTotalPricepm() != null) ? val.getTotalPricepm() : 0.0;
			double pm = (activeMembers == 0) ? 0 : pricepm / activeMembers;
//			Long memberCount = val.getMemberCount();
//			double pm = (memberCount != null && memberCount != 0) ? pricepm / memberCount : 0.0;
			if (ObjectUtils.isNotEmpty(targetPercentageMap)) {
				long targetPercentage = targetPercentageMap.getOrDefault(val.getMonths(), 0L);
				pm += pm * targetPercentage / 100;
				activeMembers += activeMembers * targetPercentage / 100;
			}
			return new ResultData(activeMembers, pm, null);
		}, (existing, newValue) -> new ResultData(existing.getTotalActiveMembers(),
				existing.getTotalPricepm() + newValue.getTotalPricepm(), null)));
	}

	public Map<String, Long> filterServiceRegionMemberViewMap(List<MemberViewDTO> memberView, String startMonth,
			String endMonth) {
		return memberView.stream().map(val -> {
			val.setState(val.getState() == null ? "Others" : val.getState());
			return val;
		}).filter(val -> (StringUtils.isNotEmpty(startMonth))
				? val.getMonths().compareTo(startMonth) >= 0 && val.getMonths().compareTo(endMonth) <= 0
				: val.getMonths().compareTo(endMonth) == 0)
				.collect(Collectors.groupingBy(MemberViewDTO::getState,
						Collectors.summingLong(MemberViewDTO::getTotalActiveMembers)));
	}

	public Map<String, MetricData> pcpGroupMetrics(List<PMPMDTO> pmpm, Map<String, Long> targetPercentageMap,
			String startMonth, String endMonth, String graphType) {
		String prevEndMonth = (StringUtils.isEmpty(startMonth)) ? dateFormat.getPreviousMonths(endMonth, 1)
				: dateFormat.getPreviousMonths(startMonth, 1);
		Integer totalMonths = (StringUtils.isNotEmpty(startMonth)) ? dateFormat.getTotalMonths(startMonth, endMonth)
				: 0;
		String prevStartMonth = (StringUtils.isNotEmpty(startMonth))
				? dateFormat.getPreviousMonths(prevEndMonth, totalMonths - 1)
				: null;
		Map<String, Double> currentPcpGroup = filterMetric(pmpm, null, startMonth, endMonth, null,
				PMPMDTO::getProvider);
		Map<String, Double> targetPcpGroup = filterMetric(pmpm,
				graphType.equals(DataConstants.TARGET_VS_ACTUAL) ? targetPercentageMap : null, prevStartMonth,
				prevEndMonth, null, PMPMDTO::getProvider);
		return finalMetric(currentPcpGroup, targetPcpGroup);
	}

	public List<Object> filterForecastPmpmMetrics(List<Forecast_PMPM> pmpm, String startMonth, String endMonth) {
		return pmpm.stream()
				.filter(val -> StringUtils.isNotEmpty(startMonth)
						? val.getMonths().compareTo(startMonth) > 0 && val.getMonths().compareTo(endMonth) <= 0
						: val.getMonths().compareTo(endMonth) == 0)
				.map(val -> {
					val.setPmpm(val.getPmpm() == null ? 0.0 : val.getPmpm());
					val.setConfidenceInterval((val.getPmpm_forecast_upper() - val.getPmpm_forecast_lower()) / 2);
					return val;
				}).collect(Collectors.groupingBy(Forecast_PMPM::getMonths,
						Collectors.collectingAndThen(Collectors.reducing((p1, p2) -> {
							p1.setPmpm(p1.getPmpm() + p2.getPmpm());
							p1.setPmpm_forecast(p1.getPmpm_forecast() + p2.getPmpm_forecast());
							p1.setPmpm_forecast_lower(p1.getPmpm_forecast_lower() + p2.getPmpm_forecast_lower());
							p1.setPmpm_forecast_upper(p1.getPmpm_forecast_upper() + p2.getPmpm_forecast_upper());
							p1.setPmpm_forecast_mae(p1.getPmpm_forecast_mae() + p2.getPmpm_forecast_mae());
							p1.setConfidenceInterval(p1.getConfidenceInterval() + p2.getConfidenceInterval());
							return p1;
						}), result -> result.orElse(new Forecast_PMPM()))))
				.values().stream().sorted(Comparator.comparing(Forecast_PMPM::getMonths)).map(val -> {
					val.setMonths(dateFormat.convertIntegertoStringDateFormat(val.getMonths()));
					val.setPmpm(calculationUtils.roundToTwoDecimals(val.getPmpm()));
					val.setPmpm_forecast(calculationUtils.roundToTwoDecimals(val.getPmpm_forecast()));
					val.setPmpm_forecast_lower(calculationUtils.roundToTwoDecimals(val.getPmpm_forecast_lower()));
					val.setPmpm_forecast_upper(calculationUtils.roundToTwoDecimals(val.getPmpm_forecast_upper()));
					val.setPmpm_forecast_mae(calculationUtils.roundToTwoDecimals(val.getPmpm_forecast_mae()));
					val.setConfidenceInterval(calculationUtils.roundToTwoDecimals(val.getConfidenceInterval()));
					return val;
				}).collect(Collectors.toList());
	}

	public List<Object> filterForecastMemberMetrics(List<Forecast_ActiveMembership> pmpm, String startMonth,
			String endMonth) {
		return pmpm.stream()
				.filter(val -> StringUtils.isNotEmpty(startMonth)
						? val.getMonths().compareTo(startMonth) > 0 && val.getMonths().compareTo(endMonth) <= 0
						: val.getMonths().compareTo(endMonth) == 0)
				.map(val -> {
					val.setActivemembership(val.getActivemembership() == null ? 0L : val.getActivemembership());
					val.setConfidenceInterval(
							calculationUtils.roundToTwoDecimals((double) (val.getActivemembership_forecast_upper()
									- val.getActivemembership_forecast_lower()) / 2));
					return val;
				}).collect(Collectors.groupingBy(Forecast_ActiveMembership::getMonths,
						Collectors.collectingAndThen(Collectors.reducing((p1, p2) -> {
							p1.setActivemembership(p1.getActivemembership() + p2.getActivemembership());
							p1.setActivemembership_forecast(
									p1.getActivemembership_forecast() + p2.getActivemembership_forecast());
							p1.setActivemembership_forecast_lower(
									p1.getActivemembership_forecast_lower() + p2.getActivemembership_forecast_lower());
							p1.setActivemembership_forecast_upper(
									p1.getActivemembership_forecast_upper() + p2.getActivemembership_forecast_upper());
							p1.setActivemembership_forecast_mae(
									p1.getActivemembership_forecast_mae() + p2.getActivemembership_forecast_mae());
							p1.setConfidenceInterval(p1.getConfidenceInterval() + p2.getConfidenceInterval());
							return p1;
						}), result -> result.orElse(new Forecast_ActiveMembership()))))
				.values().stream().sorted(Comparator.comparing(Forecast_ActiveMembership::getMonths)).map(val -> {
					val.setMonths(dateFormat.convertIntegertoStringDateFormat(val.getMonths()));
					return val;
				}).collect(Collectors.toList());
	}
}
