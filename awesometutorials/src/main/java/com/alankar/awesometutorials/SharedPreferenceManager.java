package com.alankar.awesometutorials;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alankar Gupta on 26/02/16.
 */
public class SharedPreferenceManager {
    private static SharedPreferenceManager ourInstance;
    private final SharedPreferences sharedPreference;
    private final String TAG = "AwesomeTutorials";

    private SharedPreferenceManager(Context context) {
        sharedPreference = context.getSharedPreferences(TAG,
                Context.MODE_PRIVATE);
    }

    public synchronized static SharedPreferenceManager getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (SharedPreferenceManager.class) {
                if (ourInstance == null)
                    ourInstance = new SharedPreferenceManager(context);
            }
        }
        return ourInstance;
    }

    public SharedPreferences getSharedPreference() {
        return sharedPreference;
    }


}
