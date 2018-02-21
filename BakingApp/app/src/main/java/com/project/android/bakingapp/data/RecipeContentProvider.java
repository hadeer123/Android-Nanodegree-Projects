package com.project.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Hadeer Khalifa on 2/13/2018.
 */

public class RecipeContentProvider extends ContentProvider {

    private RecipeDbHelper mRecipeDbHelper;
    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;

    public static final int INGREDIENTS = 200;
    public static final int INGREDIENTS_WITH_ID=201;

    public static final int STEPS = 300;
    public static final int STEPS_WITH_ID=301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES+"/#", RECIPES_WITH_ID);

        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGREDIENTS+"/#", INGREDIENTS_WITH_ID);

        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEPS+"/#", STEPS_WITH_ID);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mRecipeDbHelper = new RecipeDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        String id, mSelection;
        String [] mSelectionArgs;
        Cursor returnCursor;
        switch (match){
            case RECIPES:
                returnCursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPES_WITH_ID:
                 id = uri.getPathSegments().get(1);
                 mSelection = "recipeID=?";
                 mSelectionArgs= new String[]{id};
                returnCursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case INGREDIENTS:
                returnCursor = db.query(RecipeContract.RecipeIngredients.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INGREDIENTS_WITH_ID:
                 id = uri.getPathSegments().get(1);
                 mSelection = "recipeID=?";
                  mSelectionArgs= new String[]{id};
                returnCursor = db.query(RecipeContract.RecipeIngredients.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case STEPS:
                returnCursor = db.query(RecipeContract.RecipeSteps.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STEPS_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = "recipeID=?";
                mSelectionArgs= new String[]{id};
                returnCursor = db.query(RecipeContract.RecipeSteps.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+ uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPES:
                // directory
                return "vnd.android.cursor.dir" + "/" + RecipeContract.AUTHORITY + "/" + RecipeContract.PATH_RECIPES;
            case RECIPES_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + RecipeContract.AUTHORITY + "/" + RecipeContract.PATH_RECIPES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match){
            case RECIPES:
                 id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                }else{
                    throw  new android.database.SQLException("Failed to insert row into"+ uri);
                }
                break;
            case INGREDIENTS:
                 id = db.insert(RecipeContract.RecipeIngredients.TABLE_NAME, null, contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeIngredients.CONTENT_URI, id);
                }else{
                    throw  new android.database.SQLException("Failed to insert row into"+ uri);
                }
                break;
            case STEPS:
                id = db.insert(RecipeContract.RecipeSteps.TABLE_NAME, null, contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeSteps.CONTENT_URI, id);
                }else{
                    throw  new android.database.SQLException("Failed to insert row into"+ uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+ uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private int bulkInsert(SQLiteDatabase db,ContentValues [] values, String tableName, Uri uri){
        int rowsInserted;
        db.beginTransaction();
        rowsInserted = 0;
        try{
            for(ContentValues value : values){

                long _id = -1;
                try {
                    _id = db.insert(tableName, null, value);
                }catch (Exception e){
                    Log.e("error", "r");
                }

                if(_id != -1){
                    rowsInserted++;
                }

            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        if(rowsInserted>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case RECIPES:
                return bulkInsert(db, values,RecipeContract.RecipeEntry.TABLE_NAME, uri );
            case INGREDIENTS:
                return bulkInsert(db, values,RecipeContract.RecipeIngredients.TABLE_NAME, uri );
            case STEPS:
                return bulkInsert(db, values, RecipeContract.RecipeSteps.TABLE_NAME, uri );
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int RecipesUpdated;
        String id;
        int match = sUriMatcher.match(uri);
        switch (match){
            case RECIPES_WITH_ID:
                 id = uri.getPathSegments().get(1);
                RecipesUpdated = mRecipeDbHelper.getWritableDatabase().update(RecipeContract.RecipeEntry.TABLE_NAME, contentValues, "recipeID=?", new String[]{id});
                break;
            case INGREDIENTS_WITH_ID:
                 id = uri.getPathSegments().get(1);
                RecipesUpdated = mRecipeDbHelper.getWritableDatabase().update(RecipeContract.RecipeIngredients.TABLE_NAME, contentValues, "recipeID=?", new String[]{id});
                break;
            case STEPS_WITH_ID:
                id = uri.getPathSegments().get(1);
                RecipesUpdated = mRecipeDbHelper.getWritableDatabase().update(RecipeContract.RecipeSteps.TABLE_NAME, contentValues, "recipeID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+ uri);

        }

        if(RecipesUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }
}
