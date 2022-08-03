package com.interswitch.dps.codemanagement.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static LocalDate toLocalDateFormat(Date startDate) {
        LocalDate localDateFormat = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDateFormat;
    }

    public static Date fromLocalDateFormat(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTimeFormat(Date startDate) {
        LocalDateTime localDateTimeFormat = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTimeFormat;
    }

    public static Date fromLocalDateTimeFormat(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertToDate(String dateString) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM:dd:yyyy");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            if (dateString.indexOf("-") > 0) {
                date = formatter3.parse(dateString);
            } else if (dateString.indexOf(":") > 0) {
                date = formatter2.parse(dateString);
            } else {
                date = formatter1.parse(dateString);
            }
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String convertToString(Date date) {
        if(date ==null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("E yyyy-MM-dd hh:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("WAT"));
        return dateFormat.format(date);
    }

    public static String toYearMonth(Date dateString) {
        DateFormat formatter = new SimpleDateFormat("MMM yyyy");
        return formatter.format(dateString);
    }

}
