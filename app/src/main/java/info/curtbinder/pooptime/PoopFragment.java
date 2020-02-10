package info.curtbinder.pooptime;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PoopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PoopFragment extends Fragment
    implements View.OnClickListener {



    public PoopFragment() {
        // Required empty public constructor
    }

    public static PoopFragment newInstance() {
        //String param1, String param2
        PoopFragment fragment = new PoopFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_poop, container, false);
        findViews(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateDisplay();
    }

    private void findViews(View v) {

        // set the onclick listeners
        v.findViewById(R.id.btnNow).setOnClickListener(this);
        v.findViewById(R.id.btnOther).setOnClickListener(this);
    }

    private void updateDisplay() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // TODO get the rest of the data from the fields
        switch (id) {
            case R.id.btnNow: {
                // TODO get current date and time
                logNow();
                break;
            }
            case R.id.btnOther: {
                // TODO prompt for date and time
                logOther();
                break;
            }
        }
    }

    private void logNow() {
        // TODO log now
    }

    private void logOther() {
        // TODO log other
    }
}
