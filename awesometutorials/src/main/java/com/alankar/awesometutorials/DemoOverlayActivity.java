package com.alankar.awesometutorials;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.venmo.view.ArrowAlignment;
import com.venmo.view.TooltipView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by alankargupta on 31/08/16.
 */

public class DemoOverlayActivity extends AppCompatActivity {

    //Intent keys
    static final String HOLE_TYPE = "type";
    static final String RECT = "rect";
    static final String TEXT = "text";
    static final String ID = "id";

    private int marginDP = 10, yOffset = 200;
    private OverlayCanvasView overlayCanvasView;
    private RelativeLayout parent;
    private int id;
    private boolean isTooltipHavingDownArrow;
    private float arrowMargin;
    private RectF allowedTooltipBoundsRect;
    private int statusBarHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_activity);
        inflateUI();
        setupDemo();
    }


    private void inflateUI() {
        overlayCanvasView = (OverlayCanvasView) findViewById(R.id.parent);
        parent = ((RelativeLayout) findViewById(R.id.container));
    }

    private void setupDemo() {
        statusBarHeight = getStatusBarHeightFromResource();
        final Rect dimension = getIntent().getParcelableExtra(RECT);
        final HOLE holeType = (HOLE) getIntent().getSerializableExtra(HOLE_TYPE);
        final String text = getIntent().getStringExtra(TEXT);
        id = getIntent().getIntExtra(ID, 0);
        Rect hole = new Rect(dimension.left, dimension.top - statusBarHeight, dimension.right, dimension.bottom - statusBarHeight);

        overlayCanvasView.setHoleRect(hole, holeType == null ? HOLE.RECT : holeType);
        overlayCanvasView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    overlayCanvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    overlayCanvasView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                layoutTooltip(dimension);
            }
        });

        arrowMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDP, getResources().getDisplayMetrics());
        Point p = Util.getScreenDimensionInPixel(DemoOverlayActivity.this);
        float screenWidth = p.x;
        float screenHeight = p.y;
        float allowedXMin = 0 + arrowMargin;
        float allowedXMax = screenWidth - arrowMargin;
        float allowedYMin = 0 + arrowMargin + yOffset;
        float allowedYMax = screenHeight - arrowMargin - yOffset;
        allowedTooltipBoundsRect = new RectF(allowedXMin, allowedYMin, allowedXMax, allowedYMax);

        initTooltipView(dimension, statusBarHeight, text, arrowMargin);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDemo();
            }
        });
    }

    private void initTooltipView(Rect dimension, int statusBarHeight, String text, float arrowMargin) {
        float y = dimension.bottom - statusBarHeight + arrowMargin;
        TooltipView tooltipView;
        if (y > allowedTooltipBoundsRect.bottom) {
            //Need to draw tooltip on top of the hole with downwards arrow
            tooltipView = (TooltipView) LayoutInflater.from(this).inflate(R.layout.tooltip_down_arrow, parent, false);
            isTooltipHavingDownArrow = true;
        } else {
            //Need to draw tooltip below hole with up arrow
            tooltipView = (TooltipView) LayoutInflater.from(this).inflate(R.layout.tooltip_top_arrow, parent, false);
            isTooltipHavingDownArrow = false;
        }
        tooltipView.setText(text);
        tooltipView.setArrowAlignment(ArrowAlignment.CENTER);
        ((RelativeLayout) findViewById(R.id.container)).addView(tooltipView);
    }

    private void layoutTooltip(Rect dimension) {
        TooltipView tooltip = (TooltipView) findViewById(R.id.tooltip_id);
        tooltip.setY(computeTooltipY(dimension, tooltip));
        tooltip.setX(computeTooltipX(dimension, tooltip));
    }

    private float computeTooltipY(Rect dimension, TooltipView tooltip) {
        float y;
        if (isTooltipHavingDownArrow) {
            y = dimension.top - statusBarHeight - arrowMargin - tooltip.getHeight();
        } else {
            y = dimension.bottom - statusBarHeight + arrowMargin;
        }
        return y;
    }

    private float computeTooltipX(Rect dimension, TooltipView tooltip) {
        float x = dimension.centerX() - tooltip.getWidth() / 2.0f;
        if (x < allowedTooltipBoundsRect.left) {
            float offset = allowedTooltipBoundsRect.left - x;
            tooltip.setArrowAlignment(ArrowAlignment.START);
            tooltip.setAlignmentOffset((int) (tooltip.getWidth() / 2.0f - offset));
            x = allowedTooltipBoundsRect.left;
        } else if (x + tooltip.getWidth() > allowedTooltipBoundsRect.right) {
            float offset = x + tooltip.getWidth() - allowedTooltipBoundsRect.right;
            tooltip.setArrowAlignment(ArrowAlignment.END);
            tooltip.setAlignmentOffset((int) (tooltip.getWidth() / 2.0f - offset));
            x = allowedTooltipBoundsRect.right - tooltip.getWidth();
        }
        return x;
    }

    private int getStatusBarHeightFromResource() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        endDemo();
    }

    private void endDemo() {
        finish();
        overridePendingTransition(0, R.anim.fade_out);
        EventBus.getDefault().post(new DemoBuilder.DemoOverEvent(id));
    }

    public enum HOLE implements Serializable {RECT, CIRCLE}
}
