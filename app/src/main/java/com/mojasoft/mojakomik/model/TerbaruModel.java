package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TerbaruModel implements Parcelable {
    String title;
    String thumb;
    String type;

    public TerbaruModel(Parcel in) {
        title = in.readString();
        thumb = in.readString();
        type = in.readString();
        updated_on = in.readString();
        endpoint = in.readString();
        chapter = in.readString();
    }

    public static final Creator<TerbaruModel> CREATOR = new Creator<TerbaruModel>() {
        @Override
        public TerbaruModel createFromParcel(Parcel in) {
            return new TerbaruModel(in);
        }

        @Override
        public TerbaruModel[] newArray(int size) {
            return new TerbaruModel[size];
        }
    };

    public TerbaruModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    String updated_on;
    String endpoint;
    String chapter;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(thumb);
        parcel.writeString(type);
        parcel.writeString(updated_on);
        parcel.writeString(endpoint);
        parcel.writeString(chapter);
    }
}
