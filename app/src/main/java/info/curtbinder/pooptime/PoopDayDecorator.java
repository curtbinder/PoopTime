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

    private final Context ctx;
//    private final int bgColor;

    private int dayCount;
//    private TextDrawable poopDrawable;
//    private LayerDrawable container;
    private Drawable bg;
    private int inset;

    public PoopDayDecorator(Context ctx) {
        this.ctx = ctx;
//        bgColor = ContextCompat.getColor(ctx, R.color.primaryColor);
        dayCount = 0;

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
        dayCount = DBCommands.getPoopCountForDay(ctx, date);
        boolean fRet = false;
//        Log.d("PoopDay", date + ": " + dayCount);
        if ( dayCount > 0 ) {
//            poopDrawable = new TextDrawable(ctx);
//            poopDrawable.setText(Integer.toString(dayCount));
//            container = new LayerDrawable(new Drawable[]{bg, poopDrawable});
//            container.setLayerInset(1, inset, inset, 0, 0);
            fRet = true;
//        } else {
//            container = new LayerDrawable(new Drawable[]{bg});
        }
        return fRet;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(bg);
    }
}
