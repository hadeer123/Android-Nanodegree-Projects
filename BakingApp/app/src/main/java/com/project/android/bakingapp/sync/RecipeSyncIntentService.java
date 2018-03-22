package com.project.android.bakingapp.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipeSyncIntentService extends IntentService {

    public RecipeSyncIntentService() {
        super("RecipeSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RecipeSyncTask.syncRecipe(this);
    }
}
