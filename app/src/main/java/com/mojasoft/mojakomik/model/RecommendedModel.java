package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecommendedModel implements Parcelable {
    private String title;
    private String endpoint;

    public RecommendedModel(Parcel in) {
        title = in.readString();
        endpoint = in.readString();
        thumb = in.readString();
    }

    public static final Creator<RecommendedModel> CREATOR = new Creator<RecommendedModel>() {
        @Override
        public RecommendedModel createFromParcel(Parcel in) {
            return new RecommendedModel(in);
        }

        @Override
        public RecommendedModel[] newArray(int size) {
            return new RecommendedModel[size];
        }
    };

    public RecommendedModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private String thumb;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(endpoint);
        parcel.writeString(thumb);
    }
}
