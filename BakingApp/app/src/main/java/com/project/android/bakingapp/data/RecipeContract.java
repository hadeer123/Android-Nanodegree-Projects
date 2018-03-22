package com.project.android.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hadeer Khalifa on 2/13/2018.
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.project.android.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class RecipeEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_RECIPE_NAME = "recipeName";
        public static final String COLUMN_RECIPE_INGREDIENTS = "recipeIngredients";
        public static final String COLUMN_RECIPE_STEPS = "recipeSteps";
        public static final String COLUMN_RECIPE_SERVINGS = "recipeServings";
        public static final String COLUMN_RECIPE_IMAGES = "recipeImages";
    }


    public static final class RecipeIngredients implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        public static final String COLUMN_RECIPE_ID = "recipeID";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static String TABLE_NAME = "recipeIngredientsTable";

    }

    public static final class RecipeSteps implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();
        public static final String COLUMN_RECIPE_ID = "recipeID";
        public static final String COLUMN_STEP_ID = "stepID";
        public static final String COLUMN_SHORT_DESC = "shortDesc";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailURL";
        public static String TABLE_NAME = "recipeStepsTable";

    }

}
