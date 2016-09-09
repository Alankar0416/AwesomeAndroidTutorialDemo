package com.alankar.awesometutorials;

/**
 * Created by alankargupta on 07/09/16.
 */

public class DemoPolicy {

    public static boolean shouldShowDemo(DemoType demoType) {
        return BuildConfig.DEBUG || demoType != null && SharedPreferenceManager.getInstance(AwesomeTutorialApplication.getInstance()).
                getSharedPreference().getBoolean(demoType.getKey(), true);
    }

    public static void updateDemoShownStatus(DemoType demo, boolean status) {
        if (demo == null) {
            return;
        }

        SharedPreferenceManager.getInstance(AwesomeTutorialApplication.getInstance()).getSharedPreference()
                .edit().putBoolean(demo.getKey(), status).apply();
    }
}
