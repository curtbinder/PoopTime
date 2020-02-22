/*
 * MIT License
 * Copyright (c) 2020 Curt Binder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
