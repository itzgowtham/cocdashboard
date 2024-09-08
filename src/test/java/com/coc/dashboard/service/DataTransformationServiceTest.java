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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataTransformationServiceTest {

    @Mock
    private DateFormat dateFormat;

    @Mock
    private CalculationUtils calculationUtils;

    @InjectMocks
    private DataTransformationService dataTransformationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInpatientOutpatientMetrics() {
        List<PMPMDTO> pmpmList = Arrays.asList(
                new PMPMDTO(100.0, 10L, "2024-01", "SpecialityA", "Type1"),
                new PMPMDTO(150.0, 15L, "2024-02", "SpecialityB", "Type2")
        );
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L, "2024-02", 20L);

        when(dateFormat.getPreviousMonths("2024-01", 1)).thenReturn("2023-12");
        when(dateFormat.getPreviousMonths("2024-02", 1)).thenReturn("2024-01");
        when(dateFormat.getTotalMonths("2024-01", "2024-02")).thenReturn(2);

        when(calculationUtils.calculatePercentageChange(anyDouble(), anyDouble())).thenReturn(0.0);
        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenAnswer(invocation -> (Double) invocation.getArguments()[0]);

        Map<String, Map<String, MetricData>> result = dataTransformationService.inpatientOutpatientMetrics(
                pmpmList,
                targetPercentageMap,
                "2024-01",
                "2024-02",
                DataConstants.TARGET_VS_ACTUAL,
                PMPMDTO::getProvider
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("InPatient"));
        assertTrue(result.containsKey("OutPatient"));
    }

    @Test
    void testFilterServiceRegionMetric() {
        List<PMPMDTO> pmpmList = Arrays.asList(
                new PMPMDTO(100.0, 10L, "2024-01", "SpecialityA", "Type1"),
                new PMPMDTO(200.0, 20L, "2024-01", "SpecialityB", "Type2")
        );
        List<MemberViewDTO> memberViewList = Arrays.asList(
                new MemberViewDTO(5L, "2024-01", "State1"),
                new MemberViewDTO(10L, "2024-01", "State2")
        );
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 10L);

        when(dateFormat.convertIntegertoStringDateFormat(anyString())).thenReturn("2024-01");

        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenAnswer(invocation -> (Double) invocation.getArguments()[0]);

        Map<String, ResultData> result = dataTransformationService.filterServiceRegionMetric(
                pmpmList,
                memberViewList,
                targetPercentageMap,
                "2024-01",
                "2024-01",
                null
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.containsKey("State1"));
        assertFalse(result.containsKey("State2"));
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

    @Test
    void testPcpGroupMetrics() {
        List<PMPMDTO> pmpmList = Arrays.asList(
                new PMPMDTO("2024-01", "Provider1", 100.0, 10L, "Type1"),
                new PMPMDTO("2024-02", "Provider2", 150.0, 20L, "Type2")
        );
        Map<String, Long> targetPercentageMap = Map.of("2024-01", 5L, "2024-02", 10L);

        when(dateFormat.getPreviousMonths("2024-01", 1)).thenReturn("2023-12");
        when(dateFormat.getPreviousMonths("2024-02", 1)).thenReturn("2024-01");
        when(dateFormat.getTotalMonths("2024-01", "2024-02")).thenReturn(2);

        when(calculationUtils.calculatePercentageChange(anyDouble(), anyDouble())).thenReturn(0.0);
        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenAnswer(invocation -> (Double) invocation.getArguments()[0]);

        Map<String, MetricData> result = dataTransformationService.pcpGroupMetrics(
                pmpmList,
                targetPercentageMap,
                "2024-01",
                "2024-02",
                DataConstants.TARGET_VS_ACTUAL
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        MetricData metricData1 = result.get("Provider1");
        assertNotNull(metricData1);
        assertEquals(0.0, metricData1.getTarget());
        assertEquals(10.0, metricData1.getActual());
    }

    @Test
    void testFilterForecastPmpmMetrics() {
        Forecast_PMPM forecastPmpm1 = getForecastPmpm("2020-11",100.0,100.0,60.0, 100.0, 140.0, 10.0);
        Forecast_PMPM forecastPmpm2 = getForecastPmpm("2021-02", 150.0, 150.0, 100.0, 150.0, 200.0, 20.0);
        Forecast_PMPM forecastPmpm3 = getForecastPmpm("2021-01", null, 200.0, 150.0,200.0, 250.0, 30.0);
        Forecast_PMPM forecastPmpm4 = getForecastPmpm("2021-02", 250.0, 250.0, 200.0, 250.0, 300.0, 40.0);
        Forecast_PMPM forecastPmpm5 = getForecastPmpm("2021-03", 300.0, 300.0, 250.0, 300.0, 350.0, 50.0);

        List<Forecast_PMPM> forecastList = Arrays.asList(forecastPmpm1, forecastPmpm2, forecastPmpm3, forecastPmpm4, forecastPmpm5);

        when(dateFormat.convertIntegertoStringDateFormat("2021-01")).thenReturn("Jan 2021");
        when(dateFormat.convertIntegertoStringDateFormat("2021-02")).thenReturn("Feb 2021");
        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenAnswer(invocation -> (Double) invocation.getArguments()[0]);

        List<Object> result = dataTransformationService.filterForecastPmpmMetrics(forecastList, "2020-12", "2021-02");
        List<Object> nullStartMonthresult = dataTransformationService.filterForecastPmpmMetrics(forecastList, null, "2021-02");

        assertNotNull(result);
        assertEquals(2, result.size());
        Forecast_PMPM first = (Forecast_PMPM) result.get(1);
        assertEquals("Feb 2021", first.getMonths());
        assertEquals(400.0, first.getPmpm());
        assertEquals(400.0, first.getPmpm_forecast());
        assertEquals(300.0, first.getPmpm_forecast_lower());
        assertEquals(400.0, first.getPmpm_forecast_mae());
        assertEquals(500.0, first.getPmpm_forecast_upper());
        assertEquals(100.0, first.getConfidenceInterval());

        assertNotNull(nullStartMonthresult);
        Forecast_PMPM second = (Forecast_PMPM) result.get(0);
        assertEquals(0L, second.getPmpm());
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
        Forecast_ActiveMembership forecastActiveMembership2 = getForecastActiveMembership("2021-02", 100L, 100L, 50L, 100L, 150L, 20.0);
        Forecast_ActiveMembership forecastActiveMembership3 = getForecastActiveMembership("2021-01", null, 150L, 100L, 150L, 200L, 30.0);
        Forecast_ActiveMembership forecastActiveMembership4 = getForecastActiveMembership("2021-02", 200L, 200L, 150L, 200L, 250L, 40.0);
        Forecast_ActiveMembership forecastActiveMembership5 = getForecastActiveMembership("2021-03", 250L, 250L, 200L, 250L, 300L, 50.0);

        List<Forecast_ActiveMembership> forecastList = Arrays.asList(forecastActiveMembership1, forecastActiveMembership2,
                forecastActiveMembership3, forecastActiveMembership4, forecastActiveMembership5);

        when(dateFormat.convertIntegertoStringDateFormat("2021-01")).thenReturn("Jan 2021");
        when(dateFormat.convertIntegertoStringDateFormat("2021-02")).thenReturn("Feb 2021");
        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenAnswer(invocation -> (Double) invocation.getArguments()[0]);

        List<Object> result = dataTransformationService.filterForecastMemberMetrics(forecastList, "2020-12", "2021-02");
        List<Object> nullStartMonthresult = dataTransformationService.filterForecastMemberMetrics(forecastList, null, "2021-02");

        assertNotNull(result);
        assertEquals(2, result.size());
        Forecast_ActiveMembership first = (Forecast_ActiveMembership) result.get(1);
        assertEquals("Feb 2021", first.getMonths());
        assertEquals(300L, first.getActivemembership());
        assertEquals(300L, first.getActivemembership_forecast());
        assertEquals(200L, first.getActivemembership_forecast_lower());
        assertEquals(300L, first.getActivemembership_forecast_mae());
        assertEquals(400L, first.getActivemembership_forecast_upper());
        assertEquals(100.0, first.getConfidenceInterval());

        assertNotNull(nullStartMonthresult);
        Forecast_ActiveMembership second = (Forecast_ActiveMembership) result.get(0);
        assertEquals(0L, second.getActivemembership());
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
