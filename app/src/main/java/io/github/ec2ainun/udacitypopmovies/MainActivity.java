package io.github.ec2ainun.udacitypopmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.github.ec2ainun.udacitypopmovies.data.MovieContract;
import io.github.ec2ainun.udacitypopmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    String error = "error";
    private ArrayList<MovieDetails> movieList;
    // Member variables for the adapter and RecyclerView
    private CustomCursorAdapter mAdapter;
    @BindView(R.id.Movie_grid) GridView gridView;
    @BindView(R.id.Movie_recycle) RecyclerView recyclerView;
    ProgressDialog pDialog;
    private Activity context;
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    private String saved;
    private static final String TAGthis = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        context =this;
        mAdapter = new CustomCursorAdapter(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
                String PreviousLifecycleCallbacks = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
                getDataMovie(PreviousLifecycleCallbacks);
                this.setSaved(PreviousLifecycleCallbacks);
            }
        }else{
            getDataMovie("popular");
            this.setSaved("popular");
        }
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);


    }
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    private void fetchImages(String endpoint) {
        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(error, response.toString());
                        try {
                            showJsonDataView(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MainActivity.this.error, "Error: " + error.getMessage());
                        // hide the progress dialog
                        pDialog.hide();
                    }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDataMovie(String data) {
        String myApiKey ="";
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            myApiKey = bundle.getString("MY_API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(error, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        }
        URL Url = NetworkUtils.buildUrl(data, myApiKey);

        fetchImages(Url.toString());
    }

    private void showJsonDataView(JSONObject SearchResults) throws JSONException {
        //JSONObject data = new JSONObject(SearchResults);
        JSONArray result = SearchResults.getJSONArray("results");
        movieList = new ArrayList<MovieDetails>();
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            MovieDetails movie = new MovieDetails(hasil.getString("id"), hasil.getString("title"), hasil.getString("overview"), hasil.getString("poster_path"), hasil.getString("vote_average"), hasil.getString("release_date"));
            movieList.add(movie);
        }

        final MovieDAdapter movieDetailsAdapter = new MovieDAdapter(this, movieList);
        gridView.setAdapter(movieDetailsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDetails movie = movieDetailsAdapter.getItem(i);
                Bundle data = new Bundle();
                data.putParcelable("Movie", movie);
                Intent intent = new Intent(MainActivity.this, InfoMovie.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            recyclerView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            getDataMovie("popular");
            this.setSaved("popular");
            return true;
        }
        if (id == R.id.action_rated) {
            recyclerView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            getDataMovie("top_rated");
            this.setSaved("top_rated");
            return true;
        }
        if(id == R.id.action_fav){
            gridView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getDataFav();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFav() {
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //MovieReview review = movieReviews.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleTextContents = this.getSaved();
        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, lifecycleTextContents);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAGthis, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
