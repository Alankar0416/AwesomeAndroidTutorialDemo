package alankar.com.androidtutorialscreendemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alankar.awesometutorials.DemoBuilder;
import com.alankar.awesometutorials.DemoOverlayActivity;

public class ExampleActivity extends AppCompatActivity implements DemoBuilder.DemoCallback {

    int curr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        setupTopDemo();
    }

    private void setupTopDemo() {
        new DemoBuilder(this).setHoleWithView(findViewById(R.id.center_text)).
                setHoleType(DemoOverlayActivity.HOLE.RECT).withText("Hello from Top").
                setMarginInDp(10).setCallback(this).buildAndShowDelayed(DemoBuilder.IDEAL_SETTLING_TIME);
    }

    private void setupRightDemo() {
        new DemoBuilder(this).setHoleWithView(findViewById(R.id.right_text)).
                setHoleType(DemoOverlayActivity.HOLE.RECT).withText("Hello from Right").
                setMarginInDp(10).setCallback(this).buildAndShowDelayed(DemoBuilder.IDEAL_SETTLING_TIME);
    }

    private void setupLeftDemo() {
        new DemoBuilder(this).setHoleWithView(findViewById(R.id.left_text)).
                setHoleType(DemoOverlayActivity.HOLE.RECT).withText("Hello from Left").
                setMarginInDp(10).setCallback(this).buildAndShowDelayed(DemoBuilder.IDEAL_SETTLING_TIME);
    }

    private void setupBottomDemo() {
        new DemoBuilder(this).setHoleWithView(findViewById(R.id.bottom_text)).
                setHoleType(DemoOverlayActivity.HOLE.RECT).withText("Hello from Bottom").
                setMarginInDp(10).setCallback(this).buildAndShowDelayed(DemoBuilder.IDEAL_SETTLING_TIME);
    }

    private void setupFABDemo() {
        new DemoBuilder(this).setHoleWithView(findViewById(R.id.fab)).
                setHoleType(DemoOverlayActivity.HOLE.CIRCLE).withText("Hello from fab").
                setMarginInDp(10).setCallback(this).buildAndShowDelayed(DemoBuilder.IDEAL_SETTLING_TIME);
    }






    @Override
    public void onDemoDismissed() {
        if(curr == 0){
            setupRightDemo();
            curr++;
        }else if(curr == 1){
            setupLeftDemo();
            curr++;
        }else if(curr == 2){
            setupBottomDemo();
            curr++;
        }else if(curr == 3){
            setupFABDemo();
            curr++;
        }
    }
}
