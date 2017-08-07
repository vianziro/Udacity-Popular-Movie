package io.github.ec2ainun.udacitypopmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {
    String movieID;
    String title;
    String overview;
    String posterPath;
    String voteAverage;
    String releaseDate;

    public MovieDetails(String movieID, String title, String overview, String posterPath, String voteAverage, String releaseDate){
        this.movieID = movieID;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    protected MovieDetails(Parcel in) {
        movieID = in.readString();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieID);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
    }
}