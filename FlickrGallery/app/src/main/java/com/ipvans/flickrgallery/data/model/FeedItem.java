package com.ipvans.flickrgallery.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FeedItem implements Parcelable {

    private String title;
    private String link;
    private Media media;
    private String published;
    private String author;
    private String tags;

    public FeedItem() {
    }

    protected FeedItem(Parcel in) {
        title = in.readString();
        link = in.readString();
        media = in.readParcelable(Media.class.getClassLoader());
        published = in.readString();
        author = in.readString();
        tags = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeParcelable(media, flags);
        dest.writeString(published);
        dest.writeString(author);
        dest.writeString(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FeedItem> CREATOR = new Creator<FeedItem>() {
        @Override
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in);
        }

        @Override
        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Media getMedia() {
        return media;
    }

    public String getPublished() {
        return published;
    }

    public String getAuthor() {
        return author;
    }

    public String getTags() {
        return tags;
    }
}
