package com.project.android.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.android.bakingapp.data.RecipeContract;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeAdapterViewHolder> {

    private static final String TAG = RecipesAdapter.class.getName();
    private static int itemLayout;
    private final Context mContext;
    final private RecipeAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public RecipesAdapter(Context mContext, RecipeAdapterOnClickHandler clickHandler, int itemLayout) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
        this.itemLayout = itemLayout;
    }

    @Override
    public RecipesAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(itemLayout, parent, false);

        view.setFocusable(true);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        switch (itemLayout) {
            case R.layout.recipe_card:
                int recipeNameID = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
                int recipeServingsID = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS);

                final String recipeName = mCursor.getString(recipeNameID);
                final String recipeServings = String.valueOf(mCursor.getInt(recipeServingsID));

                holder.recipeNameTxtV.setText(recipeName);
                holder.recipeServingTxtV.setText(recipeServings);
                break;
            case R.layout.step_list_content:
                int stepDescID = mCursor.getColumnIndex(RecipeContract.RecipeSteps.COLUMN_SHORT_DESC);
                final String stepDesc = mCursor.getString(stepDescID);
                holder.shortDesc.setText(stepDesc);
                break;
        }


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(int recipeID, String recipeName);
        void onClick (int stepID, int recipeID);
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeNameTxtV ;
        TextView recipeServingTxtV;
        TextView shortDesc;

        RecipeAdapterViewHolder(View view) {
            super(view);
            switch (itemLayout) {
                case R.layout.recipe_card:
                    recipeNameTxtV =  view.findViewById(R.id.recipeName);
                    recipeServingTxtV = view.findViewById(R.id.servingsTxtV);
                    break;
                case R.layout.step_list_content:
                    shortDesc = itemView.findViewById(R.id.recipe_short_desc);
                    break;
            }


            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            switch (itemLayout) {
                case R.layout.recipe_card:
                    int recipeNameID = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
                    int recipeId = mCursor.getInt(recipeNameID);

                    int recipeName = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
                    String name = mCursor.getString(recipeName);
                    mClickHandler.onClick(recipeId, name);
                    Log.i(TAG, "" + recipeId);
                    break;

                case R.layout.step_list_content:

                    int recipeID = mCursor.getColumnIndex(RecipeContract.RecipeSteps.COLUMN_RECIPE_ID);
                    int reID = mCursor.getInt(recipeID);

                    int stepID = mCursor.getColumnIndex(RecipeContract.RecipeSteps.COLUMN_STEP_ID);
                    final int step = mCursor.getInt(stepID);
                    mClickHandler.onClick(step, reID);
                    break;

            }


        }
    }
}
