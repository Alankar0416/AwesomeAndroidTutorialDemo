package com.alankar.awesometutorials;
import android.app.Application;

/**
 * Created by alankargupta on 09/09/16.
 */

public class AwesomeTutorialApplication extends Application {

    private static AwesomeTutorialApplication mInstance;

    public static AwesomeTutorialApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
