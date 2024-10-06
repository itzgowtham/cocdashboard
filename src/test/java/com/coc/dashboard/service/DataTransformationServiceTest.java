package com.coc.dashboard.service;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.MetricData;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.util.CalculationUtils;
import com.coc.dashboard.util.DateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataTransformationServiceTest {

    //@Mock
    @Spy
    private DateFormat dateFormat;

    @Spy
    private CalculationUtils calculationUtils;

    @InjectMocks
    private DataTransformationService dataTransformationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<PMPMDTO> getInpatientOutpatientMetrics_pmpmList() {
        return Arrays.asList(
                new PMPMDTO(50.0, 5L, "2023-11", "SpecialityA", "I"),
                new PMPMDTO(75.0, 7L, "2023-12", "SpecialityA", "I"),
                new PMPMDTO(50.0, 5L, "2023-11", "SpecialityA", "O"),
                new PMPMDTO(75.0, 7L, "2023-12", "SpecialityA", "O"),
                new PMPMDTO(100.0, 10L, "2024-01", "SpecialityA", "I"),
                new PMPMDTO(150.0, 15L, "2024-02", "SpecialityB", "I"),
                new PMPMDTO(200.0, 20L, "2024-01", "SpecialityA", "O"),
                new PMPMDTO(250.0, 25L, "2024-02", "SpecialityB", "O"),
                new PMPMDTO(300.0, 30L, "2024-02", "SpecialityB", "I"),
                new PMPMDTO(350.0, 35L, "2024-02", null, "I"),
                new PMPMDTO(450.0, 45L, "2024-02", "SpecialityB", null),
                new PMPMDTO(450.0, 45L, "2024-03", "SpecialityB", "O")
        );
    }

    @Test
    void testInpatientOutpatientMetrics_TargetVsActual() {
        List<PMPMDTO> pmpmList = getInpatientOutpatientMetrics_pmpmList();
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 20L, "2023-11", 10L, "2023-12", 20L);

        Map<String, Map<String, MetricData>> targetVsActualResult = dataTransformationService.inpatientOutpatientMetrics(
                pmpmList,
                targetPercentageMap,
                "2024-01",
                "2024-02",
                DataConstants.TARGET_VS_ACTUAL,
                PMPMDTO::getSpeciality
        );

        assertNotNull(targetVsActualResult);
        assertEquals(2, targetVsActualResult.size());
        assertTrue(targetVsActualResult.containsKey("InPatient"));
        assertTrue(targetVsActualResult.containsKey("OutPatient"));
        assertEquals(3, targetVsActualResult.get("InPatient").size());
        assertEquals(2, targetVsActualResult.get("OutPatient").size());
        assertTrue(targetVsActualResult.get("InPatient").containsKey("SpecialityA"));
        assertTrue(targetVsActualResult.get("InPatient").containsKey("SpecialityB"));
        assertTrue(targetVsActualResult.get("InPatient").containsKey("Others"));
        assertTrue(targetVsActualResult.get("OutPatient").containsKey("SpecialityA"));
        assertTrue(targetVsActualResult.get("OutPatient").containsKey("SpecialityB"));
        assertFalse(targetVsActualResult.get("OutPatient").containsKey("Others"));
        assertNotNull(targetVsActualResult.get("InPatient").get("SpecialityA"));
        assertNotNull(targetVsActualResult.get("InPatient").get("SpecialityB"));
        assertNotNull(targetVsActualResult.get("InPatient").get("Others"));
        assertNotNull(targetVsActualResult.get("OutPatient").get("SpecialityA"));
        assertNotNull(targetVsActualResult.get("OutPatient").get("SpecialityB"));
        assertNull(targetVsActualResult.get("OutPatient").get("Others"));
        assertEquals(10.0, targetVsActualResult.get("InPatient").get("SpecialityA").getActual());
        assertEquals(12.08, targetVsActualResult.get("InPatient").get("SpecialityA").getTarget());
        assertEquals(-2.08, targetVsActualResult.get("InPatient").get("SpecialityA").getDifference());
        assertEquals(-17.24, targetVsActualResult.get("InPatient").get("SpecialityA").getDifferencePercentage());
        assertEquals(10.0, targetVsActualResult.get("InPatient").get("SpecialityB").getActual());
        assertEquals(0.0, targetVsActualResult.get("InPatient").get("SpecialityB").getTarget());
        assertEquals(10.0, targetVsActualResult.get("InPatient").get("SpecialityB").getDifference());
        assertEquals(100.0, targetVsActualResult.get("InPatient").get("SpecialityB").getDifferencePercentage());
        assertEquals(10.0, targetVsActualResult.get("OutPatient").get("SpecialityA").getActual());
        assertEquals(12.08, targetVsActualResult.get("OutPatient").get("SpecialityA").getTarget());
        assertEquals(-2.08, targetVsActualResult.get("OutPatient").get("SpecialityA").getDifference());
        assertEquals(-17.24, targetVsActualResult.get("OutPatient").get("SpecialityA").getDifferencePercentage());
        assertEquals(10.0, targetVsActualResult.get("OutPatient").get("SpecialityB").getActual());
        assertEquals(0.0, targetVsActualResult.get("OutPatient").get("SpecialityB").getTarget());
        assertEquals(10.0, targetVsActualResult.get("OutPatient").get("SpecialityB").getDifference());
        assertEquals(100.0, targetVsActualResult.get("OutPatient").get("SpecialityB").getDifferencePercentage());
    }

    @Test
    void testInpatientOutpatientMetrics_CurrentVsPrior() {
        List<PMPMDTO> pmpmList = getInpatientOutpatientMetrics_pmpmList();
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 20L, "2023-11", 10L, "2023-12", 20L);

        Map<String, Map<String, MetricData>> currentVsPriorResult = dataTransformationService.inpatientOutpatientMetrics(
                pmpmList,
                targetPercentageMap,
                null,
                "2024-02",
                DataConstants.CURRENT_VS_PRIOR,
                PMPMDTO::getSpeciality
        );

        assertNotNull(currentVsPriorResult);
        assertEquals(2, currentVsPriorResult.size());
        assertTrue(currentVsPriorResult.containsKey("InPatient"));
        assertTrue(currentVsPriorResult.containsKey("OutPatient"));
        assertEquals(2, currentVsPriorResult.get("InPatient").size());
        assertEquals(1, currentVsPriorResult.get("OutPatient").size());
        assertFalse(currentVsPriorResult.get("InPatient").containsKey("SpecialityA"));
        assertTrue(currentVsPriorResult.get("InPatient").containsKey("SpecialityB"));
        assertTrue(currentVsPriorResult.get("InPatient").containsKey("Others"));
        assertFalse(currentVsPriorResult.get("OutPatient").containsKey("SpecialityA"));
        assertTrue(currentVsPriorResult.get("OutPatient").containsKey("SpecialityB"));
        assertFalse(currentVsPriorResult.get("OutPatient").containsKey("Others"));
        assertNull(currentVsPriorResult.get("InPatient").get("SpecialityA"));
        assertNotNull(currentVsPriorResult.get("InPatient").get("SpecialityB"));
        assertNotNull(currentVsPriorResult.get("InPatient").get("Others"));
        assertNull(currentVsPriorResult.get("OutPatient").get("SpecialityA"));
        assertNotNull(currentVsPriorResult.get("OutPatient").get("SpecialityB"));
        assertNull(currentVsPriorResult.get("OutPatient").get("Others"));
        assertEquals(10.0, currentVsPriorResult.get("InPatient").get("SpecialityB").getActual());
        assertEquals(0.0, currentVsPriorResult.get("InPatient").get("SpecialityB").getTarget());
        assertEquals(10.0, currentVsPriorResult.get("InPatient").get("SpecialityB").getDifference());
        assertEquals(100.0, currentVsPriorResult.get("InPatient").get("SpecialityB").getDifferencePercentage());
        assertEquals(10.0, currentVsPriorResult.get("InPatient").get("Others").getActual());
        assertEquals(0.0, currentVsPriorResult.get("InPatient").get("Others").getTarget());
        assertEquals(10.0, currentVsPriorResult.get("InPatient").get("Others").getDifference());
        assertEquals(100.0, currentVsPriorResult.get("InPatient").get("Others").getDifferencePercentage());
        assertEquals(10.0, currentVsPriorResult.get("OutPatient").get("SpecialityB").getActual());
        assertEquals(0.0, currentVsPriorResult.get("OutPatient").get("SpecialityB").getTarget());
        assertEquals(10.0, currentVsPriorResult.get("OutPatient").get("SpecialityB").getDifference());
        assertEquals(100.0, currentVsPriorResult.get("OutPatient").get("SpecialityB").getDifferencePercentage());
    }

    private List<PMPMDTO> getServiceRegion_pmpmList() {
        return Arrays.asList(
                new PMPMDTO("2023-11", 50.0, 5L, "State1", "I"),
                new PMPMDTO("2024-01", 100.0, 10L, "State1", "I"),
                new PMPMDTO("2024-02", 150.0, 15L, "State2", "I"),
                new PMPMDTO("2024-03", 300.0, 30L, "State1", "I"),
                new PMPMDTO("2024-03", 400.0, 40L, "State1", null),
                new PMPMDTO("2024-03", 450.0, 45L, "State1", "I"),
                new PMPMDTO("2024-05", 500.0, 50L, "State2", "I"),
                new PMPMDTO("2024-03", 550.0, 55L, null, "I"),
                new PMPMDTO("2024-01", 600.0, 60L, "State1", "I"),
                new PMPMDTO("2024-02", 0.0, 15L, "State3", null)
        );
    }

    private List<MemberViewDTO> getServiceRegion_memberViewList() {
        return Arrays.asList(
                new MemberViewDTO(5L, "2023-11", "State1"),
                new MemberViewDTO(5L, "2024-01", "State1"),
                new MemberViewDTO(10L, "2024-02", "State1"),
                new MemberViewDTO(5L, "2024-03", "State1"),
                new MemberViewDTO(10L, "2024-04", "State1"),
                new MemberViewDTO(5L, "2024-01", "State2"),
                new MemberViewDTO(10L, "2024-02", "State2"),
                new MemberViewDTO(5L, "2024-03", "State2"),
                new MemberViewDTO(10L, "2024-04", "State2"),
                new MemberViewDTO(10L, "2024-04", null),
                new MemberViewDTO(5L, "2025-05", "State1")
        );
    }

    @Test
    void testFilterServiceRegionMetric() {
        List<PMPMDTO> pmpmList = getServiceRegion_pmpmList();
        List<MemberViewDTO> memberViewList = getServiceRegion_memberViewList();
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 10L, "2024-03", 10L, "2024-04", 10L);

        Map<String, ResultData> result = dataTransformationService.filterServiceRegionMetric(
                pmpmList,
                memberViewList,
                targetPercentageMap,
                "2024-01",
                "2024-04",
                "I"
        );

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("State1"));
        assertTrue(result.containsKey("State2"));
        assertTrue(result.containsKey("Others"));
        assertNotNull(result.get("State1"));
        assertNotNull(result.get("State2"));
        assertNotNull(result.get("Others"));
        assertEquals(53.16666666666667, result.get("State1").getTotalPricepm());
        assertEquals(33, result.get("State1").getTotalActiveMembers());
        assertNull(result.get("State1").getMonths());
        assertEquals(5.5, result.get("State2").getTotalPricepm());
        assertEquals(33, result.get("State2").getTotalActiveMembers());
        assertNull(result.get("State2").getMonths());
        assertEquals(60.5, result.get("Others").getTotalPricepm());
        assertEquals(11, result.get("Others").getTotalActiveMembers());
        assertNull(result.get("Others").getMonths());
    }

    @Test
    void testFilterServiceRegionMetric_WithNullStartMonth() {
        List<PMPMDTO> pmpmList = getServiceRegion_pmpmList();
        List<MemberViewDTO> memberViewList = getServiceRegion_memberViewList();
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 10L);

        Map<String, ResultData> result = dataTransformationService.filterServiceRegionMetric(
                pmpmList,
                memberViewList,
                targetPercentageMap,
                null,
                "2024-02",
                "I"
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.containsKey("State1"));
        assertTrue(result.containsKey("State2"));
        assertNull(result.get("State1"));
        assertNotNull(result.get("State2"));
        assertNull(result.get("Others"));
        assertEquals(16.5, result.get("State2").getTotalPricepm());
        assertEquals(11, result.get("State2").getTotalActiveMembers());
        assertNull(result.get("State2").getMonths());
    }

    @Test
    void testFilterServiceRegionMetric_WithNullPatientType() {
        List<PMPMDTO> pmpmList = getServiceRegion_pmpmList();
        List<MemberViewDTO> memberViewList = getServiceRegion_memberViewList();
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 10L);

        Map<String, ResultData> result = dataTransformationService.filterServiceRegionMetric(
                pmpmList,
                memberViewList,
                targetPercentageMap,
                "2024-01",
                "2024-02",
                null
        );

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("State1"));
        assertTrue(result.containsKey("State2"));
        assertTrue(result.containsKey("State3"));
        assertNotNull(result.get("State1"));
        assertNotNull(result.get("State2"));
        assertNotNull(result.get("State3"));
        assertEquals(51.33333333333333, result.get("State1").getTotalPricepm());
        assertEquals(16, result.get("State1").getTotalActiveMembers());
        assertNull(result.get("State1").getMonths());
        assertEquals(11.0, result.get("State2").getTotalPricepm());
        assertEquals(16, result.get("State2").getTotalActiveMembers());
        assertNull(result.get("State2").getMonths());
        assertTrue(0.0 == result.get("State3").getTotalPricepm());
        assertTrue(0 == result.get("State3").getTotalActiveMembers());
        assertNull(result.get("State3").getMonths());
    }

    @Test
    void testFilterServiceRegionMemberViewMap() {
        List<MemberViewDTO> memberViewList = Arrays.asList(
                new MemberViewDTO(5L, "2024-01", "State1"),
                new MemberViewDTO(10L, "2024-01", "State2")
        );

        Map<String, Long> result = dataTransformationService.filterServiceRegionMemberViewMap(
                memberViewList,
                "2024-01",
                "2024-01"
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5L, result.get("State1"));
        assertEquals(10L, result.get("State2"));
    }

    private List<PMPMDTO> getPcpGroupMetrics_pmpmList() {
        return Arrays.asList(new PMPMDTO("2023-11", "Provider1", 50.0, 5L, null),
                new PMPMDTO("2023-12", "Provider2", 100.0, 10L, null),
                new PMPMDTO("2024-01", "Provider1", 100.0, 10L, null),
                new PMPMDTO("2024-01", "Provider2", 150.0, 15L, null),
                new PMPMDTO("2024-02", "Provider1", 200.0, 20L, null),
                new PMPMDTO("2024-02", "Provider2", 250.0, 25L, null)
        );
    }

    @Test
    void testPcpGroupMetrics_TargetVsActual() {
        List<PMPMDTO> pmpmList = getPcpGroupMetrics_pmpmList();
        Map<String, Long> targetPercentageMap = Map.of("2023-11", 5L, "2023-12", 10L, "2024-01", 5L, "2024-02", 10L);

        Map<String, MetricData> result = dataTransformationService.pcpGroupMetrics(
                pmpmList,
                targetPercentageMap,
                "2024-01",
                "2024-02",
                DataConstants.TARGET_VS_ACTUAL
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("Provider1"));
        assertTrue(result.containsKey("Provider2"));
        assertNotNull(result.get("Provider1"));
        assertNotNull(result.get("Provider2"));
        assertEquals(10.5, result.get("Provider1").getTarget());
        assertEquals(10.0, result.get("Provider1").getActual());
        assertEquals(-0.5, result.get("Provider1").getDifference());
        assertEquals(-4.76, result.get("Provider1").getDifferencePercentage());
        assertEquals(11.0, result.get("Provider2").getTarget());
        assertEquals(10.0, result.get("Provider2").getActual());
        assertEquals(-1.0, result.get("Provider2").getDifference());
        assertEquals(-9.09, result.get("Provider2").getDifferencePercentage());
    }

    @Test
    void testPcpGroupMetrics_CurrentVsPrior() {
        List<PMPMDTO> pmpmList = getPcpGroupMetrics_pmpmList();
        Map<String, Long> targetPercentageMap = Map.of("2023-11", 5L, "2023-12", 10L, "2024-01", 5L, "2024-02", 10L);

        Map<String, MetricData> result = dataTransformationService.pcpGroupMetrics(
                pmpmList,
                targetPercentageMap,
                null,
                "2024-02",
                DataConstants.CURRENT_VS_PRIOR
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("Provider1"));
        assertTrue(result.containsKey("Provider2"));
        assertNotNull(result.get("Provider1"));
        assertNotNull(result.get("Provider2"));
        assertEquals(10.0, result.get("Provider1").getTarget());
        assertEquals(10.0, result.get("Provider1").getActual());
        assertEquals(0.0, result.get("Provider1").getDifference());
        assertEquals(0.0, result.get("Provider1").getDifferencePercentage());
        assertEquals(10.0, result.get("Provider2").getTarget());
        assertEquals(10.0, result.get("Provider2").getActual());
        assertEquals(0.0, result.get("Provider2").getDifference());
        assertEquals(0.0, result.get("Provider2").getDifferencePercentage());
    }

    @Test
    void testFilterForecastPmpmMetrics() {
        Forecast_PMPM forecastPmpm1 = getForecastPmpm("2020-11", 100.0, 100.0, 60.0, 100.0, 140.0, 10.0);
        Forecast_PMPM forecastPmpm2 = getForecastPmpm("2020-12", 75.432, 75.432, 20.123, 30.123, 40.123, 10.123);
        Forecast_PMPM forecastPmpm3= getForecastPmpm("2021-02", 150.0, 150.0, 100.0, 150.0, 200.0, 20.0);
        Forecast_PMPM forecastPmpm4 = getForecastPmpm("2021-01", null, 200.0, 150.0, 200.0, 250.0, 30.0);
        Forecast_PMPM forecastPmpm5 = getForecastPmpm("2021-02", 250.0, 250.0, 200.0, 250.0, 300.0, 40.0);
        Forecast_PMPM forecastPmpm6 = getForecastPmpm("2021-03", 300.0, 300.0, 250.0, 300.0, 350.0, 50.0);

        List<Forecast_PMPM> forecastList = Arrays.asList(forecastPmpm1, forecastPmpm2, forecastPmpm3, forecastPmpm4, forecastPmpm5, forecastPmpm6);

        List<Object> result = dataTransformationService.filterForecastPmpmMetrics(forecastList, "2020-11", "2021-02");
        List<Object> nullStartMonthresult = dataTransformationService.filterForecastPmpmMetrics(forecastList, null, "2021-02");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
        Forecast_PMPM first = (Forecast_PMPM) result.get(0);
        assertEquals("Dec 2020", first.getMonths());
        assertEquals(75.43, first.getPmpm());
        assertEquals(75.43, first.getPmpm_forecast());
        assertEquals(20.12, first.getPmpm_forecast_lower());
        assertEquals(30.12, first.getPmpm_forecast_mae());
        assertEquals(40.12, first.getPmpm_forecast_upper());
        assertEquals(10.0, first.getConfidenceInterval());
        Forecast_PMPM second = (Forecast_PMPM) result.get(1);
        assertEquals("Jan 2021", second.getMonths());
        assertEquals(0.0, second.getPmpm());
        assertEquals(200.0, second.getPmpm_forecast());
        assertEquals(150.0, second.getPmpm_forecast_lower());
        assertEquals(200.0, second.getPmpm_forecast_mae());
        assertEquals(250.0, second.getPmpm_forecast_upper());
        assertEquals(50.0, second.getConfidenceInterval());
        Forecast_PMPM third = (Forecast_PMPM) result.get(2);
        assertEquals("Feb 2021", third.getMonths());
        assertEquals(400.0, third.getPmpm());
        assertEquals(400.0, third.getPmpm_forecast());
        assertEquals(300.0, third.getPmpm_forecast_lower());
        assertEquals(400.0, third.getPmpm_forecast_mae());
        assertEquals(500.0, third.getPmpm_forecast_upper());
        assertEquals(100.0, third.getConfidenceInterval());

        assertNotNull(nullStartMonthresult);
        assertEquals(1, nullStartMonthresult.size());
        assertNotNull(nullStartMonthresult.get(0));
        Forecast_PMPM fourth = (Forecast_PMPM) nullStartMonthresult.get(0);
        assertEquals("Feb 2021", fourth.getMonths());
        assertEquals(250.0, fourth.getPmpm());
        assertEquals(250.0, fourth.getPmpm_forecast());
        assertEquals(200.0, fourth.getPmpm_forecast_lower());
        assertEquals(250.0, fourth.getPmpm_forecast_mae());
        assertEquals(300.0, fourth.getPmpm_forecast_upper());
        assertEquals(50.0, fourth.getConfidenceInterval());
    }

    private Forecast_PMPM getForecastPmpm(String months, Double pmpm, Double forecast, Double forecast_lower,
                                          Double forecast_mae, Double forecast_upper, Double confidenceInterval) {
        Forecast_PMPM forecastPmpm = new Forecast_PMPM();
        forecastPmpm.setMonths(months);
        forecastPmpm.setPmpm(pmpm);
        forecastPmpm.setPmpm_forecast(forecast);
        forecastPmpm.setPmpm_forecast_lower(forecast_lower);
        forecastPmpm.setPmpm_forecast_mae(forecast_mae);
        forecastPmpm.setPmpm_forecast_upper(forecast_upper);
        forecastPmpm.setConfidenceInterval(confidenceInterval);
        return forecastPmpm;
    }

    @Test
    void testFilterForecastMemberMetrics() {
        Forecast_ActiveMembership forecastActiveMembership1 = getForecastActiveMembership("2020-11", 50L, 50L, 25L, 50L, 75L, 10.0);
        Forecast_ActiveMembership forecastActiveMembership2 = getForecastActiveMembership("2020-12", 75L, 75L, 25L, 50L, 75L, 10.0);
        Forecast_ActiveMembership forecastActiveMembership3 = getForecastActiveMembership("2021-02", 100L, 100L, 50L, 100L, 150L, 20.0);
        Forecast_ActiveMembership forecastActiveMembership4 = getForecastActiveMembership("2021-01", null, 150L, 100L, 150L, 200L, 30.0);
        Forecast_ActiveMembership forecastActiveMembership5 = getForecastActiveMembership("2021-02", 200L, 200L, 150L, 200L, 250L, 40.0);
        Forecast_ActiveMembership forecastActiveMembership6 = getForecastActiveMembership("2021-03", 250L, 250L, 200L, 250L, 300L, 50.0);

        List<Forecast_ActiveMembership> forecastList = Arrays.asList(forecastActiveMembership1, forecastActiveMembership2,
                forecastActiveMembership3, forecastActiveMembership4, forecastActiveMembership5, forecastActiveMembership6);

        List<Object> result = dataTransformationService.filterForecastMemberMetrics(forecastList, "2020-12", "2021-02");
        List<Object> nullStartMonthresult = dataTransformationService.filterForecastMemberMetrics(forecastList, null, "2021-02");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
        Forecast_ActiveMembership first = (Forecast_ActiveMembership) result.get(0);
        assertEquals("Jan 2021", first.getMonths());
        assertEquals(0L, first.getActivemembership());
        assertEquals(150L, first.getActivemembership_forecast());
        assertEquals(100L, first.getActivemembership_forecast_lower());
        assertEquals(150L, first.getActivemembership_forecast_mae());
        assertEquals(200L, first.getActivemembership_forecast_upper());
        assertEquals(50.0, first.getConfidenceInterval());
        Forecast_ActiveMembership second = (Forecast_ActiveMembership) result.get(1);
        assertEquals("Feb 2021", second.getMonths());
        assertEquals(300L, second.getActivemembership());
        assertEquals(300L, second.getActivemembership_forecast());
        assertEquals(200L, second.getActivemembership_forecast_lower());
        assertEquals(300L, second.getActivemembership_forecast_mae());
        assertEquals(400L, second.getActivemembership_forecast_upper());
        assertEquals(100.0, second.getConfidenceInterval());

        assertNotNull(nullStartMonthresult);
        assertEquals(1, nullStartMonthresult.size());
        assertNotNull(nullStartMonthresult.get(0));
        Forecast_ActiveMembership third = (Forecast_ActiveMembership) nullStartMonthresult.get(0);
        assertEquals("Feb 2021", third.getMonths());
        assertEquals(200L, third.getActivemembership());
        assertEquals(200L, third.getActivemembership_forecast());
        assertEquals(150L, third.getActivemembership_forecast_lower());
        assertEquals(200L, third.getActivemembership_forecast_mae());
        assertEquals(250L, third.getActivemembership_forecast_upper());
        assertEquals(50.0, third.getConfidenceInterval());
    }

    private Forecast_ActiveMembership getForecastActiveMembership(String months, Long activeMembership, Long forecast,
                                                                  Long forecast_lower, Long forecast_mae, Long forecast_upper, Double confidenceInterval) {
        Forecast_ActiveMembership forecastActiveMembership = new Forecast_ActiveMembership();
        forecastActiveMembership.setMonths(months);
        forecastActiveMembership.setActivemembership(activeMembership);
        forecastActiveMembership.setActivemembership_forecast(forecast);
        forecastActiveMembership.setActivemembership_forecast_lower(forecast_lower);
        forecastActiveMembership.setActivemembership_forecast_mae(forecast_mae);
        forecastActiveMembership.setActivemembership_forecast_upper(forecast_upper);
        forecastActiveMembership.setConfidenceInterval(confidenceInterval);
        return forecastActiveMembership;
    }
}
