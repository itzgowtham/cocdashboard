package com.coc.dashboard.service;

import com.coc.dashboard.constants.DataConstants;
import com.coc.dashboard.dto.*;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.util.DateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DashboardServiceTest {

    @Spy
    private DateFormat dateFormat;

    @Mock
    private DataAccessService dataAccessService;

    @Mock
    private DataProcessingService process;

    @Mock
    private DataModificationService dataModificationService;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PMPMObject preparePMPMObject() {
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("TestLOB");
        pmpmObject.setState("TestState");
        pmpmObject.setStartMonth("Nov 2019");
        pmpmObject.setEndMonth("Dec 2019");
        return pmpmObject;
    }

    private TargetPMPM getTargetPmpm() {
        TargetPMPM targetPMPM = new TargetPMPM();
        targetPMPM.setMonths("key");
        targetPMPM.setTargetPercentage(50L);
        return targetPMPM;
    }

    @Test
    void testSummary() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");

        // Mock fetchData using reflection to handle private method invocation
        DataPair<List<ResultData>, List<TargetPMPM>, String> mockDataPair =
                new DataPair<>(List.of(new ResultData()), List.of(new TargetPMPM()), "2019-12");
        when(dataAccessService.kpiMetrics(any(), any())).thenReturn(mockDataPair);
        Map<String, Long> mockTargetPercentageMap = Collections.singletonMap("key", 50L);
        when(dataModificationService.convertToTargetPercentageMap(anyList())).thenReturn(mockTargetPercentageMap);

        // Mocking process.kpiMetrics
        FinalResult mockFinalResult = new FinalResult();
        when(process.kpiMetrics(anyList(), any(), any(), any(), anyMap()))
                .thenReturn(mockFinalResult);

        // Mocking process.areaChart
        Map<String, Map<String, Double>> mockMapData = Map.of("mockKey", Map.of("mockSubKey", 10.0));
        when(process.areaChart(anyList(), any(), any(), anyMap()))
                .thenReturn(mockMapData);

        // Calling the service method under test
        Map<String, Object> result = dashboardService.summary(pmpmObject);

        // Assertions
        assertEquals(mockFinalResult, result.get("kpimetrics"));
        assertEquals(mockFinalResult, result.get("kpimetrics"));
        assertEquals(mockMapData, result.get("areaChart"));

        pmpmObject.setStartMonth("Nov 2019");
        pmpmObject.setEndMonth(" ");
        result = dashboardService.summary(pmpmObject);
        assertNotNull(result);
        assertEquals(mockFinalResult, result.get("kpimetrics"));
        assertEquals(mockMapData, result.get("areaChart"));

        verify(process, times(2)).kpiMetrics(mockDataPair.getFirst(), "2019-11", "2019-12", "TestGraphType", mockTargetPercentageMap);
        verify(process, times(2)).areaChart(mockDataPair.getFirst(), "2019-12", "TestGraphType", mockTargetPercentageMap);
    }

    @Test
    void testLandingPage() throws MyCustomException {
        // Mock fetchData using reflection to handle private method invocation
        DataPair<List<ResultData>, List<TargetPMPM>, String> mockDataPair =
                new DataPair<>(List.of(new ResultData()), List.of(new TargetPMPM()), "2020-02");
        when(dataAccessService.kpiMetrics(any(), any())).thenReturn(mockDataPair);
        Map<String, Long> mockTargetPercentageMap = Collections.singletonMap("key", 50L);
        when(dataModificationService.convertToTargetPercentageMap(anyList())).thenReturn(mockTargetPercentageMap);

        // Mocking process.landingPageMetrics
        FinalResult mockFinalResult = new FinalResult();
        when(process.landingPageMetrics(anyList(), any(), any()))
                .thenReturn(mockFinalResult);

        // Calling the service method under test
        FinalResult result = dashboardService.landingPage();

        // Assertions
        assertEquals(mockFinalResult, result);
    }

    @Test
    void testDistinctLobStateMonths() throws MyCustomException {
        // Mocking dataAccessService.distinctLobStateMonths
        List<String> mockList = List.of("Test1", "Test2");
        when(dataAccessService.distinctLobStateMonths())
                .thenReturn(Map.of("key", mockList));

        // Calling the service method under test
        Map<String, List<String>> result = dashboardService.distinctLobStateMonths();

        // Assertions
        assertEquals(mockList, result.get("key"));
    }

    @Test
    void testCareCategory() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("");

        // Mocking dataAccessService.careCategory
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> mockDataPair =
                new DataPair<>(List.of(new PMPMDTO()), List.of(getTargetPmpm()), new Object());
        when(dataAccessService.careCategory(anyString(), anyString()))
                .thenReturn(mockDataPair);

        // Mocking process.careCategory
        Map<String, Map<String, MetricData>> mockFinalData = Map.of("mockKey", Map.of("mockSubKey", new MetricData()));
        when(process.careCategory(anyList(), anyMap(), any(), any(), any()))
                .thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, Map<String, MetricData>> result = dashboardService.careCategory(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(dataAccessService, times(1)).careCategory("TestLOB", "TestState");
        verify(process, times(1)).careCategory(mockDataPair.getFirst(), Collections.singletonMap("key", 50L), "2019-11", "2019-12", DataConstants.TARGET_VS_ACTUAL);
    }

    @Test
    void testProviderSpeciality() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");

        // Mocking dataAccessService.providerSpeciality
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> mockDataPair =
                new DataPair<>(List.of(new PMPMDTO()), List.of(getTargetPmpm()), new Object());
        when(dataAccessService.providerSpeciality(anyString(), anyString()))
                .thenReturn(mockDataPair);

        // Mocking process.providerSpeciality
        Map<String, Map<String, MetricData>> mockFinalData = Map.of("mockKey", Map.of("mockSubKey", new MetricData()));
        when(process.providerSpeciality(anyList(), anyMap(), any(), any(), any()))
                .thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, Map<String, MetricData>> result = dashboardService.providerSpeciality(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(process, times(1)).providerSpeciality(mockDataPair.getFirst(), Collections.singletonMap("key", 50L), "2019-11", "2019-12", "TestGraphType");
    }

    @Test
    void testServiceRegion() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");
        pmpmObject.setViewType("");

        // Mocking dataAccessService.serviceRegion
        DataPair<List<PMPMDTO>, List<MemberViewDTO>, List<TargetPMPM>> mockDataPair =
                new DataPair<>(List.of(new PMPMDTO()), List.of(new MemberViewDTO()), List.of(getTargetPmpm()));
        when(dataAccessService.serviceRegion(anyString(), anyString()))
                .thenReturn(mockDataPair);

        // Mocking process.serviceRegion
        Map<String, Map<String, MetricData>> mockFinalData = Map.of("mockKey", Map.of("mockSubKey", new MetricData()));
        when(process.serviceRegion(anyList(), anyList(), anyMap(), any(), any(), any(), any()))
                .thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, Map<String, MetricData>> result = dashboardService.serviceRegion(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(process, times(1)).serviceRegion(mockDataPair.getFirst(), mockDataPair.getSecond(), Collections.singletonMap("key", 50L), "2019-11", "2019-12", "TestGraphType", DataConstants.EXPENSE_PMPM);
    }

    @Test
    void testCareProvider() throws MyCustomException {
        // Mock the dependencies
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");

        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> mockDataPair =
                new DataPair<>(List.of(new PMPMDTO()), List.of(getTargetPmpm()), new Object());
        when(dataAccessService.careProvider(anyString(), anyString())).thenReturn(mockDataPair);

        // Mock process.careProvider to return expected data
        Map<String, Map<String, MetricData>> mockFinalData = Map.of("mockKey", Map.of("subKey", new MetricData()));
        when(process.careProvider(anyList(), anyMap(), any(), any(), any())).thenReturn(mockFinalData);

        // Call the service method under test
        Map<String, Map<String, MetricData>> result = dashboardService.careProvider(pmpmObject);

        // Assertions
//        assertNotNull(result);
        assertEquals(mockFinalData, result);
        verify(process, times(1)).careProvider(mockDataPair.getFirst(), Collections.singletonMap("key", 50L), "2019-11", "2019-12", "TestGraphType");
    }

    @Test
    void testPcpGroup() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");

        // Mocking dataAccessService.pcpGroup
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> mockDataPair =
                new DataPair<>(List.of(new PMPMDTO()), List.of(getTargetPmpm()), new Object());
        when(dataAccessService.pcpGroup(anyString(), anyString()))
                .thenReturn(mockDataPair);

        // Mocking process.pcpGroup
        Map<String, MetricData> mockFinalData = Map.of("mockKey", new MetricData());
        when(process.pcpGroup(anyList(), anyMap(), any(), any(), any()))
                .thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, MetricData> result = dashboardService.pcpGroup(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(process, times(1)).pcpGroup(mockDataPair.getFirst(), Collections.singletonMap("key", 50L), "2019-11", "2019-12", "TestGraphType");
    }

    @Test
    void testForecast() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();

        // Mocking dataAccessService.forecast
        DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> mockDataPair =
                new DataPair<>(List.of(new Forecast_PMPM()), List.of(new Forecast_ActiveMembership()), new Object());
        when(dataAccessService.forecast(anyString(), anyString())).thenReturn(mockDataPair);

        // Mocking process.forecast
        Map<String, List<Object>> mockFinalData = Map.of("mockKey", List.of(new Object()));
        when(process.forecast(anyList(), anyList(), any())).thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, List<Object>> result = dashboardService.forecast(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(dataAccessService, times(1)).forecast("TestLOB", "TestState");
        verify(process, times(1)).forecast(mockDataPair.getFirst(), mockDataPair.getSecond(), "2019-12");
    }

    @Test
    void testForecast_NullValues() throws MyCustomException {
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("");
        pmpmObject.setState("");
        pmpmObject.setStartMonth("");
        pmpmObject.setEndMonth("");

        // Mocking dataAccessService.forecast
        DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> mockDataPair =
                new DataPair<>(List.of(new Forecast_PMPM()), List.of(new Forecast_ActiveMembership()), new Object());
        when(dataAccessService.forecast(any(), any())).thenReturn(mockDataPair);

        // Mocking process.forecast
        Map<String, List<Object>> mockFinalData = Map.of("mockKey", List.of(new Object()));
        when(process.forecast(anyList(), anyList(), any())).thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, List<Object>> result = dashboardService.forecast(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
        verify(dataAccessService, times(1)).forecast("All", "All");
    }

    @Test
    void testGetTargetPercentageMap() {
        // Prepare a list of mocked TargetPMPM objects
        TargetPMPM targetPMPM1 = new TargetPMPM();
        targetPMPM1.setMonths("Jan");
        targetPMPM1.setTargetPercentage(5L);
        TargetPMPM targetPMPM2 = new TargetPMPM();
        targetPMPM2.setMonths("Feb");
        targetPMPM2.setTargetPercentage(6L);
        List<TargetPMPM> targetPMPMList = List.of(targetPMPM1, targetPMPM2);

        Map<String, Long> result = dashboardService.getTargetPercentageMap(targetPMPMList);
        assertEquals(2, result.size()); // Asserting size of the map
        assertEquals(5L, result.get("Jan")); // Asserting targetPercentage for January
        assertEquals(6L, result.get("Feb")); // Asserting targetPercentage for February
    }
}
