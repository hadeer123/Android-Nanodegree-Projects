package com.project.android.bakingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.project.android.bakingapp.data.RecipeContract;
import com.project.android.bakingapp.utils.NetworkUtils;
import com.project.android.bakingapp.utils.RecipeJsonUtil;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecipeAdapter.RecipeAdapterOnClickHandler {


    private RecipeAdapter mRecipeAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;
    private static final int ID_RECIPE_LOADER = 44;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecipesRecyclerView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this);

        mRecyclerView.setAdapter(mRecipeAdapter);

        showLoading();

        initLoader();
    }


    public void initLoader(){
        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }

    @Override
    public void onClick(String Recipe) {

    }

    private void showLoading() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

//          COMPLETED (22) If the loader requested is our forecast loader, return the appropriate CursorLoader
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
        mRecipeAdapter.swapCursor(data);

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
        mRecipeAdapter.swapCursor(null);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
