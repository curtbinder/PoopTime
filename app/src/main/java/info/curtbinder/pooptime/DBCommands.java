package info.curtbinder.pooptime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;

public class DBCommands {

    final static long INVALID = -1;
    final static long ALREADY_LOGGED = -2;

    final static Uri LATEST_URI = Uri.parse(DBProvider.CONTENT_URI + "/" + DBProvider.PATH_LATEST);
    final static Uri MAIN_URI = Uri.parse(DBProvider.CONTENT_URI + "/" + DBProvider.PATH_MAIN);

    // Date format stored in DB
    public static DateTimeFormatter getDefaultDateFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    // Date format for displaying on screen
    public static DateTimeFormatter getDisplayLongDateFormat() {
        return DateTimeFormatter.ofPattern("EEEE\nLLLL d, yyyy hh:mm a");
    }

    // Date format of Date only
    public static DateTimeFormatter getDateOnlyFormat() {
        return DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    // Date format of Time only
    public static DateTimeFormatter getTimeOnlyFormat() {
        return DateTimeFormatter.ofPattern("hh:mm a");
    }

    // Formats ONLY the date, ignoring the time
    public static String getDefaultDayOnlyFormatString(int day, int month, int year) {
        return String.format("%d-%02d-%02d", year, month, day);
    }

    public static String getDisplayDate(String sDate) {
        // Update format of date be 'Weekday, Month, DD, YYYY HH:MM AM|PM'
        if ( sDate.equals("Never") ) {
            // if it's never, just return and don't try to parse
            return sDate;
        }
        // Parse the original date format from database
        LocalDateTime d = LocalDateTime.parse(sDate, getDefaultDateFormat());
        // Format the date for display on the screen
        return d.format(getDisplayLongDateFormat());
    }

    public static String getDaysSinceLastPoop(Context ctx) {
        // Compute days between last poop date and current date
        String lastPoopDate = getLastPoopDate(ctx);
        if ( lastPoopDate.equals("Never") ) {
            return "0";
        }
        LocalDate dateLastPoop = LocalDate.parse(lastPoopDate, getDefaultDateFormat());
        return Integer.toString(Period.between(dateLastPoop, LocalDate.now()).getDays());
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

    public static ContentValues getPoopInfo(Context ctx, long id) {
        ContentValues cv = new ContentValues();
        Uri uri = Uri.withAppendedPath(MAIN_URI, Long.toString(id));
        Cursor c = ctx.getContentResolver().query(uri, null,
                null,
                null,
                null);
        if ( c != null ) {
            if ( c.moveToFirst() ) {
                cv.put(MainTable.COL_TIMESTAMP, c.getString(c.getColumnIndex(MainTable.COL_TIMESTAMP)));
                cv.put(MainTable.COL_TYPE, c.getInt(c.getColumnIndex(MainTable.COL_TYPE)));
                cv.put(MainTable.COL_NOTES, c.getString(c.getColumnIndex(MainTable.COL_NOTES)));
            }
            c.close();
        }
        return cv;
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

    public static void updatePoop(Context ctx, long id, ContentValues cv) {
        Uri uri = Uri.withAppendedPath(MAIN_URI, Long.toString(id));
        ctx.getContentResolver().update(uri, cv, null, null);
    }

    public static void deletePoop(Context ctx, long id) {
        // deletes the selected poop
        Uri uri = Uri.withAppendedPath(MAIN_URI, Long.toString(id));
        ctx.getContentResolver().delete(uri, null, null);
    }
}
