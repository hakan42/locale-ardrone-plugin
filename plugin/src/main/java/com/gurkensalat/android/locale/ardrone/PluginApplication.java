package com.gurkensalat.android.locale.ardrone;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.util.Log;

/**
 * Implements an application object for the plug-in.
 * <p>
 * This application is non-essential for the plug-in's operation; it simply
 * enables debugging options globally for the app.
 */
public final class PluginApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (BuildConfig.DEBUG)
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.v(Constants.LOG_TAG, "Application is debuggable.  Enabling additional debug logging"); //$NON-NLS-1$
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            {
                enableApiLevel9Debugging();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                enableApiLevel11Debugging();
            }

            /*
             * If using the Fragment compatibility library, enable debug logging
             * here
             */
            // android.support.v4.app.FragmentManager.enableDebugLogging(true);
            // android.support.v4.app.LoaderManager.enableDebugLogging(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void enableApiLevel9Debugging()
    {
        android.os.StrictMode.enableDefaults();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void enableApiLevel11Debugging()
    {
        android.app.LoaderManager.enableDebugLogging(true);
        android.app.FragmentManager.enableDebugLogging(true);
    }
}