package com.gurkensalat.android.locale.ardrone.bundle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gurkensalat.android.locale.ardrone.Constants;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE}
 * for this plug-in.
 */
public final class PluginBundleManager
{
    /**
     * Type: {@code String}.
     * <p>
     * String message to display in a Toast message.
     */
    public static final String BUNDLE_EXTRA_STRING_MESSAGE   = "com.yourcompany.yourapp.extra.STRING_MESSAGE";        //$NON-NLS-1$

    /**
     * Type: {@code int}
     * <p>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and
     * forward compatibility significantly easier. For example, suppose a bug is
     * found in how some version of the plug-in stored its Bundle. By having the
     * version, the plug-in can better detect when such bugs occur.
     */
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE = "com.yourcompany.yourcondition.extra.INT_VERSION_CODE"; //$NON-NLS-1$

    /**
     * Method to verify the content of the bundle are correct.
     * <p>
     * This method will not mutate {@code bundle}.
     * 
     * @param bundle
     *            bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(final Bundle bundle)
    {
        if (null == bundle)
        {
            return false;
        }

        /*
         * Make sure the expected extras exist
         */
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_MESSAGE))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("bundle must contain extra %s", BUNDLE_EXTRA_STRING_MESSAGE)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_VERSION_CODE))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the correct number of extras exist. Run this test after
         * checking for specific Bundle extras above so that the error message
         * is more useful. (E.g. the caller will see what extras are missing,
         * rather than just a message that there is the wrong number).
         */
        if (2 != bundle.keySet().size())
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("bundle must contain 2 keys, but currently contains %d keys: %s", bundle.keySet().size(), bundle.keySet())); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the extra isn't null or empty
         */
        if (TextUtils.isEmpty(bundle.getString(BUNDLE_EXTRA_STRING_MESSAGE)))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("bundle extra %s appears to be null or empty.  It must be a non-empty string", BUNDLE_EXTRA_STRING_MESSAGE)); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the extra is the correct type
         */
        if (bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 0) != bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 1))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }

            return false;
        }

        return true;
    }

    /**
     * @param context
     *            Application context.
     * @param message
     *            The toast message to be displayed by the plug-in. Cannot be
     *            null.
     * @return A plug-in bundle.
     */
    public static Bundle generateBundle(final Context context, final String message)
    {
        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putString(BUNDLE_EXTRA_STRING_MESSAGE, message);

        return result;
    }

    /**
     * Private constructor prevents instantiation
     * 
     * @throws UnsupportedOperationException
     *             because this class cannot be instantiated.
     */
    private PluginBundleManager()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}