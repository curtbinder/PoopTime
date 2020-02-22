package info.curtbinder.pooptime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class DayDecorator implements DayViewDecorator {

    private final Context ctx;
//    private final int bgColor;

//    private int dayCount;
//    private TextDrawable poopDrawable;
//    private LayerDrawable container;
    private Drawable bg;
    private int inset;

    public DayDecorator(Context ctx) {
        this.ctx = ctx;
//        bgColor = ContextCompat.getColor(ctx, R.color.primaryColor);
//        dayCount = 0;

        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(ContextCompat.getColor(ctx, R.color.primaryColor));
        bg = square;
//        inset = ctx.getResources().getDimensionPixelOffset(R.dimen.layer_inset);
//        bg = ctx.getDrawable(R.drawable.poopbackground);
//        container = new LayerDrawable(new Drawable[]{bg});
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // Look up the day in the database
        // If the DAY is in it, we decorate the day
        String date = DBCommands.getDefaultDayOnlyFormatString(day.getDay(), day.getMonth(), day.getYear());
        return DBCommands.isDayLogged(ctx, date);
//        dayCount = DBCommands.getPoopCountForDay(ctx, date);
//        boolean fRet = false;
//        Log.d("PoopDay", date + ": " + dayCount);
//        if ( dayCount > 0 ) {
//            fRet = true;
//        }
//        return fRet;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(bg);
    }
}
