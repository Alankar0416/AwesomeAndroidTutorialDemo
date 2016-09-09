package com.alankar.awesometutorials;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by alankargupta on 01/09/16.
 */

public class OverlayCanvasView extends View {

    private static final int TRANSPARENT_COLOR = 0x01FFFFFF;
    private final int DIM_COLOR = 0xCC000000;
    private Paint paintDark, paintTransparent;
    private int w, h;
    private Rect rect;
    private DemoOverlayActivity.HOLE hole;

    public OverlayCanvasView(Context context) {
        super(context);
        init();
    }

    public OverlayCanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintDark = new Paint();
        paintDark.setColor(DIM_COLOR);
        paintDark.setAntiAlias(true);
        paintTransparent = new Paint();
        paintTransparent.setColor(TRANSPARENT_COLOR);
        paintTransparent.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paintTransparent.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, w, h, paintDark);
        drawHole(canvas);
    }

    private void drawHole(Canvas canvas) {
        if (rect != null) {
            if (DemoOverlayActivity.HOLE.RECT.equals(hole)) {
                canvas.drawRect(rect, paintTransparent);
            } else if (DemoOverlayActivity.HOLE.CIRCLE.equals(hole)) {
                float radius = Math.min(rect.width(), rect.height()) / 2.0f;
                canvas.drawCircle(rect.centerX(), rect.centerY(), radius, paintTransparent);
            }
        }
    }

    void setHoleRect(Rect rect, DemoOverlayActivity.HOLE hole) {
        this.rect = rect;
        this.hole = hole;
        invalidate();
    }
}
