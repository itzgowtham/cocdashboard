package com.coc.dashboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.model.DataPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.coc.dashboard.dto.*;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.repository.TargetPMPMRepository;

public class DataAccessServiceTest {

    @InjectMocks
    private DataAccessService dataAccessService;

    @Mock
    private PMPMDataAccessService pmpmDataAccessService;

    @Mock
    private MemberViewDataAccessService memberViewDataAccessService;

    @Mock
    private TargetPMPMRepository targetPMPMRepository;

    @Mock
    private DataModificationService dataModificationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDistinctLobStateMonths() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<String> lobList = Arrays.asList("Lob1", "Lob2");
        List<String> stateList = Arrays.asList("State1", "State2");
        List<String> monthList = Arrays.asList("Jan", "Feb");
        Map<String, List<String>> mapData = Map.of("lob", lobList, "state", stateList, "month", monthList);
        when(pmpmDataAccessService.distinctLob()).thenReturn(CompletableFuture.completedFuture(lobList));
        when(pmpmDataAccessService.distinctState()).thenReturn(CompletableFuture.completedFuture(stateList));
        when(pmpmDataAccessService.distinctMonths()).thenReturn(CompletableFuture.completedFuture(monthList));
        when(dataModificationService.distinctLobStateMonths(anyList(), anyList(), anyList())).thenReturn(mapData);

        // Test method invocation
        Map<String, List<String>> result = dataAccessService.distinctLobStateMonths();

        // Assert the result
        assertEquals(mapData.size(), result.size());
        assertEquals(mapData, result);
    }

    @Test
    void testKpiMetrics() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = List.of(new PMPMDTO());
        List<MemberViewDTO> memberViewList = List.of(new MemberViewDTO());
        List<TargetPMPM> targetPercentage = List.of(new TargetPMPM());
        DataPair<List<ResultData>, List<TargetPMPM>, String> finalData = new DataPair<>(List.of(new ResultData()), targetPercentage, "endMonth");
        when(pmpmDataAccessService.kpiMetrics(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(memberViewDataAccessService.kpiMetrics(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(memberViewList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);
        when(dataModificationService.summaryPage(anyList(), anyList(), anyList())).thenReturn(finalData);

        // Test method invocation
        DataPair<List<ResultData>, List<TargetPMPM>, String> result = dataAccessService.kpiMetrics("Lob1", "State1");

        // Assert the result
        assertEquals(finalData.getFirst(), result.getFirst());
        assertEquals(finalData.getSecond(), result.getSecond());
        assertEquals(finalData.getThird(), result.getThird());
    }

    @Test
    void testCareCategory() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = new ArrayList<>();
        List<TargetPMPM> targetPercentage = new ArrayList<>();
        when(pmpmDataAccessService.findCareCategory(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);

        // Test method invocation
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> result = dataAccessService.careCategory("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(targetPercentage, result.getSecond());
        assertEquals(null, result.getThird());
    }

    @Test
    void testServiceRegion() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = new ArrayList<>();
        List<MemberViewDTO> memberViewList = new ArrayList<>();
        List<TargetPMPM> targetPercentage = new ArrayList<>();
        when(pmpmDataAccessService.serviceRegion(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(memberViewDataAccessService.serviceRegion(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(memberViewList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);

        // Test method invocation
        DataPair<List<PMPMDTO>, List<MemberViewDTO>, List<TargetPMPM>> result = dataAccessService.serviceRegion("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(memberViewList, result.getSecond());
        assertEquals(targetPercentage, result.getThird());
    }

    @Test
    void testProviderSpeciality() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = new ArrayList<>();
        List<TargetPMPM> targetPercentage = new ArrayList<>();
        when(pmpmDataAccessService.findProviderSpeciality(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);

        // Test method invocation
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> result = dataAccessService.providerSpeciality("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(targetPercentage, result.getSecond());
        assertEquals(null, result.getThird());
    }

    @Test
    void testCareProvider() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = new ArrayList<>();
        List<TargetPMPM> targetPercentage = new ArrayList<>();
        when(pmpmDataAccessService.findCareProvider(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);
        when(pmpmDataAccessService.distinctState()).thenReturn(CompletableFuture.completedFuture(List.of("State1", "State2")));

        // Test method invocation
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> result = dataAccessService.careProvider("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(targetPercentage, result.getSecond());
        assertEquals(null, result.getThird());

        result = dataAccessService.careProvider("Lob1", null);
        assertNotNull(result);
    }

    @Test
    void testPcpGroup() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = new ArrayList<>();
        List<TargetPMPM> targetPercentage = new ArrayList<>();
        when(pmpmDataAccessService.findPcpGroup(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(targetPMPMRepository.findAll()).thenReturn(targetPercentage);

        // Test method invocation
        DataPair<List<PMPMDTO>, List<TargetPMPM>, Object> result = dataAccessService.pcpGroup("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(targetPercentage, result.getSecond());
        assertEquals(null, result.getThird());
    }

    @Test
    void testForecast() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<Forecast_PMPM> pmpmList = new ArrayList<>();
        List<Forecast_ActiveMembership> memberList = new ArrayList<>();
        when(pmpmDataAccessService.findForecast(any(), any())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(memberViewDataAccessService.findForecast(any(), any())).thenReturn(CompletableFuture.completedFuture(memberList));

        // Test method invocation
        DataPair<List<Forecast_PMPM>, List<Forecast_ActiveMembership>, Object> result = dataAccessService.forecast("Lob1", "State1");

        // Assert the result
        assertEquals(pmpmList, result.getFirst());
        assertEquals(memberList, result.getSecond());
        assertNull(result.getThird());
    }

    @Test
    void testCareCategoryDetails() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<TopProvider> topProviders = List.of(new TopProvider());
        List<TopMember> topMembers = List.of(new TopMember());
        List<String> distinctCategory = List.of("Category1","Category2");
        when(pmpmDataAccessService.findCareCategoryTopProviders(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProviders));
        when(memberViewDataAccessService.findCareCategoryTopMembers(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembers));
        when(pmpmDataAccessService.findCareCategoryProvidersCount(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(5L));
        when(memberViewDataAccessService.findCareCategoryMembersCount(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(10L));
        when(pmpmDataAccessService.distinctCategory()).thenReturn(CompletableFuture.completedFuture(distinctCategory));

        // Test method invocation
        Map<String, Object> result = dataAccessService.careCategoryDetails("Lob1", "State1", "Jan", "Feb", "Category");

        // Assert the result
        assertEquals(topMembers, result.get("topMembersPerCareCategory"));
        assertEquals(topProviders, result.get("topProvidersPerCareCategory"));
        assertEquals(10L, result.get("membersCount"));
        assertEquals(5L, result.get("providersCount"));
        assertEquals(Arrays.asList("Category1", "Category2"), result.get("distinctCategory"));
    }

    @Test
    void testServiceRegionDetails() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<PMPMDTO> pmpmList = List.of(new PMPMDTO());
        List<TopProvider> topProviders = List.of(new TopProvider());
        List<TopMember> topMembers = List.of(new TopMember());
        List<MemberViewDTO> memberViewList = List.of(new MemberViewDTO());
        List<ServiceRegionBreakdown> serviceRegionBreakdown = List.of(new ServiceRegionBreakdown());
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("serviceRegionBreakdown", serviceRegionBreakdown);
        mapData.put("topMembersPerServiceRegion", topMembers);
        mapData.put("topProvidersPerServiceRegion", topProviders);
        when(pmpmDataAccessService.serviceRegionDetails(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(pmpmList));
        when(pmpmDataAccessService.findTopProviders(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProviders));
        when(memberViewDataAccessService.findServiceRegionTopMembers(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembers));
        when(memberViewDataAccessService.serviceRegion(any(), any())).thenReturn(CompletableFuture.completedFuture(memberViewList));
        when(dataModificationService.serviceRegionDetails(anyList(), anyList(), anyList(), anyList(), any(), any())).thenReturn(mapData);

        // Test method invocation
        Map<String, Object> result = dataAccessService.serviceRegionDetails("Lob1", "State1", "Jan", "Feb");

        // Assert the result
        assertEquals(serviceRegionBreakdown, result.get("serviceRegionBreakdown"));
        assertEquals(topProviders, result.get("topProvidersPerServiceRegion"));
        assertEquals(topMembers, result.get("topMembersPerServiceRegion"));
    }

    @Test
    void testProviderSpecialityDetails() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<TopSpeciality> topSpeciality = List.of(new TopSpeciality());
        List<TopProvider> topProvidersBySpeciality = List.of(new TopProvider());
        List<TopMemberSpeciality> topMembers = List.of(new TopMemberSpeciality());
        List<TopMember> topMembersBySpeciality = List.of(new TopMember());
        List<String> distinctSpeciality = Arrays.asList("Speciality1", "Speciality2");
        when(pmpmDataAccessService.findTopSpecialities(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topSpeciality));
        when(pmpmDataAccessService.findTopProvidersBySpeciality(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProvidersBySpeciality));
        when(memberViewDataAccessService.findTopProviderMembers(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembers));
        when(memberViewDataAccessService.findTopMembersBySpeciality(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembersBySpeciality));
        when(pmpmDataAccessService.distinctSpeciality()).thenReturn(CompletableFuture.completedFuture(distinctSpeciality));

        // Test method invocation
        Map<String, Object> result = dataAccessService.providerSpecialityDetails("Lob1", "State1", "Jan", "Feb", "Speciality");

        // Assert the result
        assertEquals(topSpeciality, result.get("topSpecialitiesByCost"));
        assertEquals(topProvidersBySpeciality, result.get("topProvidersPerSpeciality"));
        assertEquals(topMembers, result.get("membersPerSpeciality"));
        assertEquals(topMembersBySpeciality, result.get("topMembersPerSpeciality"));
        assertEquals(distinctSpeciality, result.get("distinctSpeciality"));
    }

    @Test
    void testCareProviderDetails() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<TopProvider> topProviders = List.of(new TopProvider());
        List<TopProvider> topProvidersBySpeciality = List.of(new TopProvider());
        List<TopMemberProvider> topMembersByProvider = List.of(new TopMemberProvider());
        List<TopMember> topMembersBySpeciality = List.of(new TopMember());
        List<String> distinctSpeciality = Arrays.asList("Speciality1", "Speciality2");
        List<String> distinctProvider = Arrays.asList("Provider1", "Provider2");
        when(pmpmDataAccessService.findTopProviders(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProviders));
        when(pmpmDataAccessService.findTopProvidersBySpeciality(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProvidersBySpeciality));
        when(memberViewDataAccessService.findTopMembersByProvider(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembersByProvider));
        when(memberViewDataAccessService.findTopMembersByProvider(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembersBySpeciality));
        when(pmpmDataAccessService.distinctSpeciality()).thenReturn(CompletableFuture.completedFuture(distinctSpeciality));
        when(pmpmDataAccessService.distinctProvider(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(distinctProvider));

        // Test method invocation
        Map<String, Object> result = dataAccessService.careProviderDetails("Lob1", "State1", "Jan", "Feb", "Speciality", "Provider");

        // Assert the result
        assertEquals(topMembersByProvider, result.get("topMembersPerProvider"));
        assertEquals(topProviders, result.get("top10ProvidersByCost"));
        assertEquals(topMembersBySpeciality, result.get("top10MembersByCostForEachSpeciality"));
        assertEquals(topProvidersBySpeciality, result.get("top10ProvidersByCostForEachSpeciality"));
        assertEquals(distinctSpeciality, result.get("distinctSpeciality"));
        assertEquals(distinctProvider, result.get("distinctProvider"));
    }

    @Test
    void testPcpGroupDetails() throws MyCustomException {
        // Mocking CompletableFuture responses
        List<TopProvider> topSpeciality = new ArrayList<>();
        List<TopProvider> topProvidersBySpeciality = new ArrayList<>();
        List<TopMemberProvider> topMembers = new ArrayList<>();
        List<TopMember> topMembersBySpeciality = new ArrayList<>();
        when(pmpmDataAccessService.findPcpTopSpeciality(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topSpeciality));
        when(pmpmDataAccessService.findPcpTopProvidersBySpeciality(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topProvidersBySpeciality));
        when(memberViewDataAccessService.findPcpGroupTopMembers(any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembers));
        when(memberViewDataAccessService.findPcpGroupTopMembersBySpeciality(any(), any(), any(), any(), any())).thenReturn(CompletableFuture.completedFuture(topMembersBySpeciality));
        when(pmpmDataAccessService.distinctSpecialityPcp()).thenReturn(CompletableFuture.completedFuture(Arrays.asList("Speciality1", "Speciality2")));
        when(pmpmDataAccessService.distinctProviderPcp(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(Arrays.asList("Provider1", "Provider2")));

        // Test method invocation
        Map<String, Object> result = dataAccessService.pcpGroupDetails("Lob1", "State1", "Jan", "Feb", "Speciality", "Provider");

        // Assert the result
        assertEquals(topSpeciality, result.get("topPcpByCost"));
        assertEquals(topProvidersBySpeciality, result.get("topPcpByCostForEachSpeciality"));
        assertEquals(topMembers, result.get("membersPerPcpGroup"));
        assertEquals(topMembersBySpeciality, result.get("topMembersByCostForEachPcp"));
        assertEquals(Arrays.asList("Speciality1", "Speciality2"), result.get("distinctSpeciality"));
        assertEquals(Arrays.asList("Provider1", "Provider2"), result.get("distinctProvider"));
    }

    @Test
    void exception_scenarios() throws MyCustomException {
        // Assert the result
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
            dataAccessService.distinctLobStateMonths()).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
            dataAccessService.kpiMetrics("TestLOB", "TestState")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
            dataAccessService.careCategory("Lob1", "State1")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.serviceRegion("Lob1", "State1")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.providerSpeciality("Lob1", "State1")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.careProvider("TestLOB", "TestState")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.pcpGroup("TestLOB", "TestState")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.forecast("TestLOB", "TestState")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.careCategoryDetails("Lob1", "State1", "Jan", "Feb", "Category")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.serviceRegionDetails("Lob1", "State1", "Jan", "Feb")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.providerSpecialityDetails("Lob1", "State1", "Jan", "Feb", "Speciality")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.careProviderDetails("Lob1", "State1", "Jan", "Feb", "Speciality", "Provider")).getMessage());
        assertEquals("Exception occurred while reading the data from db", assertThrows(MyCustomException.class, () ->
                dataAccessService.pcpGroupDetails("Lob1", "State1", "Jan", "Feb", "Speciality", "Provider")).getMessage());
    }
}
