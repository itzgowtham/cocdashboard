package com.coc.dashboard.controller;

import static org.mockito.Mockito.*;

import java.util.*;

import com.coc.dashboard.dto.FinalResult;
import com.coc.dashboard.dto.MetricData;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import com.coc.dashboard.exception.*;
import com.coc.dashboard.model.*;
import com.coc.dashboard.service.*;

public class DashboardControllerTest {

	@Mock
	private DashboardService dashboardService;

	@Mock
	private DashboardDetailsService dashboardDetailsService;

	@InjectMocks
	private DashboardController dashboardController;

	private PMPMObject mockPMPMObject;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockPMPMObject = new PMPMObject(); // Create a mock object as needed
	}

	@Test
	public void testSummary() throws MyCustomException {
		when(dashboardService.summary(any(PMPMObject.class))).thenReturn(new HashMap<>());

		ResponseEntity<Map<String, Object>> response = dashboardController.summary(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(dashboardService).summary(mockPMPMObject);
	}

	@Test
	public void testLandingPage() throws MyCustomException {
		FinalResult expectedResult = new FinalResult(); // Create expected result

		when(dashboardService.landingPage()).thenReturn(expectedResult);

		ResponseEntity<FinalResult> response = dashboardController.landingPage();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).landingPage();
	}

	@Test
	public void testDistinctLobStateMonths() throws MyCustomException {
		Map<String, List<String>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.distinctLobStateMonths()).thenReturn(expectedResult);

		ResponseEntity<Map<String, List<String>>> response = dashboardController.distinctLobStateMonths();

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).distinctLobStateMonths();
	}

	@Test
	public void testCareCategory() throws MyCustomException {
		Map<String, Map<String, MetricData>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.careCategory(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Map<String, MetricData>>> response = dashboardController.careCategory(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).careCategory(mockPMPMObject);
	}

	@Test
	public void testServiceRegion() throws MyCustomException {
		Map<String, Map<String, MetricData>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.serviceRegion(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Map<String, MetricData>>> response = dashboardController.serviceRegion(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).serviceRegion(mockPMPMObject);
	}

	@Test
	public void testProviderSpeciality() throws MyCustomException {
		Map<String, Map<String, MetricData>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.providerSpeciality(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Map<String, MetricData>>> response = dashboardController.providerSpeciality(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).providerSpeciality(mockPMPMObject);
	}

	@Test
	public void testCareProvider() throws MyCustomException {
		Map<String, Map<String, MetricData>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.careProvider(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Map<String, MetricData>>> response = dashboardController.careProvider(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).careProvider(mockPMPMObject);
	}

	@Test
	public void testPcpGroup() throws MyCustomException {
		Map<String, MetricData> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.pcpGroup(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, MetricData>> response = dashboardController.pcpGroup(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).pcpGroup(mockPMPMObject);
	}

	@Test
	public void testForecast() throws MyCustomException {
		Map<String, List<Object>> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardService.forecast(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, List<Object>>> response = dashboardController.forcast(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardService).forecast(mockPMPMObject);
	}

	@Test
	public void testCareCategoryDetails() throws MyCustomException {
		Map<String, Object> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardDetailsService.careCategoryDetails(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Object>> response = dashboardController.careCategoryDetails(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardDetailsService).careCategoryDetails(mockPMPMObject);
	}

	@Test
	public void testServiceRegionDetails() throws MyCustomException {
		Map<String, Object> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardDetailsService.serviceRegionDetails(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Object>> response = dashboardController.serviceRegionDetails(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardDetailsService).serviceRegionDetails(mockPMPMObject);
	}

	@Test
	public void testProviderSpecialityDetails() throws MyCustomException {
		Map<String, Object> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardDetailsService.providerSpecialityDetails(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Object>> response = dashboardController.providerSpecialityDetails(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardDetailsService).providerSpecialityDetails(mockPMPMObject);
	}

	@Test
	public void testCareProviderDetails() throws MyCustomException {
		Map<String, Object> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardDetailsService.careProviderDetails(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Object>> response = dashboardController.careProviderDetails(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardDetailsService).careProviderDetails(mockPMPMObject);
	}

	@Test
	public void testPcpGroupDetails() throws MyCustomException {
		Map<String, Object> expectedResult = new HashMap<>(); // Create expected result

		when(dashboardDetailsService.pcpGroupDetails(any(PMPMObject.class))).thenReturn(expectedResult);

		ResponseEntity<Map<String, Object>> response = dashboardController.pcpGroupDetails(mockPMPMObject);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(expectedResult, response.getBody());
		verify(dashboardDetailsService).pcpGroupDetails(mockPMPMObject);
	}
}
