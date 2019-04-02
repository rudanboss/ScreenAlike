package screenAlike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ToggleButton;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.screenalike.R;
import static android.content.Context.WIFI_SERVICE;
import static screenAlike.BusMessages.MESSAGE_ACTION_STREAMING_STOP;
import static screenAlike.BusMessages.MESSAGE_ACTION_STREAMING_TRY_START;
import static screenAlike.BusMessages.MESSAGE_STATUS_HTTP_ERROR_PORT_IN_USE;
import static screenAlike.BusMessages.MESSAGE_STATUS_HTTP_OK;
import static screenAlike.BusMessages.MESSAGE_STATUS_IMAGE_GENERATOR_ERROR;

public class screenHelper {
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;
    private volatile int mSeverPort;
    private String DEFAULT_SERVER_PORT = "8080";
    private boolean mIsStreaming;
    private static screenHelper sServiceInstance;
    private MediaProjectionManager mMediaProjectionManager;
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    public Activity activity;
    public screenHelper(final Context context){
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSeverPort = Integer.parseInt(mSharedPreferences.getString(mContext.getString(R.string.pref_key_server_port), DEFAULT_SERVER_PORT));
    }

    public String getServerAddress() {
        return "https:/" + getIpAddress() + ":" + mSeverPort;
    }

    public String getIpAddress() {
        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipInt = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ipInt;
    }

    public void setStreaming(final boolean streaming) {
        mIsStreaming = streaming;
    }

    public void onToggleButtonClick(View v) {
        if (mIsStreaming) {
            EventBus.getDefault().post(new BusMessages(MESSAGE_ACTION_STREAMING_STOP));
        } else {
            ((ToggleButton) v).setChecked(false);
            EventBus.getDefault().post(new BusMessages(MESSAGE_ACTION_STREAMING_TRY_START));
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BusMessages busMessage) {
        switch (busMessage.getMessage()) {
            case MESSAGE_ACTION_STREAMING_TRY_START:
                EventBus.getDefault().removeStickyEvent(BusMessages.class);
                if (mIsStreaming) return;
                final MediaProjectionManager projectionManager = getProjectionManager();
                if (projectionManager != null) {
                    activity.startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE);
                }
                break;
            case MESSAGE_STATUS_HTTP_ERROR_PORT_IN_USE:
//                if (!mPortInUseSnackbar.isShown()) mPortInUseSnackbar.show();
//                getMainActivityViewModel().setHttpServerError(true);
                break;
            case MESSAGE_STATUS_HTTP_OK:
//                EventBus.getDefault().removeStickyEvent(BusMessages.class);
//                if (mPortInUseSnackbar.isShown()) mPortInUseSnackbar.dismiss();
//                getMainActivityViewModel().setHttpServerError(false);
                break;
            case MESSAGE_STATUS_IMAGE_GENERATOR_ERROR:
//                EventBus.getDefault().removeStickyEvent(BusMessages.class);
//                EventBus.getDefault().post(new BusMessages(MESSAGE_ACTION_STREAMING_STOP));
//                startActivity(getStartIntent(this).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                if (!isFinishing())
//                    new AlertDialog.Builder(this)
//                            .setTitle(getString(R.string.main_activity_error_title))
//                            .setMessage(getString(R.string.main_activity_error_msg_unknown_format))
//                            .setIcon(R.drawable.ic_main_activity_error_24dp)
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
                break;
        }
    }

    @Nullable
    public static MediaProjectionManager getProjectionManager() {
        return sServiceInstance == null ? null : sServiceInstance.mMediaProjectionManager;
    }


}
