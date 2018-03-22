package com.project.android.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fg7cpt on 2/13/2018.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_INGREDIENTS + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_STEPS + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGES + " TEXT NOT NULL " + ");";


        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeIngredients.TABLE_NAME + " (" +
                RecipeContract.RecipeIngredients._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeIngredients.COLUMN_QUANTITY + " DOUBLE NOT NULL, " +
                RecipeContract.RecipeIngredients.COLUMN_MEASURE + " TEXT NOT NULL, " +
                RecipeContract.RecipeIngredients.COLUMN_INGREDIENT + " TEXT NOT NULL " + ");";

        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeSteps.TABLE_NAME + " (" +
                RecipeContract.RecipeSteps._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeSteps.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeSteps.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                RecipeContract.RecipeSteps.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                RecipeContract.RecipeSteps.COLUMN_DESC + " TEXT NOT NULL, " +
                RecipeContract.RecipeSteps.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +
                RecipeContract.RecipeSteps.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL "
                + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEPS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeSteps.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeIngredients.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
