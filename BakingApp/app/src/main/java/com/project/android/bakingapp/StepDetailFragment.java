package com.project.android.bakingapp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.android.bakingapp.data.RecipeContract;

public class stepDetailFragment extends Fragment {

    private static final String TAG = stepDetailFragment.class.getName();
    public static final String ARG_ITEM_ID = "item_id";
    private Cursor mCursor;
    private TextView stepDescTxtView;

    public static final String[] STEP_DETAIL_PROJECTION = {
            RecipeContract.RecipeSteps.COLUMN_STEP_ID,
            RecipeContract.RecipeSteps.COLUMN_RECIPE_ID,
            RecipeContract.RecipeSteps.COLUMN_DESC,
            RecipeContract.RecipeSteps.COLUMN_SHORT_DESC,
            RecipeContract.RecipeSteps.COLUMN_VIDEO_URL,
            RecipeContract.RecipeSteps.COLUMN_THUMBNAIL_URL
    };

    public static final int INDEX_STEP_ID = 0;
    public static final int INDEX_RECIPE_ID = 1;
    public static final int INDEX_STEP_DESC = 2;
    public static final int INDEX_STEP_SHORT_DESC = 3;
    public static final int INDEX_STEP_VIDEO_URL = 4;
    public static final int INDEX_STEP_THUMBNAIL_URL = 5;


    public stepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            queryData();


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =  activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null && mCursor!= null) {
                mCursor.moveToFirst();
                appBarLayout.setTitle(mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_SHORT_DESC])));
            }
        }
    }

    private void queryData(){
        try {
            String recipeID = getArguments().getString(STEP_DETAIL_PROJECTION[INDEX_RECIPE_ID]);
            String stepID = getArguments().getString(ARG_ITEM_ID);
            Uri uri = RecipeContract.RecipeSteps.CONTENT_URI.buildUpon().appendPath(recipeID).appendPath(stepID).build();

            mCursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        stepDescTxtView = rootView.findViewById(R.id.step_desc_txtView);
        if(mCursor!= null){
//            mCursor.moveToFirst();
            String txt = mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_DESC]));
            stepDescTxtView.setText(txt);
        }

        return rootView;
    }
}
