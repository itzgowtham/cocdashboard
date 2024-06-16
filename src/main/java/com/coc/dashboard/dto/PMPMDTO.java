package com.coc.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PMPMDTO implements Cloneable {

    private Double totalPricepm;
    private String months;
    private String speciality;
    private Long memberCount;
    private String patientType;
    private String state;
    private String provider;
    private Long activeMembers;
    private Double pmpm;
    private Long providerCount;
    
	public PMPMDTO(Double totalPricepm, String months) {
		super();
		this.totalPricepm = totalPricepm;
		this.months = months;
	}
	
	public PMPMDTO(String state, Long providerCount) {
		super();
		this.state = state;
		this.providerCount = providerCount;
	}
	
	public PMPMDTO(Double totalPricepm, String months, String state) {
		super();
		this.totalPricepm = totalPricepm;
		this.months = months;
		this.state = state;
	}
	
	public PMPMDTO(String months, Double totalPricepm, String provider) {
		super();
		this.months = months;
		this.totalPricepm = totalPricepm;
		this.provider = provider;
	}
	
	public PMPMDTO(Double totalPricepm, Long memberCount, String months, String speciality, String patientType) {
		super();
		this.totalPricepm = totalPricepm;
		this.memberCount = memberCount;
		this.months = months;
		this.speciality = speciality;
		this.patientType = patientType;
	}
	
	public PMPMDTO(String months, Double totalPricepm, Long memberCount, String state, String patientType) {
		super();
		this.months = months;
		this.totalPricepm = totalPricepm;
		this.memberCount = memberCount;
		this.state = state;
		this.patientType = patientType;
	}
	
	public PMPMDTO(String months, String provider, Double totalPricepm, Long memberCount, String patientType) {
		super();
		this.months = months;
		this.provider = provider;
		this.totalPricepm = totalPricepm;
		this.memberCount = memberCount;
		this.patientType = patientType;
	}
	
	@Override
    public PMPMDTO clone() {
        try {
            return (PMPMDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
