package com.coc.dashboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.dto.ServiceRegionBreakdown;
import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.repository.TargetPMPMRepository;
import com.coc.dashboard.util.DateFormat;

public class DataModificationServiceTest {

    @Mock
    private DateFormat dateFormat;

    @Mock
    private DataTransformationService dataTransformationService;

    @InjectMocks
    private DataModificationService dataModificationService;

    @Mock
    private TargetPMPMRepository targetPMPMRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDistinctLobStateMonths() {
        // Mock input data
        List<String> lobs = Arrays.asList("LOB1", "LOB2");
        List<String> states = Arrays.asList("State1", "State2");
        List<String> months = Arrays.asList("2019-02", "2020-02");

        // Mock DateFormat conversion
        when(dateFormat.convertIntegertoStringDateFormat(anyString())).thenAnswer(invocation -> {
            String input = invocation.getArgument(0);
            return "formatted_" + input;
        });

        // Call the method under test
        Map<String, List<String>> result = dataModificationService.distinctLobStateMonths(lobs, states, months);

        // Verify the result
        assertEquals(lobs, result.get("lob"));
        assertEquals(states, result.get("state"));
        assertEquals(Arrays.asList("formatted_2019-02", "formatted_2020-02"), result.get("months"));
    }

    @Test
    public void testSummaryPage() throws MyCustomException {
        // Mock data
        List<PMPMDTO> pmpmList = Arrays.asList(
                new PMPMDTO(50.0, "2018-02"),
                new PMPMDTO(100.0, "2019-02"),
                new PMPMDTO(150.0, "2020-02"),
                new PMPMDTO(null, "2021-02")
        );

        List<MemberViewDTO> memberViewList = Arrays.asList(
                new MemberViewDTO(null, "2018-02"),
                new MemberViewDTO(50L, "2019-02"),
                new MemberViewDTO(70L, "2020-02"),
                new MemberViewDTO(200L, "2021-02")
        );

        TargetPMPM targetPmpm1 = new TargetPMPM();
        targetPmpm1.setMonths("2019-02");targetPmpm1.setTargetPercentage(5L);
        TargetPMPM targetPmpm2 = new TargetPMPM();
        targetPmpm2.setMonths("2020-02");targetPmpm2.setTargetPercentage(8L);
        List<TargetPMPM> targetPercentage = Arrays.asList(
                targetPmpm1,targetPmpm2
        );

        // Call the method under test
        DataPair<List<ResultData>, List<TargetPMPM>, String> result = dataModificationService.summaryPage(pmpmList, memberViewList, targetPercentage);

        // Assertions
        assertEquals(4, result.getFirst().size()); // Assuming correct number of ResultData objects
        assertEquals("2018-02", result.getFirst().get(0).getMonths());
        assertEquals(50.0, result.getFirst().get(0).getTotalPricepm());
        assertEquals(0, result.getFirst().get(0).getTotalActiveMembers());
        assertEquals(2, result.getSecond().size()); // Assuming correct number of TargetPMPM objects
        assertEquals("2021-02", result.getThird()); // Assuming correct endMonth value
    }

    @Test
    public void testConvertToTargetPercentageMap() {
        // Mock data
        TargetPMPM targetPmpm1 = new TargetPMPM();
        targetPmpm1.setMonths("2019-02");targetPmpm1.setTargetPercentage(5L);
        TargetPMPM targetPmpm2 = new TargetPMPM();
        targetPmpm2.setMonths("2020-02");targetPmpm2.setTargetPercentage(8L);
        List<TargetPMPM> targetPMPMList = Arrays.asList(
                targetPmpm1, targetPmpm2
        );

        // Mock DateFormat conversion
        when(dateFormat.convertIntegertoStringDateFormat("2019-02")).thenReturn("Feb 2019");
        when(dateFormat.convertIntegertoStringDateFormat("2020-02")).thenReturn("Feb 2020");

        // Call the method under test
        Map<String, Long> result = dataModificationService.convertToTargetPercentageMap(targetPMPMList);

        // Assertions
        assertEquals(2, result.size()); // Assuming correct number of entries in the map
        assertEquals(5L, result.get("Feb 2019")); // Assuming correct conversion and mapping
    }

    @Test
    public void testServiceRegionDetails() {
        // Mock data
        List<PMPMDTO> pmpmList = Arrays.asList(
                new PMPMDTO("State2",1L),
                new PMPMDTO("State1",1L),
                new PMPMDTO("State2",1L),
                new PMPMDTO("State3", 1L),
                new PMPMDTO(null,1L)
        );

        List<MemberViewDTO> memberViewList = Arrays.asList(
                new MemberViewDTO(25L, "2018-02", "State2"),
                new MemberViewDTO(50L, "2019-02", "State1"),
                new MemberViewDTO(70L, "2020-02", "State2"),
                new MemberViewDTO(100L, "2021-02", null)
        );

        List<TopProvider> topProviders = Collections.singletonList(new TopProvider("Provider1", 100.0));
        List<TopMember> topMembers = Collections.singletonList(new TopMember(50L, 100.0));

        // Mock DataTransformationService
        when(dataTransformationService.filterServiceRegionMemberViewMap(any(), anyString(), anyString())).thenReturn(Map.of("State1", 50L, "State2", 95L));

        // Call the method under test
        Map<String, Object> result = dataModificationService.serviceRegionDetails(pmpmList, topProviders, memberViewList, topMembers, "2019-02", "2020-02");

        Map<String, ServiceRegionBreakdown> serviceRegionBreakdownMap = (Map<String, ServiceRegionBreakdown>) result.get("serviceRegionBreakdown");

        // Assertions
        assertEquals(3, serviceRegionBreakdownMap.size()); // Assuming correct breakdown map size
        assertEquals(0, serviceRegionBreakdownMap.get("State3").getTotalMembers());
        assertEquals(1, serviceRegionBreakdownMap.get("State3").getTotalProvidersCount());
        //These two assertions is to verify the group by objects assertion
        assertEquals(95, serviceRegionBreakdownMap.get("State2").getTotalMembers());
        assertEquals(2, serviceRegionBreakdownMap.get("State2").getTotalProvidersCount());
        assertEquals(1, ((List<TopMember>) result.get("topMembersPerServiceRegion")).size()); // Assuming correct top members list size
        assertEquals(1, ((List<TopProvider>) result.get("topProvidersPerServiceRegion")).size()); // Assuming correct top providers list size
    }
}
