package com.project.android.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.project.android.bakingapp.data.RecipeContract;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements RecipesAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = StepListActivity.class.getName();
    private static final int ID_STEPS_LAODER = 55;
    private final String RECIPE_ID = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID;
    private final String RECIPE_NAME = RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME;
    private boolean mTwoPane;
    private Uri recipeIDUri;
    private RecipesAdapter mRecipeListAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        recipeID = getIntent().getStringExtra(RECIPE_ID);
        recipeIDUri = RecipeContract.RecipeSteps.CONTENT_URI.buildUpon().appendPath(recipeID).build();


        setTitle(getIntent().getStringExtra(RECIPE_NAME));
        createIngredientFragment(recipeID);


        mRecyclerView = findViewById(R.id.step_list);


        assert mRecyclerView != null;

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        setupRecyclerView(mRecyclerView);


        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        initLoader();
    }

    private void createIngredientFragment(String recipeID) {
        if (findViewById(R.id.recipe_ingredients_frame) != null)
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_ingredients_frame, RecipeIngredientFragment.newInstance(recipeID)).commit();
    }


    public void initLoader() {
        getSupportLoaderManager().initLoader(ID_STEPS_LAODER, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecipeListAdapter = new RecipesAdapter(this, this, R.layout.step_list_content);
        recyclerView.setAdapter(mRecipeListAdapter);

    }


    @Override
    public void onClick(int recipeID, String recipeName) {

    }

    @Override
    public void onClick(int stepID, int recipeID, int totalSteps) {
        Intent stepDetailIntent = new Intent(StepListActivity.this, StepDetailActivity.class);

        stepDetailIntent.putExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, getIntent().getStringExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
        stepDetailIntent.putExtra(StepDetailFragment.ARG_ITEM_ID, String.valueOf(stepID));
        stepDetailIntent.putExtra(StepDetailFragment.STEP_DETAIL_PROJECTION[StepDetailFragment.INDEX_RECIPE_ID], String.valueOf(recipeID));
        stepDetailIntent.putExtra(StepDetailActivity.TOTAL_STEPS_FOR_RECIPE, totalSteps);

        startActivity(stepDetailIntent);
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_STEPS_LAODER:
                return new CursorLoader(this,
                        recipeIDUri,
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
        if (data != null) {
            mRecipeListAdapter.swapCursor(data);
        }
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mRecyclerView.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mRecipeListAdapter.swapCursor(null);

    }


}
