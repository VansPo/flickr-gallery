package com.ipvans.flickrgallery.ui.main;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ipvans.flickrgallery.R;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.model.FeedItem;
import com.ipvans.flickrgallery.ui.App;
import com.ipvans.flickrgallery.utils.DeviceUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainPresenter<MainViewState> mainPresenter;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipe;
    private View empty;
    private View error;
    private View progress;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        swipe = findViewById(R.id.swipe);
        empty = findViewById(R.id.empty);
        error = findViewById(R.id.error);
        progress = findViewById(R.id.progress);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        DaggerMainViewComponent.builder()
                .applicationComponent(App.COMPONENT)
                .build()
                .inject(this);

        initViews();
    }

    private void initViews() {
        adapter = new MainAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> mainPresenter.search("", true));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.onAttach();
        mainPresenter.observe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData);

        mainPresenter.search("", true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.onDetach();
    }

    private void setData(MainViewState state) {
        if (state.isLoading()) {
            showLoading();
        } else if (state.getError() != null) {
            showError(state.getError());
        } else {
            if (state.getData().getItems() != null && !state.getData().getItems().isEmpty())
                showContent(state.getData().getItems());
            else
                showEmpty();
        }
    }

    private void showLoading() {
        dismissSnackbar();
        if (adapter.isEmpty()) {
            progress.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
        } else {
            swipe.setRefreshing(true);
        }
    }

    private void showError(Throwable e) {
        dismissSnackbar();
        if (adapter.isEmpty()) {
            progress.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        } else {
            swipe.setRefreshing(false);
            snackbar = Snackbar.make(recyclerView, R.string.error, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void showContent(List<FeedItem> items) {
        dismissSnackbar();
        adapter.replace(items);
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        swipe.setRefreshing(false);
    }

    private void showEmpty() {
        dismissSnackbar();
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        swipe.setRefreshing(false);
    }

    private void dismissSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
