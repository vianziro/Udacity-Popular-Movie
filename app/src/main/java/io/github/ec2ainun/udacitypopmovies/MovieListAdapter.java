package io.github.ec2ainun.udacitypopmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ec2ainun on 8/13/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.NumberViewHolder>{

    private static final String TAG = MovieListAdapter.class.getSimpleName();
    private static int viewHolderCount;

    private final List<MovieDetails> movieList;
    private Context mContext;
    private int mNumberItems;
    final private ListItemClickListener mOnClickListener;
    public interface ListItemClickListener {
        void onListReqItemClick(View view , int clickedItemIndex);
    }
    public MovieListAdapter(Context mContext, List<MovieDetails> Movie, ListItemClickListener listener) {
        this.mContext = mContext;
        this.movieList = Movie;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        MovieDetails movie = movieList.get(position);
        String BaseURL = "http://image.tmdb.org/t/p/w185/";
        String gambar  =BaseURL.concat(movie.posterPath);
        Glide.with(mContext).load(gambar)
                .thumbnail(0.25f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.Movie_image) ImageView image;
        public NumberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListReqItemClick(v,clickedPosition);
        }
    }
}
