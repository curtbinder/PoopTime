package info.curtbinder.pooptime;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogEditPoop extends DialogFragment
    implements View.OnClickListener {

    private Uri uri = null;
    private TextView tvDate;
    private TextView tvTime;
    private EditText notes;
    private RadioButton radioNormal;
    private RadioButton radioHard;
    private RadioButton radioLoose;

    public static DialogEditPoop newInstance(Uri uri) {
        DialogEditPoop dlg = new DialogEditPoop();
        Bundle args = new Bundle();
        args.putParcelable(DBProvider.MAIN_ID_MIME_TYPE, uri);
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
            uri = args.getParcelable(DBProvider.MAIN_ID_MIME_TYPE);
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

    }

    private void updatePoop() {
        // get the info from the dialog and update it in the database
    }

    private void deletePoop() {

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

    }

    private void changeTime() {

    }
}
