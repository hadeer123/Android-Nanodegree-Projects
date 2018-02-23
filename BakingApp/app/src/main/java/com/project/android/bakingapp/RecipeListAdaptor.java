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
 * Created by fg7cpt on 2/21/2018.
 */

public class RecipeListAdaptor extends RecyclerView.Adapter<RecipeListAdaptor.RecipeListAdapterViewHolder> {

    private final String TAG = RecipeListAdaptor.class.getName();
    private final Context mContext;


    private Cursor mCursor;


    public RecipeListAdaptor(Context mContext) {
        this.mContext = mContext;
    }



    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.step_list_content, parent, false);
        view.setFocusable(true);
        return new RecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int stepDescID = mCursor.getColumnIndex(RecipeContract.RecipeSteps.COLUMN_SHORT_DESC);
        final String stepDesc = mCursor.getString(stepDescID);
        holder.shortDesc.setText(stepDesc);

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


    class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView shortDesc;

        public RecipeListAdapterViewHolder(View itemView) {
            super(itemView);

            shortDesc = itemView.findViewById(R.id.recipe_short_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
        }
    }

}