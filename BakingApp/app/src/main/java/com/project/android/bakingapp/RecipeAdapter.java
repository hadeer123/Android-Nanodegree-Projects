package com.project.android.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.android.bakingapp.data.RecipeContract;

/**
 * Created by fg7cpt on 2/14/2018.
 */

public class RecipeAdapter  extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{

    private final Context mContext;
    final private RecipeAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public RecipeAdapter(Context mContext, RecipeAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
    }


    public interface RecipeAdapterOnClickHandler {
        void onClick(String Recipe);
    }
    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recipe_card, parent, false);

        view.setFocusable(true);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        int recipeNameID = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
        int recipeServingsID = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS);

        final String recipeName = mCursor.getString(recipeNameID);
        final String recipeServings = String.valueOf(mCursor.getInt(recipeServingsID));

        holder.recipeNameTxtV.setText(recipeName);
        holder.recipeServingTxtV.setText(recipeServings);

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

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeNameTxtV;
        final TextView recipeServingTxtV;

        RecipeAdapterViewHolder(View view) {
            super(view);

            recipeNameTxtV = (TextView) view.findViewById(R.id.recipeNameTxV);
            recipeServingTxtV = (TextView) view.findViewById(R.id.servingsTxtV);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }
}
