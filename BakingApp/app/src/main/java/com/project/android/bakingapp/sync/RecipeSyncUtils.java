package com.project.android.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.renderscript.RSIllegalArgumentException;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipeSyncUtils {
    static boolean sInitialized;
    Context context;

    synchronized public static  void initialize(Context context){
        if(sInitialized) return;
        sInitialized = true;
    }

    public void startImmediateSync (final Context context){
        Intent intentToSyncImmediately = new Intent(context, RecipeSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
