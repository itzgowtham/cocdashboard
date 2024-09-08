package com.coc.dashboard.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateFormatTest {

    @InjectMocks
    private DateFormat dateFormat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertIntegertoStringDateFormat() {
        String inputDate = "2024-07";
        String expected = "Jul 2024";
        assertEquals(expected, dateFormat.convertIntegertoStringDateFormat(inputDate));
    }

    @Test
    public void testConvertStringtoIntegerDateFormat() {
        String inputDate = "Jul 2024";
        String expected = "2024-07";
        assertEquals(expected, dateFormat.convertStringtoIntegerDateFormat(inputDate));
    }

    @Test
    public void testGetPreviousMonths() {
        String monthYear = "2024-07";
        Integer totalMonths = 3;
        String expected = "2024-04";
        assertEquals(expected, dateFormat.getPreviousMonths(monthYear, totalMonths));
    }

    @Test
    public void testGetPreviousYearMonth() {
        String monthYear = "2024-07";
        String expected = "2023-07";
        assertEquals(expected, dateFormat.getPreviousYearMonth(monthYear));
    }

    @Test
    public void testGetTotalMonths() {
        String startMonthYear = "2023-11";
        String endMonthYear = "2024-03";
        int expected = 5;
        assertEquals(expected, dateFormat.getTotalMonths(startMonthYear, endMonthYear));
    }

    @Test
    public void testMonthYearComparator() {
        List<String> dates = new ArrayList<>(List.of("Jul 2024", "Dec 2023", "May 2024"));
        Collections.sort(dates, new DateFormat.MonthYearComparator());
        assertEquals("Jul 2024", dates.get(0));
        assertEquals("May 2024", dates.get(1));
        assertEquals("Dec 2023", dates.get(2));
    }
}
