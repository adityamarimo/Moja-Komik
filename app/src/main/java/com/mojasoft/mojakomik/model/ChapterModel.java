package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterModel implements Parcelable {
    public ChapterModel(Parcel in) {
        chapter_title = in.readString();
        chapter_endpoint = in.readString();
    }

    public static final Creator<ChapterModel> CREATOR = new Creator<ChapterModel>() {
        @Override
        public ChapterModel createFromParcel(Parcel in) {
            return new ChapterModel(in);
        }

        @Override
        public ChapterModel[] newArray(int size) {
            return new ChapterModel[size];
        }
    };

    public ChapterModel() {

    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getChapter_endpoint() {
        return chapter_endpoint;
    }

    public void setChapter_endpoint(String chapter_endpoint) {
        this.chapter_endpoint = chapter_endpoint;
    }

    private String chapter_title;
    private String chapter_endpoint;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chapter_title);
        parcel.writeString(chapter_endpoint);
    }
}
