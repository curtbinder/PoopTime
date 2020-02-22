package info.curtbinder.pooptime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

public class CalendarFragment extends Fragment implements OnDateSelectedListener {

    private MaterialCalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_calendar_poop, container, false);
        calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        calendarView.addDecorator(new DayDecorator(getContext()));
        calendarView.addDecorator(new TodayDecorator(getContext()));
        calendarView.setDateSelected(CalendarDay.today(), true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPoopsForDay(CalendarDay.today());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//        Log.d("Calendar", "Date Selected: " + date.toString() + ", selected: " + selected);
        loadPoopsForDay(date);
    }

    private void loadPoopsForDay(CalendarDay date) {
        LocalDate day = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
        String sDay = day.format(DBCommands.getDBDayOnlyFormat());
        Log.d("Calendar", "Loading " + sDay);
        Bundle args = new Bundle();
        args.putString(HistoryFragment.DAY_TO_LOAD, sDay);
        HistoryFragment history = new HistoryFragment();
        history.setArguments(args);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, history);
        ft.commit();
    }
}
