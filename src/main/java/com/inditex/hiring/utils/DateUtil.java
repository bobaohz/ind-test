package com.inditex.hiring.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private static final String REG_FOR_SPECIAL_ISO8601_FORMAT = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}.\\d{2}.\\d{2}Z$";

    public static boolean isValidISO8601(String inputDateString) {
        if (StringUtils.isBlank(inputDateString)) {
            return false;
        }
        try {
            String tmpString = changeToStandardISO8601(inputDateString);
            Instant.from(DateTimeFormatter.ISO_INSTANT.parse(tmpString));
            return true;
        } catch (DateTimeParseException e) {
            LOGGER.warn("Invalid input for ISO8601 - {}", inputDateString);
        }
        return false;
    }

    public static Instant convertToInstant(String dateWithISO8601) {
        if (!isValidISO8601(dateWithISO8601)) {
            return null;
        }

        String tmpString = changeToStandardISO8601(dateWithISO8601);
        return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(tmpString));
    }

    public static String toISO8601String(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return formatter.format(instant);
    }

    /**
     * In the test case, the date looks like "2020-06-15T00.00.00Z".
     * It's not the standard format, so here to convert it for further processing
     *
     * @return
     */
    private static String changeToStandardISO8601(String specialISO8601DateString) {
        Pattern pattern = Pattern.compile(REG_FOR_SPECIAL_ISO8601_FORMAT);
        Matcher matcher = pattern.matcher(specialISO8601DateString);
        if (matcher.matches()) {
            return specialISO8601DateString.replace(".", ":");
        }
        return specialISO8601DateString;
    }

}
