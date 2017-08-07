package io.github.ec2ainun.udacitypopmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ec2ainun on 8/8/2017.
 */

public class MovieTrailer implements Parcelable {
    String trailerID;
    String trailerKey;
    String trailerName;
    String trailerSite;

    public MovieTrailer(String trailerID, String trailerKey, String trailerName, String trailerSite){
        this.trailerID = trailerID;
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
        this.trailerSite = trailerSite;

    }
    protected MovieTrailer(Parcel in) {
        trailerID = in.readString();
        trailerKey = in.readString();
        trailerName = in.readString();
        trailerSite = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(trailerID);
        parcel.writeString(trailerKey);
        parcel.writeString(trailerName);
        parcel.writeString(trailerSite);

    }
}
