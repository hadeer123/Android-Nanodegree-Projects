package com.project.android.bakingapp;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.android.bakingapp.data.RecipeContract;


public class StepDetailActivity extends AppCompatActivity {
    public static final String TOTAL_STEPS_FOR_RECIPE = "total_steps";
    private static int totalSteps;
    private static int stepID;
    private static String recipeID;
    private ImageView previousStepImg, nextStepImg;
    private TextView stepCountTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);


        previousStepImg =  findViewById(R.id.prev_step_img);
        nextStepImg = findViewById(R.id.next_step_img);
        stepCountTxtView =  findViewById(R.id.stepsCountTxtView);


        setSupportActionBar((Toolbar)findViewById(R.id.toolbar2));


        setTitle(getIntent().getStringExtra(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            stepID = Integer.parseInt(getIntent().getStringExtra(StepDetailFragment.ARG_ITEM_ID));
            totalSteps = getIntent().getIntExtra(TOTAL_STEPS_FOR_RECIPE, -1) - 1;
            recipeID = StepDetailFragment.STEP_DETAIL_PROJECTION[StepDetailFragment.INDEX_RECIPE_ID];

            if (stepID == 0) {
                stepCountTxtView.setText(R.string.intro_txt);
                previousStepImg.setVisibility(View.INVISIBLE);
                stepCountTxtView.setVisibility(View.INVISIBLE);
            }


            createFragment(stepID);

        }

        previousStepImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stepID != 0)
                    createFragment(--stepID);
            }
        });

        nextStepImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepID < totalSteps)
                    createFragment(++stepID);
            }
        });


    }


    private void createFragment(int stepID) {

        stepCountTxtView.setText(getString(R.string.step_display, stepID, totalSteps));

        nextStepImg.setVisibility((stepID == totalSteps) ? View.INVISIBLE : View.VISIBLE);
        previousStepImg.setVisibility((stepID == 0) ? View.INVISIBLE : View.VISIBLE);
        stepCountTxtView.setVisibility((stepID == 0) ? View.INVISIBLE : View.VISIBLE);

        Bundle arguments = new Bundle();
        arguments.putString(StepDetailFragment.ARG_ITEM_ID, String.valueOf(stepID));
        arguments.putString(recipeID, getIntent().getStringExtra(recipeID));

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            navigateUpTo(new Intent(this, StepListActivity.class));
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
