package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GenreDetailModel implements Parcelable {
    public GenreDetailModel(Parcel in) {
        title = in.readString();
        type = in.readString();
        thumb = in.readString();
        endpoint = in.readString();
    }

    public static final Creator<GenreDetailModel> CREATOR = new Creator<GenreDetailModel>() {
        @Override
        public GenreDetailModel createFromParcel(Parcel in) {
            return new GenreDetailModel(in);
        }

        @Override
        public GenreDetailModel[] newArray(int size) {
            return new GenreDetailModel[size];
        }
    };

    public GenreDetailModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private String title;
    private String type;
    private String thumb;
    private String endpoint;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(thumb);
        parcel.writeString(endpoint);
    }
}
