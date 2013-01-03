package com.gurkensalat.android.locale.ardrone.ui;

// import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.twofortyfouram.locale.BreadCrumber;
import com.gurkensalat.android.locale.ardrone.Constants;
import com.gurkensalat.android.locale.ardrone.R;
import com.gurkensalat.android.locale.ardrone.bundle.BundleScrubber;
import com.gurkensalat.android.locale.ardrone.bundle.PluginBundleManager;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 */
public final class EditActivity extends Activity
{
    /**
     * Flag boolean that can only be set to true via the "Don't Save"
     * {@link com.twofortyfouram.locale.platform.R.id#twofortyfouram_locale_menu_dontsave}
     * menu item in {@link #onMenuItemSelected(int, MenuItem)}.
     * <p>
     * There is no need to save/restore this field's state when the
     * {@code Activity} is paused.
     */
    private boolean mIsCancelled = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(getIntent());
        BundleScrubber.scrub(getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setupTitleApi11();
        }
        else
        {
            setTitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
        }

        /*
         * if savedInstanceState is null, then then this is a new Activity
         * instance and a check for EXTRA_BUNDLE is needed
         */
        if (null == savedInstanceState)
        {
            final Bundle forwardedBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
            if (PluginBundleManager.isBundleValid(forwardedBundle))
            {
                ((EditText) findViewById(android.R.id.text1)).setText(forwardedBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE));
            }
        }
        /*
         * if savedInstanceState isn't null, there is no need to restore any
         * Activity state directly via onSaveInstanceState(), as the EditText
         * object handles that automatically
         */
    }

    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupTitleApi11()
    {
        CharSequence callingApplicationLabel = null;
        try
        {
            callingApplicationLabel = getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getCallingPackage(), 0));
        }
        catch (final NameNotFoundException e)
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, "Calling package couldn't be found", e); //$NON-NLS-1$
            }
        }
        if (null != callingApplicationLabel)
        {
            setTitle(callingApplicationLabel);
        }
    }

    @Override
    public void finish()
    {
        if (mIsCancelled)
        {
            setResult(RESULT_CANCELED);
        }
        else
        {
            final String message = ((EditText) findViewById(android.R.id.text1)).getText().toString();

            /*
             * If the message is of 0 length, then there isn't a setting to
             * save.
             */
            if (0 == message.length())
            {
                setResult(RESULT_CANCELED);
            }
            else
            {
                /*
                 * This is the result Intent to Locale
                 */
                final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity
                 * or the BroadcastReceiver. Note that anything placed in this
                 * Bundle must be available to Locale's class loader. So storing
                 * String, int, and other standard objects will work just fine.
                 * However Parcelable objects must also be Serializable. And
                 * Serializable objects must be standard Java objects (e.g. a
                 * private subclass to this plug-in cannot be stored in the
                 * Bundle, as Locale's classloader will not recognize it).
                 */
                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, PluginBundleManager.generateBundle(getApplicationContext(), message));

                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, generateBlurb(getApplicationContext(), message));

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.twofortyfouram_locale_help_save_dontsave, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setupActionBarApi11();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            setupActionBarApi14();
        }

        return true;
    }

    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBarApi11()
    {
        getActionBar().setSubtitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
    }

    // @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setupActionBarApi14()
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * Note: There is a small TOCTOU error here, in that the host could be
         * uninstalled right after launching the plug-in. That would cause
         * getApplicationIcon() to return the default application icon. It won't
         * fail, but it will return an incorrect icon.
         * 
         * In practice, the chances that the host will be uninstalled while the
         * plug-in UI is running are very slim.
         */
        try
        {
            getActionBar().setIcon(getPackageManager().getApplicationIcon(getCallingPackage()));
        }
        catch (final NameNotFoundException e)
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.w(Constants.LOG_TAG, "An error occurred loading the host's icon", e); //$NON-NLS-1$
            }
        }
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item)
    {
        final int id = item.getItemId();

        if (android.R.id.home == id)
        {
            finish();
            return true;
        }
        else if (R.id.twofortyfouram_locale_menu_dontsave == id)
        {
            mIsCancelled = true;
            finish();
            return true;
        }
        else if (R.id.twofortyfouram_locale_menu_save == id)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param context
     *            Application context.
     * @param message
     *            The toast message to be displayed by the plug-in. Cannot be
     *            null.
     * @return A blurb for the plug-in.
     */
    /* package */static String generateBlurb(final Context context, final String message)
    {
        final int maxBlurbLength = context.getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length);

        if (message.length() > maxBlurbLength)
        {
            return message.substring(0, maxBlurbLength);
        }

        return message;
    }
}