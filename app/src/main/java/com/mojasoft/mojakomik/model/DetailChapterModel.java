package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailChapterModel implements Parcelable {
    private String title;
    private String chapter_image_link;

    public DetailChapterModel(Parcel in) {
        title = in.readString();
        chapter_image_link = in.readString();
        image_number = in.readString();
        download_link = in.readString();
    }

    public static final Creator<DetailChapterModel> CREATOR = new Creator<DetailChapterModel>() {
        @Override
        public DetailChapterModel createFromParcel(Parcel in) {
            return new DetailChapterModel(in);
        }

        @Override
        public DetailChapterModel[] newArray(int size) {
            return new DetailChapterModel[size];
        }
    };

    public DetailChapterModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapter_image_link() {
        return chapter_image_link;
    }

    public void setChapter_image_link(String chapter_image_link) {
        this.chapter_image_link = chapter_image_link;
    }

    public String getImage_number() {
        return image_number;
    }

    public void setImage_number(String image_number) {
        this.image_number = image_number;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    private String image_number;
    private String download_link;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(chapter_image_link);
        parcel.writeString(image_number);
        parcel.writeString(download_link);
    }
}
