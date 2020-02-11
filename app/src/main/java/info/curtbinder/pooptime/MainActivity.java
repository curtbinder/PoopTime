package info.curtbinder.pooptime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if (savedInstanceState == null) {
            PoopFragment f = PoopFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, f)
                    .commit();
        }
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
                Toast.makeText(this, "About not implemented", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Calendar not implemented", Toast.LENGTH_SHORT).show();
    }

    public void onNavigateHistory() {
        navigateFragment(new PoopHistoryFragment());
    }
}
