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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItem feedItem = (FeedItem) o;

        if (title != null ? !title.equals(feedItem.title) : feedItem.title != null) return false;
        if (link != null ? !link.equals(feedItem.link) : feedItem.link != null) return false;
        if (media != null ? !media.equals(feedItem.media) : feedItem.media != null) return false;
        if (published != null ? !published.equals(feedItem.published) : feedItem.published != null)
            return false;
        if (author != null ? !author.equals(feedItem.author) : feedItem.author != null)
            return false;
        return tags != null ? tags.equals(feedItem.tags) : feedItem.tags == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (media != null ? media.hashCode() : 0);
        result = 31 * result + (published != null ? published.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

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
