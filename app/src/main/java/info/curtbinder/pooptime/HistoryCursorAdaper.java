package info.curtbinder.pooptime;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

public class HistoryCursorAdaper extends CursorAdapter {

    static class ViewHolder {
        TextView timestamp;
        TextView type;
        TextView notes;
    }

    Context ctx;

    public HistoryCursorAdaper(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        ctx = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_poop, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.timestamp = v.findViewById(R.id.timestamp);
        vh.notes = v.findViewById(R.id.notes);
        vh.type = v.findViewById(R.id.type);
        setViews(vh, cursor);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();
        setViews(vh, cursor);
    }

    private void setViews(ViewHolder v, Cursor c) {
        v.timestamp.setText(DBCommands.getDisplayDate(c.getString(c.getColumnIndex(MainTable.COL_TIMESTAMP))));
        v.type.setText(PoopType.getTypeStringFromInt(c.getInt(c.getColumnIndex(MainTable.COL_TYPE))));
        v.notes.setText(c.getString(c.getColumnIndex(MainTable.COL_NOTES)));
    }
}
