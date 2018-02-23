package com.project.android.bakingapp.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.project.android.bakingapp.data.RecipeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fg7cpt on 2/13/2018.
 */

public class RecipeJsonUtil {


    private static final String TAG = RecipeJsonUtil.class.toString();


    private static void addIngredients ( JSONArray ingredients, int recipeId, ContentResolver recContentResolver) throws JSONException{
        for (int count = 0; count < ingredients.length(); count++) {

            JSONObject ingredient = ingredients.getJSONObject(count);
            ContentValues contentValueIng = new ContentValues();


            contentValueIng.put(RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID, recipeId);
            contentValueIng.put(RecipeContract.RecipeIngredients.COLUMN_MEASURE, ingredient.getString("measure"));
            contentValueIng.put(RecipeContract.RecipeIngredients.COLUMN_QUANTITY, ingredient.getDouble("quantity"));
            contentValueIng.put(RecipeContract.RecipeIngredients.COLUMN_INGREDIENT, ingredient.getString("ingredient"));

            recContentResolver.insert(RecipeContract.RecipeIngredients.CONTENT_URI, contentValueIng);

        }

    }


    private static void addSteps ( JSONArray steps, int recipeId, ContentResolver recContentResolver) throws JSONException{

        for (int count = 0; count < steps.length(); count++) {
            JSONObject stepsObj = steps.getJSONObject(count);

            ContentValues contentValuesSteps = new ContentValues();

            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_RECIPE_ID, recipeId);
            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_STEP_ID, stepsObj.getInt("id"));
            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_DESC, stepsObj.getString("description"));
            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_SHORT_DESC, stepsObj.getString("shortDescription"));
            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_THUMBNAIL_URL, stepsObj.getString("thumbnailURL"));
            contentValuesSteps.put(RecipeContract.RecipeSteps.COLUMN_VIDEO_URL, stepsObj.getString("videoURL"));

            recContentResolver.insert(RecipeContract.RecipeSteps.CONTENT_URI, contentValuesSteps);

        }


    }
    public static void getRecipesContentValuesFromJson(Context context, String recipeJsonStr) throws JSONException{

        JSONArray RecipesJson = new JSONArray(recipeJsonStr);

        ContentValues[] recipesContentValues = new ContentValues[RecipesJson.length()];



        ContentResolver recContentResolver = context.getContentResolver();


        for (int i = 0; i < RecipesJson.length(); i++) {

            ContentValues contentValueRecipes = new ContentValues();
            JSONObject recipe = RecipesJson.getJSONObject(i);
            int recipeId, servings;
            String recipeName, image;


            recipeId = recipe.getInt("id");
            recipeName = recipe.getString("name");
            servings = recipe.getInt("servings");
            image = recipe.getString("image");

            JSONArray ingredients = recipe.getJSONArray("ingredients");

            JSONArray steps = recipe.getJSONArray("steps");



            addIngredients(ingredients,recipeId ,recContentResolver);
            addSteps(steps, recipeId, recContentResolver);


            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, recipeId);
            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGES, image);
            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS, ingredients.length());
            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS, steps.length());
            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, recipeName);
            contentValueRecipes.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, servings);

            recipesContentValues[i] = contentValueRecipes;

        }

        recContentResolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, recipesContentValues);


    }
}
