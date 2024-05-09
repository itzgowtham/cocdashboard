package com.coc.dashboard.util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateFormat {

	public String convertIntegertoStringDateFormat(String inputDate) {

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		YearMonth yearMonth = YearMonth.parse(inputDate, inputFormatter);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
		return outputFormatter.format(yearMonth);
	}
	
	public String convertStringtoIntegerDateFormat(String inputDate) {

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
		YearMonth yearMonth = YearMonth.parse(inputDate, inputFormatter);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		return outputFormatter.format(yearMonth);
	}
	
	public String getPreviousMonths(String monthYear, Integer totalMonths) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		YearMonth yearMonth = YearMonth.parse(monthYear, inputFormatter);
		yearMonth = yearMonth.minusMonths(totalMonths);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		return yearMonth.format(formatter);
	}
	
	public String getPreviousYearMonth(String monthYear) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		YearMonth yearMonth = YearMonth.parse(monthYear, inputFormatter);
		yearMonth = yearMonth.minusYears(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		return yearMonth.format(formatter);
	}
	
	public Integer getTotalMonths(String startMonthYear, String endMonthYear) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.ENGLISH);
		YearMonth startYearMonth = YearMonth.parse(startMonthYear, inputFormatter);
		YearMonth endYearMonth = YearMonth.parse(endMonthYear, inputFormatter);
		int totalMonths = (endYearMonth.getYear() - startYearMonth.getYear()) * 12;
		totalMonths += endYearMonth.getMonthValue() - startYearMonth.getMonthValue() + 1;
		return totalMonths;
	}

	public static class MonthYearComparator implements Comparator<String> {
		@Override
		public int compare(String monthYear1, String monthYear2) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
			YearMonth date1 = YearMonth.parse(monthYear1, formatter);
			YearMonth date2 = YearMonth.parse(monthYear2, formatter);
			return date2.compareTo(date1);
		}
	}
}
