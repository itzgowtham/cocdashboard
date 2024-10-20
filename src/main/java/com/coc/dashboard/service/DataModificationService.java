package com.coc.dashboard.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.coc.dashboard.dto.MemberViewDTO;
import com.coc.dashboard.dto.PMPMDTO;
import com.coc.dashboard.dto.ResultData;
import com.coc.dashboard.dto.ServiceRegionBreakdown;
import com.coc.dashboard.dto.TopMember;
import com.coc.dashboard.dto.TopProvider;
import com.coc.dashboard.entity.TargetPMPM;
import com.coc.dashboard.exception.MyCustomException;
import com.coc.dashboard.model.DataPair;
import com.coc.dashboard.util.DateFormat;

import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DataModificationService {

    private DateFormat dateFormat;
    private DataTransformationService dataTransformationService;

    public Map<String, List<String>> distinctLobStateMonths(List<String> lobs, List<String> states,
                                                            List<String> months) {
        Map<String, List<String>> data = new HashMap<>();
        months = months.stream().map(val -> dateFormat.convertIntegertoStringDateFormat(val))
                .collect(Collectors.toList());
        data.put("lob", lobs);
        data.put("state", states);
        data.put("months", months);
        return data;
    }

    public DataPair<List<ResultData>, List<TargetPMPM>, String> summaryPage(List<PMPMDTO> pmpmList,
                                                                            List<MemberViewDTO> memberViewList, List<TargetPMPM> targetPercentage) throws MyCustomException {
        Map<String, Double> pricePmMap = pmpmList.stream().collect(
                Collectors.toMap(PMPMDTO::getMonths, pm -> pm.getTotalPricepm() != null ? pm.getTotalPricepm() : 0.0));
        List<ResultData> resultObjs = memberViewList.stream().map(mv -> new ResultData((mv.getTotalActiveMembers() != null) ? mv.getTotalActiveMembers() : 0L,
                pricePmMap.getOrDefault(mv.getMonths(), 0.0), mv.getMonths())
        ).collect(Collectors.toList());
        String endMonth = pmpmList.stream().map(PMPMDTO::getMonths).max(Comparator.naturalOrder()).orElse(null);
        return new DataPair<>(resultObjs, targetPercentage, endMonth);

    }

    public Map<String, Long> convertToTargetPercentageMap(List<TargetPMPM> targetPMPM) {
        return targetPMPM.stream().collect(Collectors.toMap(
                t -> dateFormat.convertIntegertoStringDateFormat(t.getMonths()), TargetPMPM::getTargetPercentage));
    }

    public Map<String, Object> serviceRegionDetails(List<PMPMDTO> pmpmList, List<TopProvider> topProviders,
                                                    List<MemberViewDTO> memberViewList, List<TopMember> topMembers, String startMonth, String endMonth) {
        log.info("Inside DataModificationService.serviceRegionDetails() method");
        Map<String, Object> mapData = new HashMap<>();
        Map<String, Long> memberViewMap = dataTransformationService.filterServiceRegionMemberViewMap(memberViewList,
                startMonth, endMonth);
        Map<String, ServiceRegionBreakdown> regionBreakdown = pmpmList.stream().filter(val -> StringUtils.isNotEmpty(val.getState()))
                .collect(Collectors.groupingBy(PMPMDTO::getState,
                        Collectors.collectingAndThen(Collectors.reducing((p1, p2) -> new PMPMDTO(p1.getState(), p1.getProviderCount() + p2.getProviderCount())
                        ), result -> {
                            PMPMDTO reducedPMPMDTO = result.orElse(new PMPMDTO(null, 0L));
                            return new ServiceRegionBreakdown(memberViewMap.getOrDefault(reducedPMPMDTO.getState(), 0L),
                                    reducedPMPMDTO.getProviderCount());
                        })));
        mapData.put("serviceRegionBreakdown", regionBreakdown);
        mapData.put("topMembersPerServiceRegion", topMembers);
        mapData.put("topProvidersPerServiceRegion", topProviders);
        log.info("Exiting DataModificationService.serviceRegionDetails() method");
        return mapData;
    }

}
