package info.curtbinder.pooptime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        TextView tv = root.findViewById(R.id.textVersion);
        String s = "v" + BuildConfig.VERSION_NAME;
        tv.setText(s);
        tv = root.findViewById(R.id.textBuild);
        s = "Build: " + BuildConfig.VERSION_CODE;
        tv.setText(s);
        tv = root.findViewById(R.id.textDBVersion);
        s = "DB Version: " + DBHelper.DB_VERSION;
        tv.setText(s);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }
}