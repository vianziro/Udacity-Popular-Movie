package io.github.ec2ainun.udacitypopmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDAdapter extends BaseAdapter {
    private final List<MovieDetails> Movie;
    private Activity context;

    public MovieDAdapter(Activity context, List<MovieDetails> Movie) {
        this.context = context;
        this.Movie = Movie;
    }

    @Override
    public int getCount() {
        if (Movie != null) {
            return Movie.size();
        } else {
            return 0;
        }
    }

    @Override
    public MovieDetails getItem(int i) {
        if (Movie != null) {
            return Movie.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void add(MovieDetails message) {
        Movie.add(message);
    }

    public void add(List<MovieDetails> messages) {
        Movie.addAll(messages);
    }


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDetails movieDetails = this.getItem(position);

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = vi.inflate(R.layout.movie_item, null);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.Movie_image);
        TextView title = (TextView) convertView.findViewById(R.id.Movie_title);

        String BaseURL = "http://image.tmdb.org/t/p/w185/";
        String gambar  =BaseURL.concat(movieDetails.poster_path);
        title.setText(movieDetails.title);

        Picasso.with(context).load(gambar).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(iconView);

        return convertView;
    }
}
