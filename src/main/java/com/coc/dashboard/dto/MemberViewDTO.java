package com.coc.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberViewDTO implements Cloneable {

	private Long totalActiveMembers;
    private String months;
    private String state;
    
	public MemberViewDTO(Long totalActiveMembers, String months) {
		super();
		this.totalActiveMembers = totalActiveMembers;
		this.months = months;
	}
	
	@Override
    public MemberViewDTO clone() {
        try {
            return (MemberViewDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
