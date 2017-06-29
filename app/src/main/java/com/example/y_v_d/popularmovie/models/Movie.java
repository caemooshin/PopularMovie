package com.example.y_v_d.popularmovie.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



public class Movie implements Parcelable{
    @SerializedName("id")
    private long mId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPoster;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("vote_average")
    private String mUserRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("backdrop_path")
    private String mBackdrop;

    public Movie(long mId, String mTitle, String mPoster, String mOverview, String mUserRating, String mReleaseDate, String mBackdrop) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mBackdrop = mBackdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPoster);
        dest.writeString(this.mOverview);
        dest.writeString(this.mUserRating);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mBackdrop);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.mId = in.readLong();
        this.mTitle = in.readString();
        this.mPoster = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readString();
        this.mReleaseDate = in.readString();
        this.mBackdrop = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w500" + mPoster;
    }
}
