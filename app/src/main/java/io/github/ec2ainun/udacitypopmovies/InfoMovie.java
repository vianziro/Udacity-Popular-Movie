package io.github.ec2ainun.udacitypopmovies;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoMovie extends AppCompatActivity {

    @BindView(R.id.title) TextView TVtitle;
    @BindView(R.id.overview) TextView TVoverview;
    @BindView(R.id.release) TextView TVrelease_date;
    @BindView(R.id.vote) TextView TVvote_average;
    @BindView(R.id.gambar) ImageView poster;

    @BindView(R.id.MyToolbar) Toolbar toolbar;
    @BindView(R.id.MyAppbar) AppBarLayout appBarLayout;
    @BindView(R.id.bgheader) ImageView bgHeader;
    @BindView(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;

    MovieDetails movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorWhite));
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle("Movie Details");

        if (extras != null) {
            if (extras.containsKey("Movie")) {
                movie = getIntent().getExtras().getParcelable("Movie");
                String BaseURL = "http://image.tmdb.org/t/p/w500/";
                String images = BaseURL.concat(movie.posterPath);
                Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(poster);
                Picasso.with(this).load(images).placeholder(R.drawable.placeholder).error(R.drawable.errorimg).into(bgHeader);
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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    // Collapsed
                    poster.setVisibility(View.GONE);
                    TVoverview.setVisibility(View.VISIBLE);

                }
                else {
                    // Not collapsed
                    poster.setVisibility(View.VISIBLE);
                    TVoverview.setVisibility(View.GONE);

                }
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
}
