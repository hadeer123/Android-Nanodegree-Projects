package com.project.android.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.renderscript.RSIllegalArgumentException;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.project.android.bakingapp.data.RecipeContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipeSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String RECIPE_SYNC_TAG = "recipe-sync";

    static boolean sInitialized;
    Context context;

    static void scheduleFirebaseDispatcherSync(final Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncRecipeJob = dispatcher.newJobBuilder()
                .setService(RecipeFirebaseJobService.class)
                .setTag(RECIPE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS ))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncRecipeJob);
    }


    synchronized public static  void initialize(@NonNull  final Context context){
        if(sInitialized) return;
        sInitialized = true;

        scheduleFirebaseDispatcherSync(context);
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri QueryUri = RecipeContract.RecipeEntry.CONTENT_URI;

                Cursor cursor = context.getContentResolver().query(
                        QueryUri,
                        null,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync (@NonNull  final Context context){
        Intent intentToSyncImmediately = new Intent(context, RecipeSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
