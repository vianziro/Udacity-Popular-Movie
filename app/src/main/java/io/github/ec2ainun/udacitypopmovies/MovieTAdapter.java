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

public class MovieTAdapter extends RecyclerView.Adapter<MovieTAdapter.MyViewHolder> {
    private List<MovieTrailer> trailerList;
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trailerName;

        public MyViewHolder(View view) {
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailerName);
        }
    }


    public MovieTAdapter(Activity context, List<MovieTrailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item,null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieTrailer trailer = trailerList.get(position);
        holder.trailerName.setText(trailer.trailerName);
    }

    @Override
    public int getItemCount() {
        if (trailerList != null) {
            return trailerList.size();
        } else {
            return 0;
        }
    }

}
