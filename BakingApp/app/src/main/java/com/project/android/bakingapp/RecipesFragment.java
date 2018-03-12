package com.project.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.project.android.bakingapp.data.RecipeContract;
import com.project.android.bakingapp.sync.RecipeSyncUtils;


public class RecipesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RecipesAdapter.RecipeAdapterOnClickHandler{


    private RecipesAdapter mRecipesAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private static boolean isWidgetActivity = false;
    private ProgressBar mLoadingIndicator;
    private static final int ID_RECIPE_LOADER = 44;
    boolean orientationLand;
    RecyclerView.LayoutManager manager;

    static OnRecipeSelectedListener mCallback;


    public interface OnRecipeSelectedListener {
         void onRecipeSelected(int recipeID, String RecipeName);
    }

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance(boolean isWidget, OnRecipeSelectedListener listener) {
        RecipesFragment fragment = new RecipesFragment();
        mCallback = listener;
        isWidgetActivity = isWidget;
        return fragment;
    }

    public static RecipesFragment newInstance(boolean isWidget) {
        RecipesFragment fragment = new RecipesFragment();
        isWidgetActivity = isWidget;
        return fragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationLand = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE )? true : false;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);


        mRecyclerView = rootView.findViewById(R.id.RecipesRecyclerView);
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

        if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE )
            manager = new GridLayoutManager(getContext(),3);
        else
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setHasFixedSize(false);

        mRecipesAdapter = new RecipesAdapter(getContext(), this, R.layout.recipe_card);

        mRecyclerView.setAdapter(mRecipesAdapter);


        showLoading();

        initLoader();

        RecipeSyncUtils.initialize(getContext());
        return rootView;

    }


    private void showLoading() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    public void initLoader(){
        getActivity().getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case ID_RECIPE_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri recipeUrl = RecipeContract.RecipeEntry.CONTENT_URI;

                return new CursorLoader(getContext(),
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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipesAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int recipeID, String recipeName) {
        if(!isWidgetActivity)
            startRecipeDetailActivity(recipeID, recipeName);
        else
            mCallback.onRecipeSelected(recipeID, recipeName);


    }



    private  void startRecipeDetailActivity( int recipeID, String recipeName){

        Intent recipeDetailIntent = new Intent(getActivity(), StepListActivity.class);

        recipeDetailIntent.putExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipeName);
        recipeDetailIntent.putExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,Integer.toString(recipeID));
        startActivity(recipeDetailIntent);
    }

    @Override
    public void onClick(int stepID, int recipeID, int totalSteps) {

    }

    private void showRecipeView() {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
