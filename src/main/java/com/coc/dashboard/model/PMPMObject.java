package com.coc.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PMPMObject {

	private String lob;
	private String state;
	private String startMonth;
	private String endMonth;
	private String graphType;
	private String viewType;
}
