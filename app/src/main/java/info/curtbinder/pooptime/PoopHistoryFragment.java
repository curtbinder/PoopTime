package info.curtbinder.pooptime;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class PoopHistoryFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] FROM = {
            MainTable.COL_ID,
            MainTable.COL_TIMESTAMP,
            MainTable.COL_TYPE,
            MainTable.COL_NOTES
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_list_poop, container, false);
        ListView lv = v.findViewById(android.R.id.list);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                // Prompt to delete the poop
                DialogEditPoop dlg = DialogEditPoop.newInstance(id);
                dlg.show(getFragmentManager(), "dlgedit");
                return false;
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater ) {
        menu.clear();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<Cursor> loader = null;
        if (id == 0) {
            loader = new CursorLoader(getActivity(), DBCommands.MAIN_URI, FROM,
                    null, null,
                    MainTable.COL_TIMESTAMP + " DESC");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ListAdapter adapter = getListAdapter();
        if ( (adapter == null) || !(adapter instanceof CursorAdapter)) {
            adapter = new PoopHistoryCursorAdaper(getActivity(), data, 0);
            setListAdapter(adapter);
        } else {
            ((CursorAdapter) adapter).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // on reset
    }
}
