package com.project.android.bakingapp.sync;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by fg7cpt on 2/21/2018.
 */

public class RecipeFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchRecipeTask;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchRecipeTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                RecipeSyncTask.syncRecipe(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                jobFinished(jobParameters, false);
            }
        };

        mFetchRecipeTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchRecipeTask != null) {
            mFetchRecipeTask.cancel(true);
        }
        return true;
    }
}
