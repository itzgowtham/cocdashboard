package com.coc.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultData implements Cloneable{
	
    private Long totalActiveMembers;
    private Double totalPricepm;
    private String months;
    
    @Override
    public ResultData clone() {
        try {
            return (ResultData) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

