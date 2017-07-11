package io.github.ec2ainun.udacitypopmovies;

/**
 * Created by poornima-udacity on 6/26/15.
 */
public class MovieDetails {
    String title;
    String overview;
    String poster_path;
    String vote_average;
    String release_date;

    public MovieDetails(String title, String overview, String poster_path, String vote_average, String release_date)
    {
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.release_date = release_date;

    }

}