package com.coc.dashboard.service;

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

    @Mock
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
        pmpmObject.setStartMonth("2019-02");
        pmpmObject.setEndMonth("2020-02");
        return pmpmObject;
    }

    private TargetPMPM getTargetPmpm() {
        TargetPMPM targetPMPM = new TargetPMPM();
        targetPMPM.setMonths("key");
        targetPMPM.setTargetPercentage(50L);
        return targetPMPM;
    }

    @Test
    void testSummary() throws MyCustomException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");

        when(dateFormat.convertStringtoIntegerDateFormat("2020-02")).thenReturn("2020-02");
        // Mock fetchData using reflection to handle private method invocation
        DataPair<List<ResultData>, List<TargetPMPM>, String> mockDataPair =
                new DataPair<>(List.of(new ResultData()), List.of(new TargetPMPM()), "2020-02");
        when(dataAccessService.kpiMetrics(any(), any())).thenReturn(mockDataPair);
        Map<String, Long> mockTargetPercentageMap = Collections.singletonMap("key", 50L);
        when(dataModificationService.convertToTargetPercentageMap(anyList())).thenReturn(mockTargetPercentageMap);
        mockPrivateMethod(dashboardService, "fetchData", String.class, String.class)
                .invoke(dashboardService, "TestLOB", "TestState");

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

        pmpmObject.setEndMonth(" ");
        result = dashboardService.summary(pmpmObject);
        assertNotNull(result);
        assertEquals(mockFinalResult, result.get("kpimetrics"));
        assertEquals(mockMapData, result.get("areaChart"));
    }

    @Test
    void testLandingPage() throws MyCustomException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Mock fetchData using reflection to handle private method invocation
        DataPair<List<ResultData>, List<TargetPMPM>, String> mockDataPair =
                new DataPair<>(List.of(new ResultData()), List.of(new TargetPMPM()), "2020-02");
        when(dataAccessService.kpiMetrics(any(), any())).thenReturn(mockDataPair);
        Map<String, Long> mockTargetPercentageMap = Collections.singletonMap("key", 50L);
        when(dataModificationService.convertToTargetPercentageMap(anyList())).thenReturn(mockTargetPercentageMap);
        mockPrivateMethod(dashboardService, "fetchData", String.class, String.class)
                .invoke(dashboardService, null, null);

        // Mocking process.landingPageMetrics
        FinalResult mockFinalResult = new FinalResult();
        when(process.landingPageMetrics(anyList(), any(), any()))
                .thenReturn(mockFinalResult);

        // Calling the service method under test
        FinalResult result = dashboardService.landingPage();

        // Assertions
        assertEquals(mockFinalResult, result);
    }

    /**
     * Helper method to mock private methods using reflection.
     */
    private Method mockPrivateMethod(Object target, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
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
        pmpmObject.setGraphType("TestGraphType");

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
    }

    @Test
    void testServiceRegion() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setGraphType("TestGraphType");
        pmpmObject.setViewType("TestViewType");

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
    }

    @Test
    void testForecast() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();

        // Mocking dataAccessService.forecast
        DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> mockDataPair =
                new DataPair<>(List.of(new Forecast_PMPM()), List.of(new Forecast_ActiveMembership()), new Object());
        when(dataAccessService.forecast(anyString(), anyString()))
                .thenReturn(mockDataPair);

        // Mocking process.forecast
        Map<String, List<Object>> mockFinalData = Map.of("mockKey", List.of(new Object()));
        when(process.forecast(anyList(), anyList(), any()))
                .thenReturn(mockFinalData);

        // Calling the service method under test
        Map<String, List<Object>> result = dashboardService.forecast(pmpmObject);

        //testing for null scenario
        pmpmObject.setLob(null);
        pmpmObject.setState(null);
        Map<String, List<Object>> nullResult = dashboardService.forecast(pmpmObject);

        // Assertions
        assertEquals(mockFinalData, result);
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

    @Test
    public void testValidatePMPMObject_Success() throws MyCustomException {
        // Mock DateFormat behavior
        when(dateFormat.convertStringtoIntegerDateFormat("2019-02")).thenReturn("2019-02");
        when(dateFormat.convertStringtoIntegerDateFormat("2020-02")).thenReturn("2020-02");

        // Prepare PMPMObject with test data
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("Commercial");
        pmpmObject.setState("LA");
        pmpmObject.setStartMonth("2019-02");
        pmpmObject.setEndMonth("2020-02");
        pmpmObject.setGraphType("Current vs Prior");
        pmpmObject.setViewType("PMPM");

        // Call the method under test indirectly (through public methods)
        PMPMObject validatedObject = invokeValidatePMPMObject(pmpmObject);

        // Assert that validation has processed correctly
        assertEquals("Commercial", validatedObject.getLob());
        assertEquals("LA", validatedObject.getState());
        assertEquals("2019-02", validatedObject.getStartMonth());
        assertEquals("2020-02", validatedObject.getEndMonth());
        assertEquals("Current vs Prior", validatedObject.getGraphType());
        assertEquals("PMPM", validatedObject.getViewType());
        // Add more assertions as needed for other fields
    }

    @Test
    public void testValidatePMPMObject_NullPoint() throws MyCustomException {
        // Prepare PMPMObject with test data
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("");
        pmpmObject.setState("");
        pmpmObject.setStartMonth("");
        pmpmObject.setEndMonth("");
        pmpmObject.setGraphType("");
        pmpmObject.setViewType("");

        // Call the method under test indirectly (through public methods)
        PMPMObject validatedObject = invokeValidatePMPMObject(pmpmObject);

        // Assert that validation has processed correctly
        assertEquals(null, validatedObject.getLob());
        assertEquals(null, validatedObject.getState());
        assertEquals(null, validatedObject.getStartMonth());
        assertEquals(null, validatedObject.getEndMonth());
        assertEquals("Target vs Actual", validatedObject.getGraphType());
        assertEquals("Expense PMPM", validatedObject.getViewType());
        // Add more assertions as needed for other fields
    }

    // Utility method to invoke private method validatePMPMObject using reflection
    private PMPMObject invokeValidatePMPMObject(PMPMObject pmpmObject) throws MyCustomException {
        try {
            // Use reflection to access the private method
            Method validateMethod = dashboardService.getClass().getDeclaredMethod("validatePMPMObject", PMPMObject.class);
            validateMethod.setAccessible(true); // Ensure method is accessible
            return (PMPMObject) validateMethod.invoke(dashboardService, pmpmObject); // Cast to PMPMObject
        } catch (Exception e) {
            throw new MyCustomException("Failed to invoke validatePMPMObject");
        }
    }
}
