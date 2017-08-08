/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package io.github.ec2ainun.udacitypopmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ec2ainun.udacitypopmovies.data.MovieContract;


/**
 * This CustomCursorAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.MovieViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new MovieViewHolder that holds the view for each task
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int movieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID);
        int movieTitleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIETITLE);
        int movieOverviewIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEOVERVIEW);
        int moviePosterPathIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEPOSTERPATH);
        int movieVoteAverageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEVOTEAVERAGE);
        int movieReleaseIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIERELEASEDATE);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String posterPath = mCursor.getString(moviePosterPathIndex);

        //Set values
        holder.itemView.setTag(id);

        String BaseURL = "http://image.tmdb.org/t/p/w185/";
        String gambar  =BaseURL.concat(posterPath);
        Glide.with(mContext).load(gambar)
                .thumbnail(0.25f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class MovieViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        @BindView(R.id.Movie_image) ImageView image;
        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}