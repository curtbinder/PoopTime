/*
 * MIT License
 * Copyright (c) 2020 Curt Binder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package info.curtbinder.pooptime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import androidx.core.content.ContextCompat;

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
            oval.getPaint().setColor(ContextCompat.getColor(ctx, R.color.primaryColor));
        } else {
            oval.getPaint().setColor(Color.TRANSPARENT);
        }
        view.setBackgroundDrawable(oval);
    }
}
