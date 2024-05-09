package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TARGET_PMPM")
@Data
public class TargetPMPM {

	@Id
	@Column(name = "months")
    private String months;
	
	@Column(name = "target_percentage")
    private Long targetPercentage;
}
