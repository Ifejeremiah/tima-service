package com.tima.util;

import com.tima.exception.BadRequestException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    Date date;

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
}
