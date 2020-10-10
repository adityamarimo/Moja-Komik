package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularModel implements Parcelable {
    public PopularModel(Parcel in) {
        title = in.readString();
        type = in.readString();
        thumb = in.readString();
        endpoint = in.readString();
        upload_on = in.readString();
    }

    public static final Creator<PopularModel> CREATOR = new Creator<PopularModel>() {
        @Override
        public PopularModel createFromParcel(Parcel in) {
            return new PopularModel(in);
        }

        @Override
        public PopularModel[] newArray(int size) {
            return new PopularModel[size];
        }
    };

    public PopularModel() {

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

    public String getUpload_on() {
        return upload_on;
    }

    public void setUpload_on(String upload_on) {
        this.upload_on = upload_on;
    }

    private String title;
    private String type;
    private String thumb;
    private String endpoint;
    private String upload_on;

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
        parcel.writeString(upload_on);
    }
}
