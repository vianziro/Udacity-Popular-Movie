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

import butterknife.BindView;
import butterknife.ButterKnife;

public class info extends AppCompatActivity {

    @BindView(R.id.title) TextView TVtitle;
    @BindView(R.id.overview) TextView TVoverview;
    @BindView(R.id.release) TextView TVrelease_date;
    @BindView(R.id.vote) TextView TVvote_average;
    @BindView(R.id.gambar) ImageView poster;

    MovieDetails movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("Movie")) {
                movie = getIntent().getExtras().getParcelable("Movie");
                String BaseURL = "http://image.tmdb.org/t/p/w500/";
                String images = BaseURL.concat(movie.posterPath);
                Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(poster);
                TVtitle.setText(movie.title);
                TVvote_average.setText("("+movie.voteAverage+")");
                TVrelease_date.setText(movie.releaseDate);
                TVoverview.setText(movie.overview);
            }
        }else{
            TVtitle.setText("null");
            TVvote_average.setText("null");
            TVrelease_date.setText("nul");
            TVoverview.setText("null");
        }

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
