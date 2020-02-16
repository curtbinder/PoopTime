package info.curtbinder.pooptime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextRectDrawable extends Drawable {

    private String text;
    private Paint paintText;
    private Paint bg;

    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    private final int leftPadding = 8;
    private final int bottomPadding = 10;

    public TextRectDrawable(String text, int textColor, float textSize, int bgColor) {
        setDefaults(textColor, textSize, bgColor);
        setText(text);
    }

    public TextRectDrawable(int textColor, float textSize, int bgColor) {
        setDefaults(textColor, textSize, bgColor);
    }

    private void setDefaults(int textColor, float textSize, int bgColor) {
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(textColor);
        paintText.setShadowLayer(6f, 0, 0, Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.setTextSize(textSize);

        bg = new Paint();
        bg.setColor(bgColor);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawPaint(bg);
        canvas.drawText(text, 0, text.length(),
                bounds.left+leftPadding, bounds.bottom-bottomPadding,
                paintText);
    }

    public void setText(String text) {
        this.text = text;
        updateIntrinsics();
    }

    private void updateIntrinsics() {
        // call after setting the text
        mIntrinsicWidth = (int) (paintText.measureText(text, 0, text.length()) + .5);
        mIntrinsicHeight = paintText.getFontMetricsInt(null);
    }

    @Override
    public void setAlpha(int i) {
        paintText.setAlpha(i);
        bg.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paintText.setColorFilter(colorFilter);
        bg.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return paintText.getAlpha();
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }
    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }
}
