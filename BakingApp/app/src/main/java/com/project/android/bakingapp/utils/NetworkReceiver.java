package com.project.android.bakingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.android.bakingapp.R;

/**
 * Created by fg7cpt on 3/14/2018.
 */

public class NetworkReceiver extends BroadcastReceiver {
    public static NetworkListener mNetworkListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction;
        if (mNetworkListener != null) {
            intentAction = intent.getAction();
            if (!intent.equals("")) {
                if (intentAction.equals(context.getString(R.string.intent_filter_connectivity_change)) ||
                        intentAction.equals(context.getString(R.string.intent_filter_wifi_state_changed))) {
                    mNetworkListener.onConnectivityChanged(NetworkUtils.isNetworkAvailable(context));
                }
            }
        }
    }


    public interface NetworkListener {
        void onConnectivityChanged(boolean isConnected);
    }


}
