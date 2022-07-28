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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import org.threeten.bp.LocalDateTime;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> createDBActivityResultLauncher;
    ActivityResultLauncher<Intent> openDBActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if (savedInstanceState == null) {
            PoopFragment f = PoopFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, f, "POOP")
                    .commit();
        }

        // Setup ability to export database
        createDBActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri;
                        if (result.getData() != null) {
                            uri = result.getData().getData();
                            createExportedCSV(uri);
                            Toast.makeText(this,
                                    getString(R.string.exported_poops),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        openDBActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri;
                        if (result.getData() != null) {
                            uri = result.getData().getData();

                            int count = loadImportedCSV(uri);
                            Toast.makeText(this,
                                    getString(R.string.imported_poops, count),
                                    Toast.LENGTH_SHORT).show();

                            // Navigate to Calendar screen since refreshing doesn't work properly
                            onNavigateCalendar();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_history:
                onNavigateHistory();
                return true;
            case R.id.action_calendar:
                onNavigateCalendar();
                return true;
            case R.id.action_about:
                navigateFragment(new AboutFragment());
                return true;
            case R.id.action_export:
                createFile("poop-" + LocalDateTime.now().format(DBCommands.getDBDayOnlyFormat()));
                return true;
            case R.id.action_import:
                openFile("poop-" + LocalDateTime.now().format(DBCommands.getDBDayOnlyFormat()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, f)
                .addToBackStack(null)
                .commit();
    }

    public void onNavigateCalendar() {
        navigateFragment(new CalendarFragment());
    }

    public void onNavigateHistory() {
        navigateFragment(new HistoryFragment());
    }

    private void createFile(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("/Documents"));
        }
        createDBActivityResultLauncher.launch(intent);
    }

    @SuppressLint("Range")
    private void createExportedCSV(Uri uri) {
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
            if (pfd != null) {
                try (Cursor c = DBCommands.getAllPoops(getApplicationContext())) {
                    CSVWriter writer = new CSVWriter(new FileWriter(pfd.getFileDescriptor()));
                    String ts, type, notes;
                    while (c.moveToNext()) {
                        // get the fields and create a line
                        ts = c.getString(c.getColumnIndex(MainTable.COL_TIMESTAMP));
                        type = c.getString(c.getColumnIndex(MainTable.COL_TYPE));
                        notes = c.getString(c.getColumnIndex(MainTable.COL_NOTES));
                        String [] nextLine = {ts, type, notes};
                        writer.writeNext(nextLine);
                    }
                    writer.close();
                }
                pfd.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(String title) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("/Documents"));
        }
        openDBActivityResultLauncher.launch(intent);
    }

    private int loadImportedCSV(Uri uri) {
        int count = 0;
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                try {
                    CSVReader reader = new CSVReader(new FileReader(pfd.getFileDescriptor()));
                    String [] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        if (DBCommands.logPoop(this, nextLine[0], Integer.parseInt(nextLine[1]), nextLine[2]) > DBCommands.INVALID) {
                            // only count the ones that are not invalid when importing
                            count++;
                        }
                    }
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
                pfd.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
