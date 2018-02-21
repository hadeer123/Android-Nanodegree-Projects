package com.project.android.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.project.android.bakingapp.data.RecipeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by fg7cpt on 2/13/2018.
 */

public class RecipeJsonUtil {
    private static final String TAG = RecipeJsonUtil.class.toString();


    public static ContentValues getIngredientsContentValue() {
        return ingredientsContentValue;
    }

    public static ContentValues getStepsContentValue() {
        return stepsContentValue;
    }



    private static ContentValues ingredientsContentValue, stepsContentValue;

    public static ContentValues [] getRecipesContentValuesFromJson(Context context, String recipeJsonStr)     throws JSONException {

        JSONArray RecipesJson = new JSONArray(recipeJsonStr);

        ContentValues[] recipesContentValues = new ContentValues[RecipesJson.length()];

        for( int i= 0; i<RecipesJson.length(); i++){
            JSONObject recipe = RecipesJson.getJSONObject(i);
            int recipeId, servings;
            String recipeName, image;
            int ingredientsLength, stepsLength;

            double quantity = 0;
            String measure = "";
            String ingredientStr = "";

            int stepId = 0;
            String shortDesc = "";
            String desc = "";
            String videoURL = "";
            String thumbnailURL = "";

            recipeId = recipe.getInt("id");
            recipeName = recipe.getString("name");
            servings = recipe.getInt("servings");
            image = recipe.getString("image");

            JSONArray ingredients = recipe.getJSONArray("ingredients");
            ingredientsLength = ingredients.length();

            JSONArray steps = recipe.getJSONArray("steps");
            stepsLength = steps.length();

            for (int j= 0; j< ingredients.length(); j++){
            JSONObject ingredient = ingredients.getJSONObject(i);
                quantity = ingredient.getDouble("quantity");
                measure = ingredient.getString("measure");
                ingredientStr = ingredient.getString("ingredient");
            }

            for (int k= 0; k< steps.length(); k++){
                JSONObject stepsObj = steps.getJSONObject(i);
                stepId = stepsObj.getInt("id");
                shortDesc = stepsObj.getString("shortDescription");
                desc = stepsObj.getString("description");
                videoURL = stepsObj.getString("videoURL");
                thumbnailURL = stepsObj.getString("thumbnailURL");

            }

            ingredientsLength = ingredients.length();
            stepsLength = steps.length();

            ContentValues recipeValues = new ContentValues();
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,recipeId);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGES,image);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS,ingredientsLength);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS,stepsLength);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipeName);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS,servings);

            ContentValues ingredientsValues = new ContentValues();
            ingredientsValues.put(RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID,recipeId);
            ingredientsValues.put(RecipeContract.RecipeIngredients.COLUMN_MEASURE,measure);
            ingredientsValues.put(RecipeContract.RecipeIngredients.COLUMN_QUANTITY,quantity);
            ingredientsValues.put(RecipeContract.RecipeIngredients.COLUMN_INGREDIENT,ingredientStr);

            ingredientsContentValue = ingredientsValues;


            ContentValues stepsValues = new ContentValues();
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_RECIPE_ID,recipeId);
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_STEP_ID,stepId);
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_DESC,desc);
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_SHORT_DESC,shortDesc);
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_THUMBNAIL_URL,thumbnailURL);
            stepsValues.put(RecipeContract.RecipeSteps.COLUMN_VIDEO_URL,videoURL);

            stepsContentValue  = stepsValues;


            recipesContentValues [i]= recipeValues;
        }


        return recipesContentValues;

    }
}
