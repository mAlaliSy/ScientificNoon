package org.n_scientific.scientificnoon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammad on 02/06/17.
 */

public final class DateUtils {

    private DateUtils() {
    }


    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }


    public static String formatDate(String date, String sourcePattern, String wantedPattern) {
        return formatDate(parseDate(date, sourcePattern), wantedPattern);
    }

}
