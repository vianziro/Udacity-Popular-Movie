package io.github.ec2ainun.udacitypopmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ec2ainun.udacitypopmovies.utilities.AsynMovieQueryTask;
import io.github.ec2ainun.udacitypopmovies.utilities.AsyncTaskCompleteListener;
import io.github.ec2ainun.udacitypopmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    String TAG = "error";
    private ArrayList<MovieDetails> movieList;
    @BindView(R.id.Movie_grid) GridView gridView;
    ProgressDialog pDialog;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        context =this;
        getDataMovie("popular");

    }

    private void fetchImages(String endpoint) {
        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
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
                        Log.e(TAG, "Error: " + error.getMessage());
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
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        }
        URL Url = NetworkUtils.buildUrl(data, myApiKey);
        /*new AsynMovieQueryTask(this, new AsyncTaskCompleteListener<String>() {
            @Override
            public void onTaskComplete(String result) throws JSONException {
                showJsonDataView(result);
            }
        }).execute(Url);*/
        fetchImages(Url.toString());
    }

    private void showJsonDataView(JSONObject SearchResults) throws JSONException {
        //JSONObject data = new JSONObject(SearchResults);
        JSONArray result = SearchResults.getJSONArray("results");
        movieList = new ArrayList<MovieDetails>();
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            MovieDetails movie = new MovieDetails(hasil.getString("title"), hasil.getString("overview"), hasil.getString("poster_path"), hasil.getString("vote_average"), hasil.getString("release_date"));
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
            getDataMovie("popular");
            return true;
        }
        if (id == R.id.action_rated) {
            getDataMovie("top_rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
