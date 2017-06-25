package com.amperas17.mangaedenapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    public static final int MILLIS_IN_SECOND = 1000;

    public static String defaultFormat(Date date){
        return DATE_FORMAT.format(date);
    }

    public static Date getDateFromSeconds(long seconds){
        return new Date(seconds * MILLIS_IN_SECOND);
    }

    public static String defaultFormatFromSeconds(long seconds){
        return defaultFormat(getDateFromSeconds(seconds));
    }
}
