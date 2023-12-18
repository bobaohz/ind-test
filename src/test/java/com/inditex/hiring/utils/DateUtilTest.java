package com.inditex.hiring.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Instant;

public class DateUtilTest {

    @Test
    public void testIsValidISO8601(){
        Assert.assertTrue(DateUtil.isValidISO8601("2023-11-09T19:30:45.123Z"));
        Assert.assertTrue(DateUtil.isValidISO8601("2023-11-09T19:30:45Z"));
        Assert.assertFalse(DateUtil.isValidISO8601("2023-11-09 19:30:45Z"));
        Assert.assertFalse(DateUtil.isValidISO8601("2023-11-09 19:30:45"));
        Assert.assertFalse(DateUtil.isValidISO8601(""));
        Assert.assertFalse(DateUtil.isValidISO8601(null));
        Assert.assertTrue(DateUtil.isValidISO8601("2020-06-14T00:00:00Z"));
        //a special ISO8601 case used in test case and in the question
        Assert.assertTrue(DateUtil.isValidISO8601("2020-06-14T00.00.00Z"));
    }

    @Test
    public void testConvertToInstant(){
        Instant instant = DateUtil.convertToInstant("2023-11-09T19:30:45.123Z");
        Assertions.assertNotNull(instant);
        Assertions.assertEquals(123000000, instant.getNano());

        //negative case
        instant = DateUtil.convertToInstant("2023-11-09T19.30.45.123Z");
        Assertions.assertNull(instant);
    }

    @Test
    public void testToISO8601String() {
        String originalISO8601String = "2023-11-09T19:30:45.123Z";
        Instant instant = DateUtil.convertToInstant(originalISO8601String);
        Assert.assertEquals(originalISO8601String, DateUtil.toISO8601String(instant));
    }

}
