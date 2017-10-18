package com.ipvans.flickrgallery.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Feed implements Parcelable {

    private String title;
    private String link;
    private String description;
    private String modified;
    private List<FeedItem> items;

    public Feed() {
    }

    public Feed(String title, String link, String description, String modified, List<FeedItem> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.modified = modified;
        this.items = items;
    }

    protected Feed(Parcel in) {
        title = in.readString();
        link = in.readString();
        description = in.readString();
        modified = in.readString();
        items = in.createTypedArrayList(FeedItem.CREATOR);
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(modified);
        dest.writeTypedList(items);
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getModified() {
        return modified;
    }

    public List<FeedItem> getItems() {
        return items;
    }
}
