package info.curtbinder.pooptime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class PoopDayDecorator implements DayViewDecorator {

    // Drawable for the day
    private final Drawable poopDrawable;
    private final Context ctx;
    private final float textSize;
    private final static int DEFAULT_TEXT_SIZE = 12;
    private final int bgColor;

    private int dayCount;
    private TextRectDrawable poopCountDrawable;

    public PoopDayDecorator(Context ctx) {
        this.ctx = ctx;
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                DEFAULT_TEXT_SIZE, ctx.getResources().getDisplayMetrics());
        bgColor = ContextCompat.getColor(ctx, R.color.primaryColor);
        dayCount = 0;
        poopCountDrawable = new TextRectDrawable(Color.WHITE, textSize, bgColor);

        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(ContextCompat.getColor(ctx, R.color.primaryColor));
        poopDrawable = square;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // Look up the day in the database
        // If the DAY is in it, we decorate the day
        String date = DBCommands.getDefaultDayOnlyFormatString(day.getDay(), day.getMonth(), day.getYear());
        dayCount = DBCommands.getPoopCountForDay(ctx, date);
        poopCountDrawable = new TextRectDrawable(Color.WHITE, textSize, bgColor);
        boolean fRet = false;
        Log.d("PoopDay", date + ": " + dayCount);
        if ( dayCount > 0 ) {
            poopCountDrawable.setText(Integer.toString(dayCount));
            fRet = true;
        }
        return fRet;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(poopCountDrawable);
    }
}
