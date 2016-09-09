package com.alankar.awesometutorials;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Random;

import static com.alankar.awesometutorials.DemoOverlayActivity.HOLE_TYPE;
import static com.alankar.awesometutorials.DemoOverlayActivity.ID;
import static com.alankar.awesometutorials.DemoOverlayActivity.RECT;
import static com.alankar.awesometutorials.DemoOverlayActivity.TEXT;

/**
 * Created by alankargupta on 06/09/16.
 */

public class DemoBuilder {

    public static final int IDEAL_SETTLING_TIME = 600;
    private DemoCallback callback;
    private Activity activity;
    private Rect holeDimension;
    private String text = "Sample Tooltip Text";
    private DemoOverlayActivity.HOLE holeType = DemoOverlayActivity.HOLE.RECT;
    private View holeComputedView;
    private int margin;
    private int id;

    public DemoBuilder(Activity activity) {
        this.activity = activity;
    }

    public DemoBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public DemoBuilder setHoleRectangle(Rect holeDimension) {
        this.holeDimension = holeDimension;
        return this;
    }

    private void computeHoleDimension() {
        if (holeDimension != null) {
            return;
        } else if (holeComputedView != null) {
            holeComputedView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        holeComputedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        holeComputedView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    int[] coordinate = new int[2];
                    holeComputedView.getLocationOnScreen(coordinate);
                    holeDimension = new Rect(coordinate[0], coordinate[1], coordinate[0] + holeComputedView.getWidth(), coordinate[1] + holeComputedView.getHeight());
                }
            });
        } else {
            throw new IllegalStateException("Client must pass hole dimension");
        }
    }

    public DemoBuilder setHoleWithView(final View view) {
        holeComputedView = view;
        return this;
    }

    public DemoBuilder setMarginInDp(int marginInDp) {
        this.margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp,
                activity.getResources().getDisplayMetrics());
        return this;
    }

    public DemoBuilder setCallback(DemoCallback callback) {
        this.callback = callback;
        return this;
    }

    public DemoBuilder setHoleType(DemoOverlayActivity.HOLE holeType) {
        this.holeType = holeType;
        return this;
    }

    public void buildAndShowDelayed(int milliSeconds) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
                toggleUserInputBlock(false);
            }
        }, milliSeconds);
        computeHoleDimension();
        toggleUserInputBlock(true);
    }

    private void show() {
        Intent intent = new Intent(activity, DemoOverlayActivity.class);
        intent.putExtra(RECT, getHoleDimension());
        intent.putExtra(HOLE_TYPE, holeType);
        intent.putExtra(TEXT, text);
        id = new Random().nextInt();
        intent.putExtra(ID, id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, 0);
        EventBus.getDefault().register(this);
    }

    private Rect getHoleDimension() {
        if (holeDimension == null) {
            return null;
        }
        holeDimension.left -= margin;
        holeDimension.right += margin;
        holeDimension.top -= margin;
        holeDimension.bottom += margin;
        return holeDimension;
    }

    @Subscribe
    public void onDemoOver(DemoOverEvent demoOverEvent) {
        if (callback != null && demoOverEvent.getTag() == id) {
            callback.onDemoDismissed();
        }
    }

    private void toggleUserInputBlock(boolean block) {
        if (block) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public interface DemoCallback {
        void onDemoDismissed();
    }

    static class DemoOverEvent {
        private final int tag;

        DemoOverEvent(int tag) {
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }
    }

}
