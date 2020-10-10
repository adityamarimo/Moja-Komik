package com.mojasoft.mojakomik.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DetailMangaModel implements Parcelable {
    private String title;
    private String type;

    protected DetailMangaModel(Parcel in) {
        title = in.readString();
        type = in.readString();
        author = in.readString();
        status = in.readString();
        manga_endpoint = in.readString();
        thumb = in.readString();
        synopsis = in.readString();
        listGenreModel = in.createTypedArrayList(ListGenreModel.CREATOR);
    }

    public static final Creator<DetailMangaModel> CREATOR = new Creator<DetailMangaModel>() {
        @Override
        public DetailMangaModel createFromParcel(Parcel in) {
            return new DetailMangaModel(in);
        }

        @Override
        public DetailMangaModel[] newArray(int size) {
            return new DetailMangaModel[size];
        }
    };

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManga_endpoint() {
        return manga_endpoint;
    }

    public void setManga_endpoint(String manga_endpoint) {
        this.manga_endpoint = manga_endpoint;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<ListGenreModel> getListGenreModel() {
        return listGenreModel;
    }

    public void setListGenreModel(List<ListGenreModel> listGenreModel) {
        this.listGenreModel = listGenreModel;
    }

    private String author;
    private String status;
    private String manga_endpoint;
    private String thumb;
    private String synopsis;

    private List<ListGenreModel> listGenreModel;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(author);
        parcel.writeString(status);
        parcel.writeString(manga_endpoint);
        parcel.writeString(thumb);
        parcel.writeString(synopsis);
        parcel.writeTypedList(listGenreModel);
    }
}

