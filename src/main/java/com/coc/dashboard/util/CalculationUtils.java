package com.coc.dashboard.util;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
public class CalculationUtils {

	public Double calculatePercentageChange(Number presentValue, Number previousValue) {
		return (previousValue.doubleValue() == 0.0) ? (presentValue.doubleValue() == 0.0) ? 0.0 : 100.0
				: roundToTwoDecimals(
						((presentValue.doubleValue() - previousValue.doubleValue()) / previousValue.doubleValue())
								* 100);
	}

	public Double roundToTwoDecimals(Double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.parseDouble(df.format(value));
	}

	public Double roundToFourDecimals(Double value) {
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.parseDouble(df.format(value));
	}
}
