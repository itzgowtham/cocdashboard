package com.coc.dashboard.service;

import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.PMPMObject;
import com.coc.dashboard.util.DateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DashboardDetailsServiceTest {

    @Spy
    private DateFormat dateFormat;

    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private DashboardDetailsService dashboardDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private PMPMObject preparePMPMObject() {
        PMPMObject pmpmObject = new PMPMObject();
        pmpmObject.setLob("TestLOB ");
        pmpmObject.setState("TestState ");
        pmpmObject.setStartMonth("Jan 2023");
        pmpmObject.setEndMonth("Dec 2023");
        return pmpmObject;
    }

    @Test
    public void testCareCategoryDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setCareCategory("");

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.careCategoryDetails method
        when(dataAccessService.careCategoryDetails(anyString(), anyString(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.careCategoryDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
        verify(dataAccessService, times(1)).careCategoryDetails("TestLOB", "TestState", "2023-01", "2023-12", null);
    }

    @Test
    public void testServiceRegionDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setStartMonth(null);
        pmpmObject.setEndMonth(null);

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.serviceRegionDetails method
        when(dataAccessService.serviceRegionDetails(anyString(), anyString(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.serviceRegionDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
        verify(dataAccessService, times(1)).serviceRegionDetails("TestLOB", "TestState", null, null);

    }

    @Test
    public void testProviderSpecialityDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.providerSpecialityDetails method
        when(dataAccessService.providerSpecialityDetails(anyString(), anyString(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.providerSpecialityDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
        verify(dataAccessService, times(1)).providerSpecialityDetails("TestLOB", "TestState", "2023-01", "2023-12", null);
    }

    @Test
    public void testCareProviderDetails() throws MyCustomException {
        PMPMObject pmpmObject = preparePMPMObject();
        pmpmObject.setSpeciality("");
        pmpmObject.setProviderName("");

        Map<String, Object> expectedMapData = Collections.singletonMap("data", "mockedData");

        // Mocking behavior of dataAccessService.careProviderDetails method
        when(dataAccessService.careProviderDetails(anyString(), anyString(), any(), any(), any(), any()))
                .thenReturn(expectedMapData);

        // Call the service method under test
        Map<String, Object> result = dashboardDetailsService.careProviderDetails(pmpmObject);

        // Assert that the result matches the expected data
        assertEquals(expectedMapData, result);
        verify(dataAccessService, times(1)).careProviderDetails("TestLOB", "TestState", "2023-01", "2023-12", null, null);
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
        verify(dataAccessService, times(1)).pcpGroupDetails("TestLOB", "TestState", "2023-01", "2023-12", "TestSpeciality", "TestProviderName");
    }
}
