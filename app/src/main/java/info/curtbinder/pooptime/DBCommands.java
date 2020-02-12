package info.curtbinder.pooptime;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DBCommands {

    final static long INVALID = -1;
    final static long ALREADY_LOGGED = -2;

    final static Uri LATEST_URI = Uri.parse(DBProvider.CONTENT_URI + "/" + DBProvider.PATH_LATEST);
    final static Uri MAIN_URI = Uri.parse(DBProvider.CONTENT_URI + "/" + DBProvider.PATH_MAIN);

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

    // Formats ONLY the date, ignoring the time
    public static String getDefaultDayOnlyFormatString(int day, int month, int year) {
        return String.format("%d-%02d-%02d", year, month, day);
    }

    public static String getDisplayDate(String sDate) {
        // Update format of date be 'Weekday, Month, DD, YYYY HH:MM AM|PM'
        String s = sDate;
        if ( s.equals("Never") ) {
            // if it's never, just return and don't try to parse
            return s;
        }
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
        // Get last poop date
        // Get current date
        // Compute days between
//        long days = 0;
        LocalDateTime dateLastPoop = null;
        LocalDateTime dateToday = null;
        String lastPoopDate = getLastPoopDate(ctx);
        String today = getDefaultDateFormat().format(Calendar.getInstance().getTime());
        try {
            dateLastPoop = LocalDateTime.parse(lastPoopDate);
            dateToday = LocalDateTime.parse(today);
        } catch (DateTimeParseException e) {
            // ignore the parser error
        }
        Duration duration = Duration.ZERO;
        if( (dateLastPoop != null) && (dateToday != null) ) {
//            days = ChronoUnit.DAYS.between(dateLastPoop, dateToday);
            duration = Duration.between(dateLastPoop, dateToday);
        }
        return Long.toString(duration.toDays());
//        return Long.toString(days);
    }

    public static String getLastPoopDate(Context ctx) {
        String sDate = "Never";
        Cursor c = ctx.getContentResolver().query(LATEST_URI, null,
                null,
                null,
                MainTable.COL_TIMESTAMP + " DESC");
        if ( c != null ) {
            if ( c.moveToFirst() ) {
                sDate = c.getString(c.getColumnIndex(MainTable.COL_TIMESTAMP));
            }
            c.close();
        }
        return sDate;
    }

    public static String getLastPoopDisplayDate(Context ctx) {
        return getDisplayDate(getLastPoopDate(ctx));
    }

    public static boolean isPoopLogged(Context ctx, String date) {
        boolean fRet = false;
        Cursor c = ctx.getContentResolver().query(MAIN_URI, null,
                MainTable.COL_TIMESTAMP + " = ?",
                new String[] {date}, null);
        if ( c != null ) {
            if (c.getCount() > 0) {
                // logged
                fRet = true;
            }
            c.close();
        }
        return fRet;
    }

    // Does the given day have any poops logged?
    public static boolean isDayLogged(Context ctx, String date) {
        boolean fRet = false;
        Cursor c = ctx.getContentResolver().query(MAIN_URI, null,
                MainTable.COL_TIMESTAMP + " like '" + date + "%'",
                null, null);
        if ( c != null ) {
            if (c.getCount() > 0) {
                // logged
                fRet = true;
            }
            c.close();
        }
        return fRet;
    }

    public static long logPoop(Context ctx, String date, int type, String notes) {
        long lRet = INVALID;
        if (isPoopLogged(ctx, date)) {
            return ALREADY_LOGGED;
        }
        ContentValues cv = new ContentValues();
        cv.put(MainTable.COL_TIMESTAMP, date);
        cv.put(MainTable.COL_TYPE, type);
        cv.put(MainTable.COL_NOTES, notes);
        Uri i = ctx.getContentResolver().insert(
                DBCommands.MAIN_URI,
                cv
        );

        String s = i.getLastPathSegment();
        if (s.isEmpty() ) {
            // failed to insert
            return lRet;
        }
        lRet = Long.parseLong(s);
        return lRet;
    }

    public static String getTypeStringFromInt(int type) {
        switch (type) {
            case 0:
                return "Normal";
            case 1:
                return "Hard";
            case 2:
                return "Loose";
        }
        return "None";
    }

    public static int getTypeIntFromString(String type) {
        int retVal;
        switch(type) {
            default:
            case "Normal":
                retVal = 0;
                break;
            case "Hard":
                retVal = 1;
                break;
            case "Loose":
                retVal = 2;
                break;
        }
        return retVal;
    }
}
