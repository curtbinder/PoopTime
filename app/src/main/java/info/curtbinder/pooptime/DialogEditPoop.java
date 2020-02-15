package info.curtbinder.pooptime;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class DialogEditPoop extends DialogFragment {

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
    }
}
