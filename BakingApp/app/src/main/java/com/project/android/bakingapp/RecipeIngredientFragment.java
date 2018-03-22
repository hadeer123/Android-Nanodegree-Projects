package com.project.android.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.android.bakingapp.data.RecipeContract;

public class RecipeIngredientFragment extends Fragment {
    public static final String[] RECIPE_DETAIL_PROJECTION = {
            RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID,
            RecipeContract.RecipeIngredients.COLUMN_QUANTITY,
            RecipeContract.RecipeIngredients.COLUMN_MEASURE,
            RecipeContract.RecipeIngredients.COLUMN_INGREDIENT,
    };
    public static final int INDEX_RECIPE_ID = 0;
    public static final int INDEX_ING = 3;
    public static final int INDEX_ING_QUANTITY = 1;
    public static final int INDEX_ING_MEASURE = 2;
    private static final String TAG = RecipeIngredientFragment.class.getName();
    private static final String ARG_RECIPE_ID = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID;
    private TextView recipeIngredientsTxtV;
    private String recipeId;

    public RecipeIngredientFragment() {
        // Required empty public constructor
    }


    public static RecipeIngredientFragment newInstance(String recipeID) {
        RecipeIngredientFragment fragment = new RecipeIngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeID);
        fragment.setArguments(args);
        return fragment;
    }

    public static StringBuilder queryIngredients(String recipeID, Context context) {
        StringBuilder ingredientDisplayString = new StringBuilder();
        try {
            Uri uri = RecipeContract.RecipeIngredients.CONTENT_URI.buildUpon().appendPath(recipeID).build();

            Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
            ingredientDisplayString = new StringBuilder();
            if (mCursor != null) {
                mCursor.moveToFirst();
                int ingCount = mCursor.getCount();
                for (int i = 0; i <= ingCount; i++) {
                    String ing = mCursor.getString(mCursor.getColumnIndex(RECIPE_DETAIL_PROJECTION[INDEX_ING]));
                    String Q = mCursor.getString(mCursor.getColumnIndex(RECIPE_DETAIL_PROJECTION[INDEX_ING_QUANTITY]));
                    String measure = mCursor.getString(mCursor.getColumnIndex(RECIPE_DETAIL_PROJECTION[INDEX_ING_MEASURE]));
                    ingredientDisplayString.append(ing.toUpperCase().charAt(0) + ing.substring(1).toLowerCase() + "( " + Q + " " + measure + " )" + System.getProperty("line.separator"));
                    mCursor.moveToPosition(i);

                }

            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return ingredientDisplayString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getString(ARG_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);
        recipeIngredientsTxtV = rootView.findViewById(R.id.recipeIngredientsTxtView);
        recipeIngredientsTxtV.setText(queryIngredients(recipeId, getActivity()));

        return rootView;
    }


}
