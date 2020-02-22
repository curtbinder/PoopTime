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

public class HistoryFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOAD_ALL = 0;
    public static final int LOAD_DAY = 1;
    public static final String DAY_TO_LOAD = "day";

    private int loaderId = LOAD_ALL;
    private String mDay = "";

    private static final String[] FROM = {
            MainTable.COL_ID,
            MainTable.COL_TIMESTAMP,
            MainTable.COL_TYPE,
            MainTable.COL_NOTES
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if ( args != null ) {
            if ( args.containsKey(DAY_TO_LOAD) ) {
                mDay = args.getString(DAY_TO_LOAD);
                loaderId = LOAD_DAY;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( loaderId == LOAD_ALL ) {
            setHasOptionsMenu(true);
        }
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
        getLoaderManager().restartLoader(loaderId, null, this);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater ) {
        if ( loaderId == LOAD_ALL ) {
            menu.clear();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<Cursor> loader = null;
        if (id == LOAD_ALL) {
            loader = new CursorLoader(getActivity(), DBCommands.MAIN_URI, FROM,
                    null, null,
                    MainTable.COL_TIMESTAMP + " DESC");
        } else if (id == LOAD_DAY) {
            loader = new CursorLoader(getActivity(), DBCommands.MAIN_URI, FROM,
                    MainTable.COL_TIMESTAMP + " like '" + mDay + "%'", null,
                    MainTable.COL_TIMESTAMP + " DESC");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ListAdapter adapter = getListAdapter();
        if ( (adapter == null) || !(adapter instanceof CursorAdapter)) {
            adapter = new HistoryCursorAdaper(getActivity(), data, 0);
            setListAdapter(adapter);
        } else {
            ((CursorAdapter) adapter).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // on reset
        ListAdapter adapter = getListAdapter();
        ((CursorAdapter) adapter).swapCursor(null);
    }
}
