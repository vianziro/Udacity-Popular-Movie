package io.github.ec2ainun.udacitypopmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {
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

    protected MovieDetails(Parcel in) {
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
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
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(vote_average);
        parcel.writeString(release_date);
    }
}