package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListGenreModel implements Parcelable {
    public ListGenreModel(Parcel in) {
        title = in.readString();
        endpoint = in.readString();
    }

    public static final Creator<ListGenreModel> CREATOR = new Creator<ListGenreModel>() {
        @Override
        public ListGenreModel createFromParcel(Parcel in) {
            return new ListGenreModel(in);
        }

        @Override
        public ListGenreModel[] newArray(int size) {
            return new ListGenreModel[size];
        }
    };

    public ListGenreModel() {

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

    private String title;
    private String endpoint;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(endpoint);
    }
}
