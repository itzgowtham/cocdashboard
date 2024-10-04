package com.coc.dashboard.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CalculationUtilsTest {

    @InjectMocks
    private CalculationUtils calculationUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCalculatePercentageChange() {
        // Test case 1: Non-zero previous value
        double presentValue = 80.0;
        double previousValue = 50.0;
        double expected = 60.0;
        assertEquals(expected, calculationUtils.calculatePercentageChange(presentValue, previousValue));

        // Test case 2: Zero previous value
        presentValue = 80.0;
        previousValue = 0.0;
        expected = 100.0;
        assertEquals(expected, calculationUtils.calculatePercentageChange(presentValue, previousValue));

        // Test case 3: Zero present value
        presentValue = 0.0;
        previousValue = 0.0;
        expected = 0.0;
        assertEquals(expected, calculationUtils.calculatePercentageChange(presentValue, previousValue));
    }

    @Test
    public void testRoundToTwoDecimals() {
        double value = 123.456789;
        double expected = 123.46;
        assertEquals(expected, calculationUtils.roundToTwoDecimals(value));
    }
}
