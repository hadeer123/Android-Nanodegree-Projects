package com.project.android.bakingapp.sync;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;

import com.project.android.bakingapp.MainActivity;
import com.project.android.bakingapp.R;
import com.project.android.bakingapp.data.RecipeContract;
import com.project.android.bakingapp.utils.NetworkUtils;
import com.project.android.bakingapp.utils.RecipeJsonUtil;

import java.net.URL;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipeSyncTask  {
    private static final int RECIPE_NOTIFICATION_ID = 4000 ;


    private final static String BAKING_RECIPES_URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

   synchronized public static void syncRecipe(Context context){
        try {

            URL getRecipes = NetworkUtils.buildURL(BAKING_RECIPES_URL);

            String  SearchResults = NetworkUtils.getResponseFromHttpUrl(getRecipes);

            ContentValues [] recipeValues = RecipeJsonUtil
                    .getRecipesContentValuesFromJson(context, SearchResults);

            ContentValues  ingredientsValues = RecipeJsonUtil.getIngredientsContentValue();
            ContentValues  stepsValues = RecipeJsonUtil.getStepsContentValue();

            if (recipeValues != null && recipeValues.length != 0 && ingredientsValues != null  && stepsValues != null ) {
                ContentResolver recContentResolver = context.getContentResolver();

                //recContentResolver.delete(RecipeContract.RecipeEntry.CONTENT_URI,null,null);

                recContentResolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);


                //   recContentResolver.delete(RecipeContract.RecipeIngredients.CONTENT_URI,null,null);

                recContentResolver.insert(RecipeContract.RecipeIngredients.CONTENT_URI, ingredientsValues);


                //   recContentResolver.delete(RecipeContract.RecipeSteps.CONTENT_URI,null,null);

                recContentResolver.insert(RecipeContract.RecipeSteps.CONTENT_URI, stepsValues);

            }

            String notificationTitle = context.getString(R.string.app_name);
            String notificationText = " you have new recipes";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            Intent openApp = new Intent(context, MainActivity.class);


            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(openApp);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(RECIPE_NOTIFICATION_ID, notificationBuilder.build());


        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());

        }
    }


}
