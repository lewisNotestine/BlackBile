package com.lnotes.grrr;

import android.app.Application;
import android.content.Context;

/**
 * <p>
 * Custom application.
 * </p>
 * Created by LN_1 on 12/12/13.
 */
public class GrrrApplication extends Application {

    private static Context mAppContext;


    @Override
    public void onCreate() {
        mAppContext = this.getApplicationContext();
    }

    public static Context getLoggrrrApplicationContext() {
        return mAppContext;
    }
}
