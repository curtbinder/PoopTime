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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class DayDecorator implements DayViewDecorator {

    private final Context ctx;
    private Drawable bg;

    public DayDecorator(Context ctx) {
        this.ctx = ctx;
        ShapeDrawable square = new ShapeDrawable(new RectShape());
        square.getPaint().setColor(ContextCompat.getColor(ctx, R.color.primaryColor));
        bg = square;
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
        view.setBackgroundDrawable(bg);
    }
}
