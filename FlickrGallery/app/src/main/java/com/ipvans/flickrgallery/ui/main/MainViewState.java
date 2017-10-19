package com.ipvans.flickrgallery.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.ipvans.flickrgallery.data.model.Feed;

public class MainViewState implements Parcelable {

    private final boolean loading;
    private final Throwable error;
    private final Feed data;

    public String tags;
    public Parcelable extra;

    public MainViewState(boolean loading, Throwable error, Feed data, String tags) {
        this.loading = loading;
        this.error = error;
        this.data = data;
        this.tags = tags;
    }

    protected MainViewState(Parcel in) {
        loading = in.readByte() != 0;
        error = (Throwable) in.readSerializable();
        data = in.readParcelable(Feed.class.getClassLoader());
        tags = in.readString();
    }

    public static final Creator<MainViewState> CREATOR = new Creator<MainViewState>() {
        @Override
        public MainViewState createFromParcel(Parcel in) {
            return new MainViewState(in);
        }

        @Override
        public MainViewState[] newArray(int size) {
            return new MainViewState[size];
        }
    };

    public boolean isLoading() {
        return loading;
    }

    public Throwable getError() {
        return error;
    }

    public Feed getData() {
        return data;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "MainViewState{" +
                "loading=" + loading +
                ", error=" + error +
                ", data=" + data +
                ", tags='" + tags + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (loading ? 1 : 0));
        dest.writeSerializable(error);
        dest.writeParcelable(data, flags);
        dest.writeString(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MainViewState that = (MainViewState) o;

        if (loading != that.loading) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = (loading ? 1 : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    public static MainViewState empty() {
        return new MainViewState(false, null, null, "");
    }
}
