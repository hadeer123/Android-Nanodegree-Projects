package com.project.android.bakingapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.project.android.bakingapp.data.RecipeContract;
import com.project.android.bakingapp.sync.RecipeSyncUtils;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecipesAdapter.RecipeAdapterOnClickHandler {


    private RecipesAdapter mRecipesAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;
    private static final int ID_RECIPE_LOADER = 44;
    boolean orientationLand;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecipesRecyclerView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE )
            manager = new GridLayoutManager(this,3);
        else
            manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setHasFixedSize(false);

        mRecipesAdapter = new RecipesAdapter(this, this);

        mRecyclerView.setAdapter(mRecipesAdapter);

        showLoading();

        initLoader();

        RecipeSyncUtils.initialize(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationLand = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE )? true : false;

    }


    public void initLoader(){
        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }

    @Override
    public void onClick(int recipeID, String recipeName) {
        Intent recipeDetailIntent = new Intent(MainActivity.this, StepListActivity.class);
        Uri uriForRecipe = RecipeContract.RecipeSteps.CONTENT_URI.buildUpon().appendPath(Integer.toString(recipeID)).build();
        recipeDetailIntent.setData(uriForRecipe);
        recipeDetailIntent.putExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipeName);
        startActivity(recipeDetailIntent);
    }

    private void showLoading() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        switch (id) {

            case ID_RECIPE_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri recipeUrl = RecipeContract.RecipeEntry.CONTENT_URI;

                return new CursorLoader(this,
                        recipeUrl,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecipesAdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showRecipeView();
    }

    private void showRecipeView() {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipesAdapter.swapCursor(null);
    }


}
