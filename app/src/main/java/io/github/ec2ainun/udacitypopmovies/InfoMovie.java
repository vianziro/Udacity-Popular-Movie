package io.github.ec2ainun.udacitypopmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ec2ainun.udacitypopmovies.data.MovieContract;
import io.github.ec2ainun.udacitypopmovies.utilities.NetworkUtils;

public class InfoMovie extends AppCompatActivity {

    @BindView(R.id.title) TextView TVtitle;
    @BindView(R.id.overview) TextView TVoverview;
    @BindView(R.id.release) TextView TVrelease_date;
    @BindView(R.id.vote) TextView TVvote_average;
    @BindView(R.id.gambar) ImageView poster;
    @BindView(R.id.makeFav) ImageView makeFav;
    @BindView(R.id.makeUnFav) ImageView makeUnFav;
    @BindView(R.id.MyToolbar) Toolbar toolbar;
    @BindView(R.id.MyAppbar) AppBarLayout appBarLayout;
    @BindView(R.id.bgheader) ImageView bgHeader;
    @BindView(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.recycler_view_trailer) RecyclerView recyclerViewTrailer;
    @BindView(R.id.recycler_view_review) RecyclerView recyclerViewReview;
    @BindView(R.id.scrollView) NestedScrollView scrollView;

    public static int scrollX = 0;
    public static int scrollY = -1;
    private static final String SAVED_LAYOUT_MANAGER = "scroll";
    String TAG = "error";
    MovieDetails movie;
    ArrayList<MovieReview> movieReviews;
    ArrayList<MovieTrailer> movieTrailers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorWhite));
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (extras != null) {
            if (extras.containsKey("Movie")) {
                movie = getIntent().getExtras().getParcelable("Movie");
                String BaseURL = "http://image.tmdb.org/t/p/w500/";
                String BaseURL2 = "http://image.tmdb.org/t/p/w185/";
                String images = BaseURL.concat(movie.posterPath);
                String images2 = BaseURL2.concat(movie.posterPath);
                String myApiKey ="";
                try {
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = ai.metaData;
                    myApiKey = bundle.getString("MY_API_KEY");
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                }
                URL video = NetworkUtils.buildUrlId("videos", myApiKey, movie.movieID);
                URL reviews = NetworkUtils.buildUrlId("reviews", myApiKey, movie.movieID);
                fetchVideo(video.toString());
                fetchReview(reviews.toString());
                //Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(poster);
                //Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(bgHeader);
                Glide.with(this).load(images2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(poster);
                Glide.with(this).load(images)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bgHeader);
                TVtitle.setText(movie.title);
                TVvote_average.setText(movie.voteAverage+"/10");
                TVrelease_date.setText(movie.releaseDate);
                TVoverview.setText(movie.overview);
                collapsingToolbar.setTitle(movie.title);

                if(movie.isFav.equals("1")){

                    makeFav.setVisibility(View.VISIBLE);
                    makeUnFav.setVisibility(View.GONE);
                }else{
                    makeFav.setVisibility(View.GONE);
                    makeUnFav.setVisibility(View.VISIBLE);
                }

                Cursor datadb = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        MovieContract.MovieEntry.COLUMN_MOVIEID + " = ?",
                        new String[]{movie.movieID},
                        null);

                if (datadb.moveToFirst()){
                    //cursor is not empty
                    makeFav.setVisibility(View.VISIBLE);
                    makeUnFav.setVisibility(View.GONE);
                }

                makeFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Insert new task data via a ContentResolver
                        // Create new empty ContentValues object
                        ContentValues contentValues = new ContentValues();
                        // Put the task description and selected mPriority into the ContentValues
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEID, movie.movieID);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEOVERVIEW, movie.overview);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIETITLE, movie.title);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEPOSTERPATH, movie.posterPath);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEVOTEAVERAGE, movie.voteAverage);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIERELEASEDATE, movie.releaseDate);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEFAV, "1");

                        // Insert the content values via a ContentResolver
                        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                        makeFav.setVisibility(View.GONE);
                        makeUnFav.setVisibility(View.VISIBLE);
                    }
                });
                makeUnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                MovieContract.MovieEntry.COLUMN_MOVIEID + " = ?",
                                new String[]{movie.movieID});
                        makeFav.setVisibility(View.VISIBLE);
                        makeUnFav.setVisibility(View.GONE);
                    }
                });
            }
        }else{
            TVtitle.setText("null");
            TVvote_average.setText("null");
            TVrelease_date.setText("nul");
            TVoverview.setText("null");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SAVED_LAYOUT_MANAGER,
                new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray(SAVED_LAYOUT_MANAGER);
        if(position != null){
            this.scrollX = position[0];
            this.scrollY = position[1];
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(scrollX, scrollY);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchVideo(String endpoint) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            showJsonDataToRecycleViewTrailer(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                // hide the progress dialog=
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    private void fetchReview(String endpoint) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            showJsonDataToRecycleViewReview(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                // hide the progress dialog=
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showJsonDataToRecycleViewTrailer(JSONObject SearchResults) throws JSONException {
        //JSONObject data = new JSONObject(SearchResults);
        JSONArray result = SearchResults.getJSONArray("results");
        movieTrailers = new ArrayList<MovieTrailer>();
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            MovieTrailer movieTrailer = new MovieTrailer(hasil.getString("id"), hasil.getString("key"), hasil.getString("name"), hasil.getString("site"));
            movieTrailers.add(movieTrailer);
        }

        final MovieTAdapter movieTAdapter = new MovieTAdapter(this, movieTrailers);

        recyclerViewTrailer.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTrailer.setLayoutManager(mLayoutManager);
        recyclerViewTrailer.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewTrailer.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTrailer.setAdapter(movieTAdapter);

        recyclerViewTrailer.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewTrailer, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieTrailer trailer = movieTrailers.get(position);
                watchYoutubeVideo(trailer.trailerKey);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void showJsonDataToRecycleViewReview(JSONObject SearchResults) throws JSONException {
        //JSONObject data = new JSONObject(SearchResults);
        JSONArray result = SearchResults.getJSONArray("results");
        movieReviews = new ArrayList<MovieReview>();
        for (int i = 0; i < result.length(); ++i) {
            JSONObject hasil = result.getJSONObject(i);
            MovieReview movieReview = new MovieReview(hasil.getString("id"), hasil.getString("author"), hasil.getString("content"), hasil.getString("url"));
            movieReviews.add(movieReview);
        }

        final MovieRAdapter movieRAdapter = new MovieRAdapter(this,movieReviews);

        recyclerViewReview.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewReview.setLayoutManager(mLayoutManager);
        recyclerViewReview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReview.setAdapter(movieRAdapter);

        recyclerViewReview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewReview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieReview review = movieReviews.get(position);
                viewReview(review.reviewUrl);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void viewReview(String url){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
    }
}
