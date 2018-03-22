package com.project.android.bakingapp.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;



public class RecipeFirebaseJobService extends JobService {

    private static AsyncTask<Void, Void, Void> mFetchRecipeTask;

    public RecipeFirebaseJobService() {
        super();
    }

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
