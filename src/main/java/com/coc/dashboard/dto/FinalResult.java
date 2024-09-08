package com.coc.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalResult {

	private Long memberMonths;
    private Long endingMembers;
    private Double pmpm;
    private Long targetMemberMonths;
    private Long targetEndingMembers;
    private Double targetPmpm;
    private Double memberMonthsPercentageChange;
    private Double endingMembersPercentageChange;
    private Double pmpmPercentageChange;
}
