package info.curtbinder.pooptime;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

public class MainTable {
    public static final String TABLE_NAME = "main";

    // columns
    public static final String COL_ID = "_id";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_TYPE = "type";
    public static final String COL_NOTES = "notes";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + " ("
                + COL_ID + " integer primary key autoincrement, "
                + COL_TIMESTAMP + " text, "
                + COL_TYPE + " integer default 0, "
                + COL_NOTES + " text"
                + ");"
        );
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO fill in when upgrading database versions
        /*
                int currentVersion = oldVersion;
        while (currentVersion < newVersion) {
            currentVersion++;
            switch (currentVersion) {
                case 2:
                    upgradeToVersion2(db);
                    break;
                default:
                    break;
            }
        }
         */
        return;
    }
}
