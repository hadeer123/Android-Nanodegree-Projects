package com.project.android.bakingapp;

import android.app.Application;
import android.content.IntentFilter;

import com.project.android.bakingapp.utils.NetworkReceiver;

/**
 * Created by fg7cpt on 3/15/2018.
 */

public class MainApplication extends Application {


    private static IntentFilter mIntentFilter;
    private static NetworkReceiver networkReceiver;
    private static MainApplication mApplicationInstance;

    private static synchronized MainApplication getApplicationInstance() {
        return mApplicationInstance;
    }

    public static void setNetworkListener(NetworkReceiver.NetworkListener listener) {
        NetworkReceiver.mNetworkListener = listener;
    }

    public static void setReceiverStatus(boolean isActivityVisible) {
        if (isActivityVisible) {

            getApplicationInstance().registerReceiver(networkReceiver, mIntentFilter);
        } else {

            getApplicationInstance().unregisterReceiver(networkReceiver);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationInstance = this;
        networkReceiver = new NetworkReceiver();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(getString(R.string.intent_filter_connectivity_change));
        mIntentFilter.addAction(getString(R.string.intent_filter_wifi_state_changed));

        setReceiverStatus(true);
    }
}
