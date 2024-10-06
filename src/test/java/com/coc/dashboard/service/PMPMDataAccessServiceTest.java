package com.coc.dashboard.service;

import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.dto.TopSpeciality;
import com.coc.dashboard.entity.Forecast_PMPM;
import com.coc.dashboard.repository.ForecastPMPMRepository;
import com.coc.dashboard.repository.PMPMCategoryRepository;
import com.coc.dashboard.repository.PMPMPcpRepository;
import com.coc.dashboard.repository.PMPMRepository;
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

public class PMPMDataAccessServiceTest {

    @Mock
    private PMPMRepository pmpmRepository;

    @Mock
    private PMPMCategoryRepository pmpmCategoryRepository;

    @Mock
    private PMPMPcpRepository pmpmPcpRepository;

    @Mock
    private ForecastPMPMRepository forecastRepository;

    @InjectMocks
    private PMPMDataAccessService pmpmDataAccessService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDistinctLob() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("LOB1");
        when(pmpmRepository.findDistinctLob()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctLob();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctState() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("State1");
        when(pmpmRepository.findDistinctState()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctState();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctMonths() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("2023-01");
        when(pmpmRepository.findDistinctMonths()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctMonths();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testKpiMetrics() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmRepository.findSummaryRecords(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.kpiMetrics("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testServiceRegion() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmRepository.findServiceRegion(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.serviceRegion("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareCategory() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmCategoryRepository.findCareCategory(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.findCareCategory("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindProviderSpeciality() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmRepository.findProviderSpeciality(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.findProviderSpeciality("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareProvider() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmRepository.findCareProviders(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.findCareProvider("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindPcpGroup() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmPcpRepository.findPcpGroup(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.findPcpGroup("lob", "state");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindForecast() throws ExecutionException, InterruptedException {
        List<Forecast_PMPM> mockResult = Collections.singletonList(new Forecast_PMPM());
        when(forecastRepository.findByLobAndStatecode(anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<Forecast_PMPM>> futureResult = pmpmDataAccessService.findForecast("lob", "state");

        List<Forecast_PMPM> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopSpecialities() throws ExecutionException, InterruptedException {
        List<TopSpeciality> mockResult = Collections.singletonList(new TopSpeciality());
        when(pmpmRepository.findTopSpecialities(anyString(), anyString(), anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<TopSpeciality>> futureResult = pmpmDataAccessService.findTopSpecialities("lob", "state", "startMonth", "endMonth");

        List<TopSpeciality> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopProviders() throws ExecutionException, InterruptedException {
        List<TopProvider> mockResult = Collections.singletonList(new TopProvider());
        when(pmpmRepository.findTopProviders(anyString(), anyString(), anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<TopProvider>> futureResult = pmpmDataAccessService.findTopProviders("lob", "state", "startMonth", "endMonth");

        List<TopProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindTopProvidersBySpeciality() throws ExecutionException, InterruptedException {
        List<TopProvider> mockResult = Collections.singletonList(new TopProvider());
        when(pmpmRepository.findTopProvidersBySpeciality(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopProvider>> futureResult = pmpmDataAccessService.findTopProvidersBySpeciality("lob", "state", "startMonth", "endMonth", "speciality");

        List<TopProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareCategoryTopProviders() throws ExecutionException, InterruptedException {
        List<TopProvider> mockResult = Collections.singletonList(new TopProvider());
        when(pmpmCategoryRepository.findTopProvidersByCategory(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopProvider>> futureResult = pmpmDataAccessService.findCareCategoryTopProviders("lob", "state", "startMonth", "endMonth", "category");

        List<TopProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindPcpTopSpeciality() throws ExecutionException, InterruptedException {
        List<TopProvider> mockResult = Collections.singletonList(new TopProvider());
        when(pmpmPcpRepository.findTopProviders(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopProvider>> futureResult = pmpmDataAccessService.findPcpTopSpeciality("lob", "state", "startMonth", "endMonth");

        List<TopProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindPcpTopProvidersBySpeciality() throws ExecutionException, InterruptedException {
        List<TopProvider> mockResult = Collections.singletonList(new TopProvider());
        when(pmpmPcpRepository.findTopProvidersBySpeciality(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<List<TopProvider>> futureResult = pmpmDataAccessService.findPcpTopProvidersBySpeciality("lob", "state", "startMonth", "endMonth", "speciality");

        List<TopProvider> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctCategory() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("Category1");
        when(pmpmCategoryRepository.findDistinctCategories()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctCategory();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctSpeciality() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("Speciality1");
        when(pmpmRepository.findDistinctSpecialities()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctSpeciality();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctProvider() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("Provider1");
        when(pmpmRepository.findDistinctProviders(anyString(), anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctProvider("lob", "state", "endMonth");

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctSpecialityPcp() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("SpecialityPcp1");
        when(pmpmPcpRepository.findDistinctSpecialities()).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctSpecialityPcp();

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testDistinctProviderPcp() throws ExecutionException, InterruptedException {
        List<String> mockResult = Collections.singletonList("ProviderPcp1");
        when(pmpmPcpRepository.findDistinctProviders(anyString(), anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<String>> futureResult = pmpmDataAccessService.distinctProviderPcp("lob", "state", "endMonth");

        List<String> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testServiceRegionDetails() throws ExecutionException, InterruptedException {
        List<PMPMDTO> mockResult = Collections.singletonList(new PMPMDTO());
        when(pmpmRepository.findServiceRegionDetails(anyString(), anyString(), anyString(), anyString())).thenReturn(mockResult);

        CompletableFuture<List<PMPMDTO>> futureResult = pmpmDataAccessService.serviceRegionDetails("lob", "state", "startMonth", "endMonth");

        List<PMPMDTO> result = futureResult.get();
        assertEquals(mockResult, result);
    }

    @Test
    public void testFindCareCategoryProvidersCount() throws ExecutionException, InterruptedException {
        long mockResult = 10L;
        when(pmpmCategoryRepository.findProvidersCount(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResult);

        CompletableFuture<Long> futureResult = pmpmDataAccessService.findCareCategoryProvidersCount("lob", "state", "startMonth", "endMonth", "category");

        long result = futureResult.get();
        assertEquals(mockResult, result);
    }
}
