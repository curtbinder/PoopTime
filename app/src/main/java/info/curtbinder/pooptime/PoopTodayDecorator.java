package info.curtbinder.pooptime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class PoopTodayDecorator implements DayViewDecorator {

    private final Context ctx;

    public PoopTodayDecorator(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return (day.equals(CalendarDay.today()));
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Handle the determination in the decorate call
        // this function will only be called on TODAY based on the shouldDecorate() logic
        // If we do not do the lookup here, the day will not be updated appropriately when the
        // database changes and the widget is forced to redraw it's view
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        CalendarDay day = CalendarDay.today();
        String sDate = DBCommands.getDefaultDayOnlyFormatString(day.getDay(), day.getMonth(), day.getYear());
        if (DBCommands.isDayLogged(ctx, sDate)) {
            oval.getPaint().setColor(ctx.getColor(R.color.primaryColor));
        } else {
            oval.getPaint().setColor(Color.TRANSPARENT);
        }
        view.setBackgroundDrawable(oval);
    }
}
