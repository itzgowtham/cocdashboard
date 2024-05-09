package com.coc.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricData {

	private Double target;
	private Double actual;
	private Double difference;
	private Double differencePercentage;
}
