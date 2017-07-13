package io.github.ec2ainun.udacitypopmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class info extends AppCompatActivity {

    TextView TVtitle, TVoverview, TVrelease_date, TVvote_average;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TVtitle = (TextView) findViewById(R.id.title);
        TVoverview = (TextView) findViewById(R.id.overview);
        TVrelease_date = (TextView) findViewById(R.id.release);
        TVvote_average = (TextView) findViewById(R.id.vote);
        poster = (ImageView) findViewById(R.id.gambar);

        MovieDetails movie = getIntent().getExtras().getParcelable("Movie");
        String BaseURL = "http://image.tmdb.org/t/p/w500/";
        String images  =BaseURL.concat(movie.poster_path);

        Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(poster);
        TVtitle.setText(movie.title);
        TVvote_average.setText("("+movie.vote_average+")");
        TVrelease_date.setText(movie.release_date);
        TVoverview.setText(movie.overview);
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
}
