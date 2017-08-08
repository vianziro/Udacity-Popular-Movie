package io.github.ec2ainun.udacitypopmovies;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ec2ainun on 8/8/2017.
 */

public class MovieRAdapter extends RecyclerView.Adapter<MovieRAdapter.MyViewHolder> {
    private List<MovieReview> reviewList;
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewAuthor, reviewContent;

        public MyViewHolder(View view) {
            super(view);
            reviewAuthor = (TextView) view.findViewById(R.id.reviewAuthor);
            reviewContent = (TextView) view.findViewById(R.id.reviewContent);
        }
    }


    public MovieRAdapter(Activity context, List<MovieReview> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item,null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieReview review = reviewList.get(position);
        holder.reviewAuthor.setText("Author : "+review.reviewAuthor);
        holder.reviewContent.setText(review.reviewContent);
    }

    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        } else {
            return 0;
        }
    }

}
