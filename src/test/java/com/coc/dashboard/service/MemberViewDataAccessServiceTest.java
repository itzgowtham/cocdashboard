package com.coc.dashboard.service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopMemberProvider;
import com.coc.dashboard.dto.TopMemberSpeciality;
import com.coc.dashboard.entity.Forecast_ActiveMembership;
import com.coc.dashboard.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class MemberViewDataAccessServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ForecastMemberRepository forecastRepository;

    @Mock
    private CareCategoryDetailsRepository careCategoryDetailsRepository;

    @Mock
    private ServiceAreaRegionDetailsRepository serviceAreaRegionDetailsRepository;

    @Mock
    private ProviderSpecialityDetailsRepository providerSpecialityDetailsRepository;

    @Mock
    private PcpGroupDetailsRepository pcpGroupDetailsRepository;

    @InjectMocks
    private MemberViewDataAccessService memberViewDataAccessService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testKpiMetrics() throws ExecutionException, InterruptedException {
        List<MemberViewDTO> mockResult = Collections.singletonList(new MemberViewDTO());
        when(memberRepository.findSummaryRecords(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<MemberViewDTO>> futureResult = memberViewDataAccessService.kpiMetrics("lob", "state");

        List<MemberViewDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testServiceRegion() throws ExecutionException, InterruptedException {
        List<MemberViewDTO> mockResult = Collections.singletonList(new MemberViewDTO());
        when(memberRepository.findServiceRegion(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<MemberViewDTO>> futureResult = memberViewDataAccessService.serviceRegion("lob", "state");

        List<MemberViewDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindForecast() throws ExecutionException, InterruptedException {
        List<Forecast_ActiveMembership> mockResult = Collections.singletonList(new Forecast_ActiveMembership());
        when(forecastRepository.findByLobAndStatecode(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<Forecast_ActiveMembership>> futureResult = memberViewDataAccessService.findForecast("lob", "state");

        List<Forecast_ActiveMembership> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareCategoryTopMembers() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(careCategoryDetailsRepository.findTopMembersByCategory(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findCareCategoryTopMembers("lob", "state", "startMonth", "endMonth", "category");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindServiceRegionTopMembers() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(serviceAreaRegionDetailsRepository.findTopMembers(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findServiceRegionTopMembers("lob", "state", "startMonth", "endMonth");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopMembers() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(providerSpecialityDetailsRepository.findTopMembers(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findTopMembers("lob", "state", "startMonth", "endMonth");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopProviderMembers() throws ExecutionException, InterruptedException {
        List<TopMemberSpeciality> mockResult = Collections.singletonList(new TopMemberSpeciality());
        when(providerSpecialityDetailsRepository.findTopProviderMembers(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMemberSpeciality>> futureResult = memberViewDataAccessService.findTopProviderMembers("lob", "state", "startMonth", "endMonth");

        List<TopMemberSpeciality> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopMembersBySpeciality() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(providerSpecialityDetailsRepository.findTopMembersBySpeciality(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findTopMembersBySpeciality("lob", "state", "startMonth", "endMonth", "speciality");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopMembersByProvider() throws ExecutionException, InterruptedException {
        List<TopMemberProvider> mockResult = Collections.singletonList(new TopMemberProvider());
        when(providerSpecialityDetailsRepository.findTopMembersByProvider(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMemberProvider>> futureResult = memberViewDataAccessService.findTopMembersByProvider("lob", "state", "startMonth", "endMonth");

        List<TopMemberProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopMembersByProviderWithName() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(providerSpecialityDetailsRepository.findTopMembersByProvider(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findTopMembersByProvider("lob", "state", "startMonth", "endMonth", "providerName");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindPcpGroupTopMembers() throws ExecutionException, InterruptedException {
        List<TopMemberProvider> mockResult = Collections.singletonList(new TopMemberProvider());
        when(pcpGroupDetailsRepository.findTopMembers(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMemberProvider>> futureResult = memberViewDataAccessService.findPcpGroupTopMembers("lob", "state", "startMonth", "endMonth");

        List<TopMemberProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindPcpGroupTopMembersBySpeciality() throws ExecutionException, InterruptedException {
        List<TopMember> mockResult = Collections.singletonList(new TopMember());
        when(pcpGroupDetailsRepository.findTopMembersBySpeciality(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopMember>> futureResult = memberViewDataAccessService.findPcpGroupTopMembersBySpeciality("lob", "state", "startMonth", "endMonth", "providerName");

        List<TopMember> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareCategoryMembersCount() throws ExecutionException, InterruptedException {
        Long mockResult = 10L;
        when(careCategoryDetailsRepository.findMembersCount(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<Long> futureResult = memberViewDataAccessService.findCareCategoryMembersCount("lob", "state", "startMonth", "endMonth", "category");

        Long result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindMembersCount() throws ExecutionException, InterruptedException {
        Long mockResult = 20L;
        when(providerSpecialityDetailsRepository.findMembersCount(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<Long> futureResult = memberViewDataAccessService.findMembersCount("lob", "state", "startMonth", "endMonth", "speciality");

        Long result = futureResult.get();
        assertEquals(mockResult, result);
    }
}
