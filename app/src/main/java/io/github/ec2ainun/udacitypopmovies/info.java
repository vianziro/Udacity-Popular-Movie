package io.github.ec2ainun.udacitypopmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class info extends AppCompatActivity {

    String title, overview, poster_path, release_date, vote_average;
    TextView TVtitle, TVoverview, TVposter_path, TVrelease_date, TVvote_average;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TVtitle = (TextView) findViewById(R.id.title);
        TVoverview = (TextView) findViewById(R.id.overview);
        TVrelease_date = (TextView) findViewById(R.id.release);
        TVvote_average = (TextView) findViewById(R.id.vote);
        poster = (ImageView) findViewById(R.id.gambar);


        title = getIntent().getExtras().getString("title");
        overview = getIntent().getExtras().getString("overview");
        poster_path = getIntent().getExtras().getString("poster_path");
        release_date = getIntent().getExtras().getString("release_date");
        vote_average = getIntent().getExtras().getString("vote_average");
        String BaseURL = "http://image.tmdb.org/t/p/w500/";
        String images  =BaseURL.concat(poster_path);

        Picasso.with(this).load(images).into(poster);
        TVtitle.setText(title);
        TVvote_average.setText("("+vote_average+")");
        TVrelease_date.setText(release_date);
        TVoverview.setText(overview);

    }
}
