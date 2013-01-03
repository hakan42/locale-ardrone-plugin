package com.gurkensalat.android.locale.ardrone;

import android.content.Context;

/**
 * Class of constants used by this Locale plug-in.
 */
public final class Constants
{
    /**
     * Log tag for logcat messages.
     */
    // TODO: Change this to your application's own log tag.
    public static final String  LOG_TAG                            = "ToastSetting";   //$NON-NLS-1$

    /**
     * Flag to enable logcat messages.
     */
    public static final boolean IS_LOGGABLE                        = BuildConfig.DEBUG;

    /**
     * Flag to enable runtime checking of method parameters.
     */
    public static final boolean IS_PARAMETER_CHECKING_ENABLED      = BuildConfig.DEBUG;

    /**
     * Flag to enable runtime checking of whether a method is called on the
     * correct thread.
     */
    public static final boolean IS_CORRECT_THREAD_CHECKING_ENABLED = BuildConfig.DEBUG;

    /**
     * Determines the "versionCode" in the {@code AndroidManifest}.
     * 
     * @param context
     *            to read the versionCode.
     * @return versionCode of the app.
     */
    public static int getVersionCode(final Context context)
    {
        if (Constants.IS_PARAMETER_CHECKING_ENABLED)
        {
            if (null == context)
            {
                throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
            }
        }

        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (final UnsupportedOperationException e)
        {
            /*
             * This exception is thrown by test contexts
             */

            return 1;
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     * 
     * @throws UnsupportedOperationException
     *             because this class cannot be instantiated.
     */
    private Constants()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}