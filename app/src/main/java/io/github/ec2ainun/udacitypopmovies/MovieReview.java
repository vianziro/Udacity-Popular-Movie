package io.github.ec2ainun.udacitypopmovies;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ec2ainun on 8/8/2017.
 */

public class MovieReview implements Parcelable {
    String reviewID;
    String reviewAuthor;
    String reviewContent;
    String reviewUrl;

    public MovieReview(String reviewID, String reviewAuthor, String reviewContent, String reviewUrl){
        this.reviewID = reviewID;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
    }
    protected MovieReview(Parcel in) {
        reviewID = in.readString();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewUrl = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(reviewID);
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
        parcel.writeString(reviewUrl);

    }
}
