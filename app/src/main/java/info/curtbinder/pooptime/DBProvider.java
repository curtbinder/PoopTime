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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBProvider extends ContentProvider {

    private DBHelper data;
    private static final String CONTENT = DBProvider.class.getPackage().getName() + ".provider";

    private static final String CONTENT_MIME_TYPE = "/vnd." + CONTENT + ".";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT);

    // PATHS
    public static final String PATH_MAIN = "main";
    public static final String PATH_LATEST = "latest";

    // MIME Types
    // main - 1 item
    public static final String MAIN_ID_MIME_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_MIME_TYPE + PATH_MAIN;
    // main - all items
    public static final String MAIN_MIME_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_MIME_TYPE + PATH_MAIN;
    // latest poop
    public static final String LATEST_MIME_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_MIME_TYPE + PATH_LATEST;

    // Uri matcher values
    private static final int CODE_MAIN = 100;
    private static final int CODE_MAIN_ID = 101;
    private static final int CODE_LATEST = 110;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(CONTENT, PATH_MAIN, CODE_MAIN);
        sUriMatcher.addURI(CONTENT, PATH_MAIN + "/#", CODE_MAIN_ID);
        sUriMatcher.addURI(CONTENT, PATH_LATEST, CODE_LATEST);
    }

    @Override
    public boolean onCreate() {
        data = new DBHelper(getContext());
        return (data != null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        Cursor c = null;
        SQLiteDatabase db = data.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String limit = null;
        qb.setTables(MainTable.TABLE_NAME);
        // if an individual item is queried by the URI, then append where clause here
        if (match == CODE_MAIN_ID) {
            qb.appendWhere(MainTable.COL_ID + "=" + uri.getLastPathSegment());
        }
        if (match == CODE_LATEST) {
            limit = "1";
        }
        c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder, limit);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch(match) {
            case CODE_MAIN:
                return MAIN_MIME_TYPE;
            case CODE_MAIN_ID:
                return MAIN_ID_MIME_TYPE;
            case CODE_LATEST:
                return LATEST_MIME_TYPE;
            default:
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = data.getWritableDatabase();
        long id = db.insert(MainTable.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PATH_MAIN + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = data.getWritableDatabase();
        int rowsDeleted = 0;
        if (sUriMatcher.match(uri) == CODE_MAIN_ID) {
            // delete based on specific ID from URI
            rowsDeleted = db.delete(MainTable.TABLE_NAME, MainTable.COL_ID + "=?",
                    new String[] {uri.getLastPathSegment()});
        } else {
            // delete based on selection criteria given
            rowsDeleted = db.delete(MainTable.TABLE_NAME, selection, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = data.getWritableDatabase();
        int rowsUpdated = 0;
        if (sUriMatcher.match(uri) == CODE_MAIN_ID) {
            // update based on specific ID from URI
            rowsUpdated = db.update(MainTable.TABLE_NAME, contentValues, MainTable.COL_ID + "=?",
                    new String[] {uri.getLastPathSegment()});
        } else {
            // update based on selection criteria given
            rowsUpdated = db.update(MainTable.TABLE_NAME, contentValues, selection, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
