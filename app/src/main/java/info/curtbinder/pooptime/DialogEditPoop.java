package info.curtbinder.pooptime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.threeten.bp.LocalDateTime;

public class DialogEditPoop extends DialogFragment
    implements View.OnClickListener {

    private static final String ARG_ID = "ID";

    private long id;
    private TextView tvDate;
    private TextView tvTime;
    private EditText notes;
    private RadioButton radioNormal;
    private RadioButton radioHard;
    private RadioButton radioLoose;
    private LocalDateTime dateTime;

    public static DialogEditPoop newInstance(long id) {
        DialogEditPoop dlg = new DialogEditPoop();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        dlg.setArguments(args);
        return dlg;
    }

    private void findViews(View root) {
        tvDate = root.findViewById(R.id.textDate);
        tvTime = root.findViewById(R.id.textTime);
        notes = root.findViewById(R.id.notes);
        radioNormal = root.findViewById(R.id.radioNormal);
        radioHard = root.findViewById(R.id.radioHard);
        radioLoose = root.findViewById(R.id.radioLoose);
        root.findViewById(R.id.btnChangeDate).setOnClickListener(this);
        root.findViewById(R.id.btnChangeTime).setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        View root = inflater.inflate(R.layout.dlg_edit_poop, null);
        findViews(root);
        builder.setView(root);
        Bundle args = getArguments();
        builder.setTitle(R.string.edit_poop_title);
        if(args != null) {
            id = args.getLong(ARG_ID);
            loadData();
            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updatePoop();
                }
            });
            builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletePoop();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        } else {
            // should never happen, but create a null window
            tvDate.setText(R.string.never);
            tvTime.setText(R.string.never);
            builder.setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }

        return builder.create();
    }

    private void loadData() {
        // get data from the database
        ContentValues cv = DBCommands.getPoopInfo(getContext(), id);
        Log.d("EditPoop", "Content: " + cv);
        notes.setText(cv.getAsString(MainTable.COL_NOTES));
        int type = cv.getAsInteger(MainTable.COL_TYPE);
        switch (type){
            default:
            case PoopType.NORMAL:
                radioNormal.setChecked(true);
                break;
            case PoopType.HARD:
                radioHard.setChecked(true);
                break;
            case PoopType.LOOSE:
                radioLoose.setChecked(true);
                break;
        }
        dateTime = LocalDateTime.parse(
                cv.getAsString(MainTable.COL_TIMESTAMP),
                DBCommands.getDefaultDateFormat());
        tvDate.setText(dateTime.format(DBCommands.getDateOnlyFormat()));
        tvTime.setText(dateTime.format(DBCommands.getTimeOnlyFormat()));
    }

    private void updatePoop() {
        // get the info from the dialog and update it in the database
        ContentValues cv = new ContentValues();
        int type = PoopType.NORMAL;
        if ( radioLoose.isChecked() ) {
            type = PoopType.LOOSE;
        } else if ( radioHard.isChecked() ) {
            type = PoopType.HARD;
        }
        cv.put(MainTable.COL_TYPE, type);
        cv.put(MainTable.COL_TIMESTAMP, dateTime.format(DBCommands.getDefaultDateFormat()));
        cv.put(MainTable.COL_NOTES, notes.getText().toString());
        DBCommands.updatePoop(getContext(), id, cv);
    }

    private void deletePoop() {
        // delete the current item
        DBCommands.deletePoop(getContext(), id);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnChangeDate:
                changeDate();
                break;
            case R.id.btnChangeTime:
                changeTime();
                break;
        }
    }

    private void changeDate() {
        // update the dateTime variable with the date selected
        DatePickerDialog dp = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the stored LocalDateTime with the new date
                        dateTime = LocalDateTime.of(year, month+1, dayOfMonth, dateTime.getHour(), dateTime.getMinute());
                        tvDate.setText(dateTime.format(DBCommands.getDateOnlyFormat()));
                    }
                },
                dateTime.getYear(),
                dateTime.getMonthValue()-1,
                dateTime.getDayOfMonth()
                );
        dp.show();
    }

    private void changeTime() {
        // update the dateTime variable with the time selected
        TimePickerDialog tp = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the stored LocalDateTime with the new time
                        dateTime = LocalDateTime.of(
                                dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                                hourOfDay, minute);
                        tvTime.setText(dateTime.format(DBCommands.getTimeOnlyFormat()));
                    }
                },
                dateTime.getHour(),
                dateTime.getMinute(),
                false
                );
        tp.show();
    }
}
