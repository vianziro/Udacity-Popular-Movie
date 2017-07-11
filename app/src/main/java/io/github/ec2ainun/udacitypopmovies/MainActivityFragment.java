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

    /*MovieDetails[] movieDetailses = {
            new MovieDetails("Cupcake", "1.5", R.drawable.cupcake),
            new MovieDetails("Donut", "1.6", R.drawable.donut),
            new MovieDetails("Eclair", "2.0-2.1", R.drawable.eclair),
            new MovieDetails("Froyo", "2.2-2.2.3", R.drawable.froyo)
    };*/

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //movieDetailsAdapter = new MovieDetailsAdapter(getActivity(), Arrays.asList(movieDetailses));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.Movie_grid);
        gridView.setAdapter(movieDetailsAdapter);

        return rootView;
    }
}