package io.github.ec2ainun.udacitypopmovies;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment containing the list view of Android versions.
 */
public class MainActivityFragment extends Fragment {

    private MovieDAdapter movieDetailsAdapter;

    /*MovieDetails[] movieDetailses = {
            new MovieDetails("beatuy", "good", "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "5.6", "tes"),
            new MovieDetails("inter", "badd", "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "9.0", "tes" ),
            new MovieDetails("Eblaclair", "yoo", "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "8.8", "tes" ),
            new MovieDetails("tes", "yeah", "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "9.0", "tes")
    };*/
    ArrayList<MovieDetails> movieList;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle bundle=getArguments();
        GridView gridView = (GridView) rootView.findViewById(R.id.Movie_grid);
        if(bundle!=null && savedInstanceState==null){
            movieList = bundle.getParcelableArrayList("data");
            //Log.d("tes", movieList.toString());
            /*for(MovieDetails data : movieList){
                movieDetailsAdapter.add(data);
                movieDetailsAdapter.notifyDataSetChanged();
            }*/
            movieDetailsAdapter = new MovieDAdapter(getActivity(), movieList);
            gridView.setAdapter(movieDetailsAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieDetails movie = movieDetailsAdapter.getItem(i);
                    Bundle data = new Bundle();
                    data.putString("title", movie.title);
                    data.putString("overview", movie.overview);
                    data.putString("poster_path", movie.poster_path);
                    data.putString("release_date", movie.release_date);
                    data.putString("vote_average", movie.vote_average);
                    Intent intent = new Intent(getActivity(), info.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("data", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("data")) {
            //movieList = new ArrayList<MovieDetails>(Arrays.asList(movieDetailses));
            movieList = new ArrayList<MovieDetails>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("data");
        }
    }
}