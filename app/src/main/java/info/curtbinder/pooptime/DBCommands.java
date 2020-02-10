package info.curtbinder.pooptime;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.text.ParseException;
import java.util.Date;

public class DBCommands {

    final static long INVALID = -1;
    final static long ALREADY_LOGGED = -2;

    // Date format stored in DB
    public static SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    // Date format for displaying on screen
    public static SimpleDateFormat getDisplayLongDateFormat() {
        return new SimpleDateFormat("EEEE, LLLL d, yyyy hh:mm aaa");
    }

    // Formats the date for the DB from the individual times
    public static String getDefaultDateFormatString(int day, int month, int year, int hour, int minute) {
        return String.format("%d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
    }

    public static String getDisplayDate(String sDate) {
        // Update format of date be 'Weekday, Month, DD, YYYY HH:MM AM|PM'
        String s = sDate;
        try {
            // Parse the original date format from database
            SimpleDateFormat dftOriginal = getDefaultDateFormat();
            Date d = dftOriginal.parse(s);
            // Set a calendar instance date/time
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            SimpleDateFormat dftDisplay = getDisplayLongDateFormat();
            // Get updated display from the time
            s = dftDisplay.format(cal.getTime());
        } catch (ParseException e) {
            // unable to parse date, just return the date
        }
        return s;
    }

    public static String getDaysSinceLastPoop(Context ctx) {
        // TODO complete getDaysSinceLastPoop
        return "42";
    }

    public static String getLastPoopDate(Context ctx) {
        // TODO complete getLastPoopDate
        return "Monday March 1, 2019 3:42 PM";
    }

    public static boolean isPoopLogged(Context ctx, String date) {
        // TODO complete isPoopLogged
        return false;
    }

    public static long logPoop(Context ctx, String date, int type, String notes) {
        // TODO complete logPoop
        long lRet = INVALID;
        if (isPoopLogged(ctx, date)) {
            return ALREADY_LOGGED;
        }
        return lRet;
    }
}
