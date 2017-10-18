package com.ipvans.flickrgallery.ui.main;

import io.reactivex.Observable;

public interface MainPresenter<T> {

    void onAttach();

    void onDetach();

    void restoreState(T data);

    Observable<T> observe();

    T getLatestState();

    void search(String tags, boolean force);

}
