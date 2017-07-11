package io.github.ec2ainun.udacitypopmovies;

/**
 * Created by poornima-udacity on 6/26/15.
 */
public class MovieDetails {
    String versionName;
    String versionNumber;
    int image; // drawable reference id

    public MovieDetails(String vName, String vNumber, int image)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
    }

}