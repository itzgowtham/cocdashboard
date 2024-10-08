package com.coc.dashboard.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.*;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.util.CalculationUtils;
import com.coc.dashboard.util.DateFormat;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.*;

public class DataProcessingServiceTest {

    @InjectMocks
    private DataProcessingService dataProcessingService;

    @Spy
    private DateFormat dateFormat;

    @Mock
    private DataTransformationService dataTransformationService;

    @Spy
    private CalculationUtils calculationUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStringUtils() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty("2019-02"));
    }

    private List<ResultData> getKpiMetricsResultData() {
        List<ResultData> data = new ArrayList<>();
        data.add(new ResultData(25L, 250.0, "2018-03"));
        data.add(new ResultData(50L, 500.0, "2018-08"));
        data.add(new ResultData(100L, 1000.0, "2019-02"));
        data.add(new ResultData(150L, 1500.0, "2019-03"));
        data.add(new ResultData(200L, 2000.0, "2019-08"));
        data.add(new ResultData(250L, 2500.0, "2020-02"));
        data.add(new ResultData(300L, 3000.0, "2020-08"));
        return data;
    }

    @Test
    public void testKpiMetrics_TargetVsActual() throws MyCustomException {
        // Setup
        List<ResultData> data = getKpiMetricsResultData();
        Map<String, Long> targetPercentageMap = Map.of("Aug 2018", 5L, "Feb 2019", 6L, "Feb 2020", 7L);

        FinalResult expectedResult = new FinalResult(600L, 250L, 10.0, 183L, 106L, 10.49, 227.87, 135.85, -4.67);

        // Execute
        FinalResult result = dataProcessingService.kpiMetrics(data, "2019-03", "2020-02", DataConstants.TARGET_VS_ACTUAL, targetPercentageMap);

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testKpiMetrics_CurrentVsPrior() throws MyCustomException {
        // Setup
        List<ResultData> data = getKpiMetricsResultData();
        data.add(new ResultData(75L, 750.0, "2020-01"));
        Map<String, Long> targetPercentageMap = Map.of("Aug 2018", 5L, "Feb 2019", 6L, "Feb 2020", 7L);

        FinalResult expectedResult = new FinalResult(250L, 250L, 10.0, 75L, 75L, 10.0, 233.33, 233.33, 0.0);

        // Execute
        FinalResult result = dataProcessingService.kpiMetrics(data, null, "2020-02", DataConstants.CURRENT_VS_PRIOR, targetPercentageMap);

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testKpiMetrics_MissingPreviousData() throws MyCustomException {
        // Setup
        List<ResultData> data = List.of(new ResultData(250L, 2500.0, "2020-02"));
        FinalResult expectedResult = new FinalResult(250L, 250L, 10.0, 0L, 0L, 0.0, 0.0, 0.0, 0.0);

        // Execute
        FinalResult result = dataProcessingService.kpiMetrics(data, null, "2020-02", DataConstants.CURRENT_VS_PRIOR, new HashMap<>());

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testKpiMetrics_CustomException() throws MyCustomException {
        // Setup
        List<ResultData> data = new ArrayList<>();

        // Execute
        Exception exception = assertThrows(MyCustomException.class, () -> dataProcessingService.kpiMetrics(data, null, "2020-02", DataConstants.TARGET_VS_ACTUAL, new HashMap<>()));

        // Verify
        assertEquals("No Data Found", exception.getMessage());
    }

    @Test
    public void testLandingPageMetrics() {
        // Setup
        List<ResultData> kpiMetrics = getKpiMetricsResultData();
        FinalResult expectedResult = new FinalResult(250L, 250L, 10.0, 100L, 100L, 10.0, 150.0, 150.0, 0.0);

        // Execute
        FinalResult result = dataProcessingService.landingPageMetrics(kpiMetrics, "2019-02", "2020-02");

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLandingPageMetrics_missingCurrentData() {
        // Setup
        List<ResultData> kpiMetrics = getKpiMetricsResultData();
        FinalResult expectedResult = new FinalResult(0L, 0L, 0.0, 0L, 0L, 0.0, 0.0, 0.0, 0.0);

        // Execute
        FinalResult result = dataProcessingService.landingPageMetrics(kpiMetrics, "2020-01", "2020-02");

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLandingPageMetrics_missingPreviousData() {
        // Setup
        List<ResultData> kpiMetrics = new ArrayList<>();
        FinalResult expectedResult = new FinalResult(0L, 0L, 0.0, 0L, 0L, 0.0, 0.0, 0.0, 0.0);

        // Execute
        FinalResult result = dataProcessingService.landingPageMetrics(kpiMetrics, "2020-01", "2020-02");

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAreaChart_SuccessResponse() {
        // Setup
        List<ResultData> areaChart = Arrays.asList(
                new ResultData(50L, 500.0, "2018-08"),
                new ResultData(100L, 1000.0, "2019-02"),
                new ResultData(150L, 1500.0, "2019-08"),
                new ResultData(200L, 2000.0, "2020-02"),
                new ResultData(250L, 2500.0, "2020-02"),
                new ResultData(300L, 3000.0, "2019-02"),
                new ResultData(350L, 3500.0, "2020-08")
        );
        Map<String, Long> targetPercentageMap = Map.of("Aug 2018", 5L, "Feb 2019", 5L, "Aug 2019", 5L, "Feb 2020", 5L);

        Map<String, Double> expectedCurrentData = Map.of("Aug 2019", 10.0, "Feb 2020", 10.0);
        Map<String, Double> expectedPreviousData = Map.of("Feb 2019", 10.0, "Aug 2019", 10.0);
        Map<String, Double> expectedTargetData = Map.of("Feb 2019", 10.5, "Aug 2019", 10.5);

        // Execute
        Map<String, Map<String, Double>> targetVsActualResult = dataProcessingService.areaChart(areaChart, "2020-02", DataConstants.TARGET_VS_ACTUAL, targetPercentageMap);
        Map<String, Map<String, Double>> currentVsPriorResult = dataProcessingService.areaChart(areaChart, "2020-02", DataConstants.CURRENT_VS_PRIOR, targetPercentageMap);

        // Verify
        assertNotNull(targetVsActualResult);
        assertEquals(expectedCurrentData, targetVsActualResult.get("actual"));
        assertEquals(10.0, targetVsActualResult.get("actual").get("Feb 2020"));
        assertEquals(10.0, targetVsActualResult.get("actual").get("Aug 2019"));
        assertEquals(expectedTargetData, targetVsActualResult.get("target"));
        assertEquals(10.5, targetVsActualResult.get("target").get("Feb 2019"));
        assertEquals(10.5, targetVsActualResult.get("target").get("Aug 2019"));

        assertNotNull(currentVsPriorResult);
        assertEquals(expectedCurrentData, currentVsPriorResult.get("current"));
        assertEquals(expectedPreviousData, currentVsPriorResult.get("previous"));

        assertEquals(expectedCurrentData, currentVsPriorResult.get("current"));
        assertEquals(expectedPreviousData, currentVsPriorResult.get("previous"));

        // Validate keys are present
        assertTrue(targetVsActualResult.get("actual").containsKey("Aug 2019"));
        assertTrue(targetVsActualResult.get("target").containsKey("Aug 2019"));
        assertTrue(currentVsPriorResult.get("current").containsKey("Feb 2020"));
        assertTrue(currentVsPriorResult.get("previous").containsKey("Feb 2019"));
    }

    @Test
    public void testServiceRegion_SuccessResponse() {
        // Setup
        List<PMPMDTO> pmpm = new ArrayList<>();
        List<MemberViewDTO> memberView = new ArrayList<>();
        Map<String, Long> targetPercentageMap = Map.of("2018-02",5L,"2019-01", 6L,"2019-02", 7L, "2020-01", 8L, "2020-02", 9L);
        String startMonth = "2019-02";
        String endMonth = "2020-02";
        String viewType = DataConstants.EXPENSE_PMPM;
        Map<String, ResultData> currentResultData = new HashMap<>();
        currentResultData.put("A", new ResultData(100L, 1000.0, "2018"));
        currentResultData.put("B", new ResultData(100L, 1000.0, "2019"));
        currentResultData.put("C", new ResultData(100L, 1000.0, "2020"));
        currentResultData.put("D", null);
        Map<String, ResultData> previousResultData = new HashMap<>();
        previousResultData.put("A", new ResultData(100L, 1000.0, "2021"));
        previousResultData.put("B", new ResultData(100L, 1000.0, "2022"));
        previousResultData.put("E", new ResultData(100L, 1000.0, "2023"));

        when(dataTransformationService.filterServiceRegionMetric(anyList(), anyList(), eq(null), any(), any(), any()))
                .thenReturn(currentResultData);
        when(dataTransformationService.filterServiceRegionMetric(anyList(), anyList(), eq(targetPercentageMap), any(), any(), any()))
                .thenReturn(previousResultData);

        // Execute
        Map<String, Map<String, MetricData>> currentVsPriorResult = dataProcessingService.serviceRegion(pmpm, memberView, targetPercentageMap, startMonth, endMonth, DataConstants.CURRENT_VS_PRIOR, viewType);

        startMonth = "";
        viewType = "Expense";
        Map<String, Map<String, MetricData>> targetVsActualResult = dataProcessingService.serviceRegion(pmpm, memberView, targetPercentageMap, startMonth, endMonth, DataConstants.TARGET_VS_ACTUAL, viewType);

        // Verify
        assertNotNull(currentVsPriorResult);

        assertFalse(currentVsPriorResult.get("all").isEmpty());
        assertNotNull(currentVsPriorResult.get("all").get("A"));
        assertEquals(1000.0, currentVsPriorResult.get("all").get("A").getActual());
        assertEquals(1000.0, currentVsPriorResult.get("all").get("A").getTarget());
        assertEquals(0.0, currentVsPriorResult.get("all").get("A").getDifference());
        assertEquals(0.0, currentVsPriorResult.get("all").get("A").getDifferencePercentage());
        assertFalse(currentVsPriorResult.get("ip").isEmpty());
        assertFalse(currentVsPriorResult.get("op").isEmpty());

        assertFalse(targetVsActualResult.get("all").isEmpty());
        assertNotNull(targetVsActualResult.get("all").get("A"));
        assertEquals(100.0, targetVsActualResult.get("all").get("A").getActual());
        assertEquals(100.0, targetVsActualResult.get("all").get("A").getTarget());
        assertEquals(0.0, targetVsActualResult.get("all").get("A").getDifference());
        assertEquals(0.0, targetVsActualResult.get("all").get("A").getDifferencePercentage());
        assertFalse(targetVsActualResult.get("ip").isEmpty());
        assertFalse(targetVsActualResult.get("op").isEmpty());
    }

    @Test
    public void testCareCategory_SuccessResponse() {
        // Setup
        List<PMPMDTO> pmpm = new ArrayList<>();
        Map<String, Long> targetPercentageMap = new HashMap<>();
        String startMonth = "2019-02";
        String endMonth = "2020-02";
        String graphType = DataConstants.TARGET_VS_ACTUAL;
        Map<String, MetricData> expectedMetrics = Collections.singletonMap("careCategory", new MetricData(100.0, 100.0, 0.0, 0.0));

        when(dataTransformationService.inpatientOutpatientMetrics(anyList(), anyMap(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.singletonMap("careCategory", expectedMetrics));

        // Execute
        Map<String, Map<String, MetricData>> result = dataProcessingService.careCategory(pmpm, targetPercentageMap, startMonth, endMonth, graphType);

        // Verify
        assertNotNull(result);
        assertEquals(expectedMetrics, result.get("careCategory"));
    }

    @Test
    public void testProviderSpeciality_SuccessResponse() {
        // Setup
        List<PMPMDTO> pmpm = new ArrayList<>();
        Map<String, Long> targetPercentageMap = new HashMap<>();
        String startMonth = "2019-02";
        String endMonth = "2020-02";
        String graphType = DataConstants.TARGET_VS_ACTUAL;
        Map<String, MetricData> expectedMetrics = Collections.singletonMap("providerSpeciality", new MetricData(100.0, 100.0, 0.0, 0.0));

        when(dataTransformationService.inpatientOutpatientMetrics(anyList(), anyMap(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.singletonMap("providerSpeciality", expectedMetrics));

        // Execute
        Map<String, Map<String, MetricData>> result = dataProcessingService.providerSpeciality(pmpm, targetPercentageMap, startMonth, endMonth, graphType);

        // Verify
        assertNotNull(result);
        assertEquals(expectedMetrics, result.get("providerSpeciality"));
    }

    @Test
    public void testCareProvider_SuccessResponse() {
        // Setup
        List<PMPMDTO> pmpm = new ArrayList<>();
        Map<String, Long> targetPercentageMap = new HashMap<>();
        String startMonth = "2019-02";
        String endMonth = "2020-02";
        String graphType = DataConstants.TARGET_VS_ACTUAL;
        Map<String, MetricData> expectedMetrics = Collections.singletonMap("careProvider", new MetricData(100.0, 100.0, 0.0, 0.0));

        when(dataTransformationService.inpatientOutpatientMetrics(anyList(), anyMap(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.singletonMap("careProvider", expectedMetrics));

        // Execute
        Map<String, Map<String, MetricData>> result = dataProcessingService.careProvider(pmpm, targetPercentageMap, startMonth, endMonth, graphType);

        // Verify
        assertNotNull(result);
        assertEquals(expectedMetrics, result.get("careProvider"));
    }

    @Test
    public void testPcpGroup_SuccessResponse() {
        // Setup
        List<PMPMDTO> pmpm = new ArrayList<>();
        Map<String, Long> targetPercentageMap = new HashMap<>();
        String startMonth = "2019-02";
        String endMonth = "2020-02";
        String graphType = DataConstants.TARGET_VS_ACTUAL;
        Map<String, MetricData> expectedMetrics = Collections.singletonMap("pcpGroup", new MetricData(100.0, 100.0, 0.0, 0.0));

        when(dataTransformationService.pcpGroupMetrics(anyList(), anyMap(), anyString(), anyString(), anyString()))
                .thenReturn(expectedMetrics);

        // Execute
        Map<String, MetricData> result = dataProcessingService.pcpGroup(pmpm, targetPercentageMap, startMonth, endMonth, graphType);

        // Verify
        assertNotNull(result);
        assertEquals(expectedMetrics, result);
    }

    @Test
    public void testForecast_SuccessResponse() {
        // Setup
        List<Forecast_PMPM> pmpm = Arrays.asList(new Forecast_PMPM(), new Forecast_PMPM());
        List<Forecast_ActiveMembership> member = Arrays.asList(new Forecast_ActiveMembership(), new Forecast_ActiveMembership());
        String endMonth = "2020-02";

        when(dataTransformationService.filterForecastPmpmMetrics(anyList(), anyString(), anyString())).thenReturn(new ArrayList<>(pmpm));
        when(dataTransformationService.filterForecastMemberMetrics(anyList(), anyString(), anyString())).thenReturn(new ArrayList<>(member));

        Map<String, List<Object>> expectedForecast = new HashMap<>();
        expectedForecast.put("pmpm", new ArrayList<>(pmpm));
        expectedForecast.put("member", new ArrayList<>(member));

        // Execute
        Map<String, List<Object>> result = dataProcessingService.forecast(pmpm, member, endMonth);

        // Verify
        assertNotNull(result);
        assertEquals(expectedForecast, result);
    }
}
