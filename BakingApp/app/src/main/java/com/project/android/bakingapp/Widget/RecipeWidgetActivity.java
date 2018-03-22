package com.project.android.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;

import com.project.android.bakingapp.R;
import com.project.android.bakingapp.RecipeIngredientFragment;
import com.project.android.bakingapp.RecipesFragment;

public class RecipeWidgetActivity extends AppCompatActivity implements RecipesFragment.OnRecipeSelectedListener {

    private AppWidgetManager mAppWidgetManager;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_recipe_widget);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        getSupportFragmentManager().beginTransaction().add(R.id.recipeFrame, RecipesFragment.newInstance(true, this)).commit();

    }


    private void updateWidget(String ingredients, String recipeName) {

        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.recipe_ingredients_widget);

        views.setTextViewText(R.id.appwidget_text, ingredients);
        views.setTextViewText(R.id.RecipeName, recipeName);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    @Override
    public void onRecipeSelected(int recipeID, String recipeName) {

        String ingredients = String.valueOf(RecipeIngredientFragment.queryIngredients(String.valueOf(recipeID), getApplicationContext()));
        updateWidget(ingredients, recipeName);
    }
}
