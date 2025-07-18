package com.tima.util;

import com.tima.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    Date date;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);

    public DateUtil() {
    }

    public DateUtil(Date date) {
        this.date = date;
    }

    public String getDateInGMT() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    public Date parseDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            return dateFormat.parse(date);
        } catch (Exception error) {
            throw new BadRequestException(error.getMessage());
        }
    }

    public static void validateStartAndEndDates(String startDate, String endDate) {
        try {
            LocalDate localStartDate = null;
            LocalDate localEndDate = null;
            if (!StringUtils.isEmpty(startDate)) localStartDate = LocalDate.parse(startDate, formatter);
            if (!StringUtils.isEmpty(endDate)) localEndDate = LocalDate.parse(endDate, formatter);
            if (localStartDate != null && localEndDate != null && localStartDate.isAfter(localEndDate))
                throw new BadRequestException("Invalid Date Specified. Start Date is after End Date");
        } catch (DateTimeParseException error) {
            throw new BadRequestException("Invalid Date Specified. Provide date in format yyyyMMdd(e.g 20010823)");
        }
    }
}
