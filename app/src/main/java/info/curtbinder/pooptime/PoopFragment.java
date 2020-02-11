package info.curtbinder.pooptime;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PoopFragment extends Fragment
    implements View.OnClickListener {


    public static final String TAG = PoopFragment.class.getSimpleName();

    private EditText notes;
    private RadioGroup types;
    private RadioButton normal;
    private TextView lastPoop;
    private TextView daysSince;

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
        notes = v.findViewById(R.id.notes);
        types = v.findViewById(R.id.radioPanel);
        normal = v.findViewById(R.id.radioNormal);
        lastPoop = v.findViewById(R.id.lastPoop);
        daysSince = v.findViewById(R.id.daysSince);

        // set the onclick listeners
        v.findViewById(R.id.btnNow).setOnClickListener(this);
        v.findViewById(R.id.btnOther).setOnClickListener(this);
    }

    private void updateDisplay() {
        // Clear the fields
        notes.setText("");
        normal.toggle();
        // refresh the quick summary
        final Context ctx = getContext();
        String s = DBCommands.getDaysSinceLastPoop(ctx);
        daysSince.setText(s);
        s = DBCommands.getLastPoopDisplayDate(ctx);
        lastPoop.setText(s);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String sNotes = notes.getText().toString();
        int iType = getTypeFromRadioId(types.getCheckedRadioButtonId());
        switch (id) {
            case R.id.btnNow: {
                logNow(iType, sNotes);
                break;
            }
            case R.id.btnOther: {
                logOther(iType, sNotes);
                break;
            }
        }
    }

    private int getTypeFromRadioId(int radioID) {
        RadioButton r = (RadioButton)getActivity().findViewById(radioID);
        String text = r.getText().toString();
        return DBCommands.getTypeIntFromString(text);
    }

    private void logNow(int type, String notes) {
        log(DBCommands.getDefaultDateFormat().format(Calendar.getInstance().getTime()), type, notes);
    }

    private void logOther(final int type, final String notes) {
        // display popup prompting for the date
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
        DatePickerDialog dp = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        callTimePicker(year, month, dayOfMonth, type, notes);
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    private void callTimePicker(final int year, final int month, final int dayOfMonth, final int type, final String notes) {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog tp = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Log.d(TAG, "Chose: " + hourOfDay + ":" + minute);
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth, hourOfDay, minute);
                        SimpleDateFormat dft = DBCommands.getDefaultDateFormat();
                        String timestamp = dft.format(c.getTime());
                        Log.d(TAG, "Chose: " + timestamp);
                        log(timestamp, type, notes);
                    }
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        );
        tp.show();
    }

    private void log(String timestamp, int type, String notes) {
        String fancyDate = DBCommands.getDisplayDate(timestamp);
        Log.d(TAG, "Date: " + timestamp + "\nType: " + type + "\nNotes: " + notes);

        long result = DBCommands.logPoop(getContext(), timestamp, type, notes);
        if (result == DBCommands.ALREADY_LOGGED) {
            Toast.makeText(getContext(), "Already logged poop on " + fancyDate, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Added poop on " + fancyDate, Toast.LENGTH_SHORT).show();

        updateDisplay();
    }
}
