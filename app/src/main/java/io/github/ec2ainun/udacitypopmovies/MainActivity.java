package io.github.ec2ainun.udacitypopmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
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
import android.widget.Toast;

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
        LoaderManager.LoaderCallbacks<Cursor>,CustomCursorAdapter.ListItemClickListener, MovieListAdapter.ListItemClickListener {

    private ArrayList<MovieDetails> movieList;
    private CustomCursorAdapter mAdapter;
    @BindView(R.id.Movie_recycle) RecyclerView recyclerView;
    ProgressDialog pDialog;
    private Activity context;
    private String saved;
    //static
    private static final String TAGthis = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    private static final String SAVED_LAYOUT_MANAGER = "scroll";
    private static final int MOVIE_LOADER_ID = 0;
    private static final String error = "error";

    Parcelable mListState;
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
        context = this;
        mAdapter = new CustomCursorAdapter(context,this);
        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
                String PreviousLifecycleCallbacks = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
                if(PreviousLifecycleCallbacks == "fav"){
                    getDataFav();
                }else{
                    getDataMovie(PreviousLifecycleCallbacks);
                }
                this.setSaved(PreviousLifecycleCallbacks);
            }

        }else{
            getDataMovie("popular");
            this.setSaved("popular");
        }
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleTextContents = this.getSaved();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, lifecycleTextContents);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // re-queries for all tasks
        if (getSupportLoaderManager() != null) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
        if (mListState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
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
        JSONArray result = SearchResults.getJSONArray("results");
        movieList = new ArrayList<MovieDetails>();
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            MovieDetails movie = new MovieDetails(hasil.getString("id"), hasil.getString("title"), hasil.getString("overview"), hasil.getString("poster_path"), hasil.getString("vote_average"), hasil.getString("release_date"));
            movieList.add(movie);
        }
        final MovieListAdapter movieDetailsAdapter = new MovieListAdapter(context, movieList,this);
        recyclerView.setAdapter(movieDetailsAdapter);
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
            getDataMovie("popular");
            this.setSaved("popular");
            return true;
        }
        if (id == R.id.action_rated) {
            getDataMovie("top_rated");
            this.setSaved("top_rated");
            return true;
        }
        if(id == R.id.action_fav){
            getDataFav();
            this.setSaved("fav");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFav() {
        recyclerView.setAdapter(mAdapter);

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


    @Override
    public void onListDBItemClick(View view, int clickedItemIndex) {
        int id = (int) view.getTag();
        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor datadb =getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        MovieDetails movie = new MovieDetails(datadb.getString(1),
                datadb.getString(2),
                datadb.getString(3),
                datadb.getString(4),
                datadb.getString(5),
                datadb.getString(6));

        Bundle data = new Bundle();
        data.putParcelable("Movie", movie);
        Intent intent = new Intent(MainActivity.this, InfoMovie.class);
        intent.putExtras(data);
        startActivity(intent);

    }

    @Override
    public void onListReqItemClick(View view, int clickedItemIndex) {
        MovieDetails movie = movieList.get(clickedItemIndex);
        Bundle data = new Bundle();
        data.putParcelable("Movie", movie);
        Intent intent = new Intent(MainActivity.this, InfoMovie.class);
        intent.putExtras(data);
        startActivity(intent);
    }
}
