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

import java.util.*;

public class DataProcessingServiceTest {

    @InjectMocks
    private DataProcessingService dataProcessingService;

    @Mock
    private DateFormat dateFormat;

    @Mock
    private DataTransformationService dataTransformationService;

    @Mock
    private CalculationUtils calculationUtils;

    @Mock
    private DateFormat.MonthYearComparator monthYearComparator;

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

    @Test
    public void testKpiMetrics_SuccessResponse() throws MyCustomException {
        // Setup
        List<ResultData> data = Arrays.asList(
                new ResultData(50L, 500.0, "2018-08"),
                new ResultData(100L, 1000.0, "2019-02"),
                new ResultData(150L, 1500.0, "2019-08"),
                new ResultData(200L, 2000.0, "2020-02")
        );
        Map<String, Long> targetPercentageMap = Map.of("Aug 2018", 5L, "Feb 2019", 6L, "Feb 2020", 7L);

        when(dateFormat.getPreviousMonths("2019-03", 1)).thenReturn("2019-02");
        when(dateFormat.getPreviousMonths("2019-02", 11)).thenReturn("2018-02");
        when(dateFormat.getPreviousYearMonth("2020-02")).thenReturn("2019-03");
        when(dateFormat.getPreviousYearMonth("2019-02")).thenReturn("2018-03");
        when(dateFormat.convertIntegertoStringDateFormat("2018-08")).thenReturn("Aug 2018");
        when(dateFormat.convertIntegertoStringDateFormat("2019-02")).thenReturn("Feb 2019");
        when(dateFormat.convertIntegertoStringDateFormat("2019-08")).thenReturn("Aug 2019");
        when(dateFormat.convertIntegertoStringDateFormat("2020-02")).thenReturn("Feb 2020");
        when(dateFormat.getTotalMonths(anyString(), anyString())).thenReturn(12);
        when(calculationUtils.roundToTwoDecimals(10.0)).thenReturn(10.0);
        when(calculationUtils.roundToTwoDecimals(1585.0/150)).thenReturn(10.67);
        when(calculationUtils.calculatePercentageChange(350L,158L)).thenReturn(221.0);
        when(calculationUtils.calculatePercentageChange(200L,106L)).thenReturn(188.0);
        when(calculationUtils.calculatePercentageChange(10.0,10.67)).thenReturn(-6.27);

        FinalResult expectedResult = new FinalResult(350L, 200L, 10.0, 158L, 106L, 10.67, 221.0, 188.0, -6.27);
    //    when(dataProcessingService.calculateFinalResult(anyLong(), anyLong(), anyDouble(), anyLong(), anyLong(), anyDouble())).thenReturn(expectedResult);

        // Execute
        FinalResult result = dataProcessingService.kpiMetrics(data, "2019-03", "2020-02", DataConstants.TARGET_VS_ACTUAL, targetPercentageMap);

        // Verify
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLandingPageMetrics_missingData() {
        // Setup
        List<ResultData> kpiMetrics = Arrays.asList(
                new ResultData(100L, 1000.0, "2019-02"),
                new ResultData(200L, 2000.0, "2020-02")
        );
        String startMonth = "2019-02";
        String endMonth = "2020-02";

        // Execute
        FinalResult result = dataProcessingService.landingPageMetrics(kpiMetrics, startMonth, endMonth);

        // Verify
        assertNotNull(result);
        assertEquals(new FinalResult(200L, 200L, 10.0, 100L, 100L, 10.0, 0.0, 0.0, 0.0), result);
    }

    @Test
    public void testAreaChart_SuccessResponse() {
        // Setup
        List<ResultData> areaChart = Arrays.asList(
                new ResultData(50L, 500.0, "2018-08"),
                new ResultData(100L, 1000.0, "2019-02"),
                new ResultData(150L, 1500.0, "2019-08"),
                new ResultData(200L, 2000.0, "2020-02")
        );
        Map<String, Long> targetPercentageMap = Map.of("2018-08", 5L, "2019-08", 6L, "2020-02", 7L);

        when(dateFormat.getPreviousMonths(anyString(), anyInt())).thenReturn("2019-02");
        when(dateFormat.getPreviousYearMonth("2020-02")).thenReturn("2019-03");
        when(dateFormat.getPreviousYearMonth("2019-02")).thenReturn("2018-03");
        when(dateFormat.convertIntegertoStringDateFormat("2018-08")).thenReturn("Aug 2018");
        when(dateFormat.convertIntegertoStringDateFormat("2019-02")).thenReturn("Feb 2019");
        when(dateFormat.convertIntegertoStringDateFormat("2019-08")).thenReturn("Aug 2019");
        when(dateFormat.convertIntegertoStringDateFormat("2020-02")).thenReturn("Feb 2020");
        when(calculationUtils.roundToTwoDecimals(10.0)).thenReturn(10.0);

        Map<String, Double> expectedCurrentData = Map.of("Aug 2019", 10.0, "Feb 2020", 10.0);
        Map<String, Double> expectedPreviousData = Map.of("Aug 2018", 10.0, "Feb 2019", 10.0);

        // Execute
        Map<String, Map<String, Double>> targetVsActualResult = dataProcessingService.areaChart(areaChart, "2020-02", DataConstants.TARGET_VS_ACTUAL, targetPercentageMap);
        Map<String, Map<String, Double>> currentVsPriorResult = dataProcessingService.areaChart(areaChart, "2020-02", DataConstants.CURRENT_VS_PRIOR, targetPercentageMap);

        // Verify
        assertNotNull(targetVsActualResult);
        assertEquals(expectedCurrentData, targetVsActualResult.get("actual"));
        assertEquals(expectedPreviousData, targetVsActualResult.get("target"));

        assertNotNull(currentVsPriorResult);
        assertEquals(expectedCurrentData, currentVsPriorResult.get("current"));
        assertEquals(expectedPreviousData, currentVsPriorResult.get("previous"));

        assertEquals(expectedCurrentData, currentVsPriorResult.get("current"));
        assertEquals(expectedPreviousData, currentVsPriorResult.get("previous"));

        // Validate keys are present
        assertTrue(targetVsActualResult.get("actual").containsKey("Aug 2019"));
        assertTrue(targetVsActualResult.get("target").containsKey("Aug 2018"));
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
        when(dateFormat.getPreviousMonths(any(), anyInt())).thenReturn("2019-01");
        when(dateFormat.getTotalMonths(any(), any())).thenReturn(13);
        when(calculationUtils.calculatePercentageChange(anyInt(), anyInt())).thenReturn(10.0);
        when(calculationUtils.roundToTwoDecimals(anyDouble())).thenReturn(100.0);

        // Execute
        Map<String, Map<String, MetricData>> currentVsPriorResult = dataProcessingService.serviceRegion(pmpm, memberView, targetPercentageMap, startMonth, endMonth, DataConstants.CURRENT_VS_PRIOR, viewType);

        startMonth = "";
        viewType = "Expense";
        Map<String, Map<String, MetricData>> targetVsActualResult = dataProcessingService.serviceRegion(pmpm, memberView, targetPercentageMap, startMonth, endMonth, DataConstants.TARGET_VS_ACTUAL, viewType);

        // Verify
        assertNotNull(currentVsPriorResult);

        assertFalse(currentVsPriorResult.get("all").isEmpty());
        assertNotNull(currentVsPriorResult.get("all").get("A"));
        assertFalse(currentVsPriorResult.get("ip").isEmpty());
        assertFalse(currentVsPriorResult.get("op").isEmpty());

        assertFalse(targetVsActualResult.get("all").isEmpty());
        assertFalse(targetVsActualResult.get("ip").isEmpty());
        assertFalse(targetVsActualResult.get("op").isEmpty());
    }

//    @Test
//    public void testServiceRegion_NullCondition() {
//        // Setup
//        List<PMPMDTO> pmpm = new ArrayList<>();
//        List<MemberViewDTO> memberView = new ArrayList<>();
//
//        when(dataTransformationService.filterServiceRegionMetric(anyList(), anyList(), any(), any(), any(), any()))
//                .thenReturn(new HashMap<>());
//
//        // Execute
//        Map<String, Map<String, MetricData>> result = dataProcessingService.serviceRegion(pmpm, memberView, null, null, null, null, null);
//        Map<String, Map<String, MetricData>> result2 = dataProcessingService.serviceRegion(pmpm, memberView, null, null, null, DataConstants.TARGET_VS_ACTUAL, null);
//
//        // Verify
//        assertTrue(result.get("all").isEmpty());
//        assertTrue(result.get("ip").isEmpty());
//        assertTrue(result.get("op").isEmpty());
//
//        assertTrue(result2.get("all").isEmpty());
//        assertTrue(result2.get("ip").isEmpty());
//        assertTrue(result2.get("op").isEmpty());
//    }


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

        when(dateFormat.getPreviousYearMonth(anyString())).thenReturn(endMonth);
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
