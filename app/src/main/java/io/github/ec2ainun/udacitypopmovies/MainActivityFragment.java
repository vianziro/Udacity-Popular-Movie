package io.github.ec2ainun.udacitypopmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * A fragment containing the list view of Android versions.
 */
public class MainActivityFragment extends Fragment {

    private MovieDetailsAdapter movieDetailsAdapter;

    MovieDetails[] movieDetailses = {
            new MovieDetails("beatuy", "good", "\\/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "5.6", "tes"),
            new MovieDetails("inter", "badd", "\\/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "9.0", "tes" ),
            new MovieDetails("Eblaclair", "yoo", "\\/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "8.8", "tes" ),
            new MovieDetails("tes", "yeah", "\\/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "9.0", "tes")
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieDetailsAdapter = new MovieDetailsAdapter(getActivity(), Arrays.asList(movieDetailses));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.Movie_grid);
        gridView.setAdapter(movieDetailsAdapter);

        return rootView;
    }
}