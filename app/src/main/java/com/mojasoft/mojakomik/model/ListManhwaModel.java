package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListManhwaModel implements Parcelable {
    public ListManhwaModel(Parcel in) {
        title = in.readString();
        thumb = in.readString();
        type = in.readString();
        updated_on = in.readString();
        endpoint = in.readString();
        chapter = in.readString();
    }

    public static final Creator<ListManhwaModel> CREATOR = new Creator<ListManhwaModel>() {
        @Override
        public ListManhwaModel createFromParcel(Parcel in) {
            return new ListManhwaModel(in);
        }

        @Override
        public ListManhwaModel[] newArray(int size) {
            return new ListManhwaModel[size];
        }
    };

    public ListManhwaModel() {

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

    private String title;
    private String thumb;
    private String type;
    private String updated_on;
    private String endpoint;
    private String chapter;

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
