package io.github.ec2ainun.udacitypopmovies;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.ec2ainun.udacitypopmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    String TAG = "error";
    /*private MovieDetailsAdapter movieDetailsAdapter;
    private ArrayList<MovieDetails> movieList;
    MovieDetails[] movieDetailses;*/
    private ArrayList<MovieDetails> movieList = new ArrayList<MovieDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataMovie("popular");
        setContentView(R.layout.activity_main);

        /*movieList = new  ArrayList<MovieDetails>(Arrays.asList(movieDetailses));
        movieDetailsAdapter = new MovieDetailsAdapter(this, movieList);*/
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
        new MovieQueryTask().execute(Url);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String SearchResults = null;
            try {
                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SearchResults;
        }

        @Override
        protected void onPostExecute(String SearchResults) {
            if (SearchResults != null && !SearchResults.equals("")) {
                try {
                    showJsonDataView(SearchResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    private void showJsonDataView(String SearchResults) throws JSONException {
        JSONObject data = new JSONObject(SearchResults);
        JSONArray result = data.getJSONArray("results");
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            String title = hasil.getString("title");
            String overview = hasil.getString("overview");
            String poster_path = hasil.getString("poster_path");
            String vote_average = hasil.getString("vote_average");
            String release_date = hasil.getString("release_date");
            MovieDetails movie = new MovieDetails(title, overview, poster_path, vote_average, release_date);
            movieList.add(movie);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", movieList);
        // set Fragmentclass Arguments
        MainActivityFragment send = new MainActivityFragment();
        send.setArguments(bundle);

        /*movieDetailsAdapter.notifyDataSetChanged();*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
