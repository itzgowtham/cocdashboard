package com.coc.dashboard.service;

import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.util.DateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class DashboardDetailsServiceTest {

    @Mock
    private DateFormat dateFormat;

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private DashboardDetailsService dashboardDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCareCategoryDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.careCategoryDetails method
        when(dataAccessService.careCategoryDetails(anyString(), anyString(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.careCategoryDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
    }

    @Test
    public void testServiceRegionDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.serviceRegionDetails method
        when(dataAccessService.serviceRegionDetails(anyString(), anyString(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.serviceRegionDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
    }

    @Test
    public void testProviderSpecialityDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setSpeciality("TestSpeciality");

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.providerSpecialityDetails method
        when(dataAccessService.providerSpecialityDetails(anyString(), anyString(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.providerSpecialityDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
    }

    @Test
    public void testCareProviderDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setSpeciality("TestSpeciality");
        pmpmObject.setProviderName("TestProviderName");

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.careProviderDetails method
        when(dataAccessService.careProviderDetails(anyString(), anyString(), any(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.careProviderDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
    }

    @Test
    public void testPcpGroupDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setSpeciality("TestSpeciality");
        pmpmObject.setProviderName("TestProviderName");

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.pcpGroupDetails method
        when(dataAccessService.pcpGroupDetails(anyString(), anyString(), any(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.pcpGroupDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
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
        pmpmObject.setSpeciality("Pediatric");
        pmpmObject.setProviderName("The House of Operation");
        pmpmObject.setCareCategory("Psychiatrist");
        pmpmObject.setGraphType("Current vs Prior");
        pmpmObject.setViewType("PMPM");

        // Call the method under test indirectly (through public methods)
        PMPMObject validatedObject = invokeValidatePMPMObject(pmpmObject);

        // Assert that validation has processed correctly
        assertEquals("Commercial", validatedObject.getLob());
        assertEquals("LA", validatedObject.getState());
        assertEquals("2019-02", validatedObject.getStartMonth());
        assertEquals("2020-02", validatedObject.getEndMonth());
        assertEquals("Pediatric", validatedObject.getSpeciality());
        assertEquals("The House of Operation", validatedObject.getProviderName());
        assertEquals("Psychiatrist", validatedObject.getCareCategory());
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
        pmpmObject.setSpeciality("");
        pmpmObject.setProviderName("");
        pmpmObject.setCareCategory("");
        pmpmObject.setGraphType("");
        pmpmObject.setViewType("");

        // Call the method under test indirectly (through public methods)
        PMPMObject validatedObject = invokeValidatePMPMObject(pmpmObject);

        // Assert that validation has processed correctly
        assertEquals(null, validatedObject.getLob());
        assertEquals(null, validatedObject.getState());
        assertEquals(null, validatedObject.getStartMonth());
        assertEquals(null, validatedObject.getEndMonth());
        assertEquals(null, validatedObject.getSpeciality());
        assertEquals(null, validatedObject.getProviderName());
        assertEquals(null, validatedObject.getCareCategory());
        assertEquals("Target vs Actual", validatedObject.getGraphType());
        assertEquals("Expense PMPM", validatedObject.getViewType());
        // Add more assertions as needed for other fields
    }

    // Utility method to invoke private method validatePMPMObject using reflection
    private PMPMObject invokeValidatePMPMObject(PMPMObject pmpmObject) throws MyCustomException {
        try {
            // Use reflection to access the private method
            Method validateMethod = dashboardDetailsService.getClass().getDeclaredMethod("validatePMPMObject", PMPMObject.class);
            validateMethod.setAccessible(true); // Ensure method is accessible
            return (PMPMObject) validateMethod.invoke(dashboardDetailsService, pmpmObject); // Cast to PMPMObject
        } catch (Exception e) {
            throw new MyCustomException("Failed to invoke validatePMPMObject");
        }
    }

    private PMPMObject preparePMPMObject() {
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("TestLOB");
        pmpmObject.setState("TestState");
        pmpmObject.setStartMonth("202301");
        pmpmObject.setEndMonth("202312");
        return pmpmObject;
    }
}
