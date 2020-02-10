package info.curtbinder.pooptime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

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
}
