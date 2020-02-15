package info.curtbinder.pooptime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class PoopDayDecorator implements DayViewDecorator {

    // Drawable for the day
    private final Drawable poopDrawable;
    private final Context ctx;

    public PoopDayDecorator(Context ctx) {
        this.ctx = ctx;
        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(ContextCompat.getColor(ctx, R.color.primaryColor));
        poopDrawable = square;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // Look up the day in the database
        // If the DAY is in it, we decorate the day
        String date = DBCommands.getDefaultDayOnlyFormatString(day.getDay(), day.getMonth(), day.getYear());
        return DBCommands.isDayLogged(ctx, date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(poopDrawable);
    }
}
