package com.ipvans.flickrgallery.ui.main;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ipvans.flickrgallery.R;
import com.ipvans.flickrgallery.data.model.FeedItem;
import com.ipvans.flickrgallery.ui.App;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String RECYCLER_STATE ="RECYCLER_STATE";
    private static final String SCREEN_STATE ="SCREEN_STATE";
    private static final String MENU_STATE ="MENU_STATE";

    @Inject
    MainPresenter<MainViewState> mainPresenter;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipe;
    private View empty;
    private View error;
    private View progress;

    private MenuItem search;
    private SearchView searchView;
    private Snackbar snackbar;

    private String savedMenuState;

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

        mainPresenter.onAttach();
        mainPresenter.observe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData);

        if (savedInstanceState == null)
            mainPresenter.search("", false);
        else
            restoreState(savedInstanceState);
    }

    private void initViews() {
        adapter = new MainAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> mainPresenter.search("", true));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(SCREEN_STATE, mainPresenter.getLatestState());
        outState.putString(MENU_STATE, mainPresenter.getLatestState().getTags());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDetach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        search = toolbar.getMenu().findItem(R.id.action_search);
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        searchView = (SearchView) search.getActionView();
        if (!TextUtils.isEmpty(savedMenuState)) {
            search.expandActionView();
            searchView.setQuery(savedMenuState, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainPresenter.search(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals(mainPresenter.getLatestState().getTags()))
                    mainPresenter.search(newText, true);
                return true;
            }
        });

        return true;
    }

    private void restoreState(Bundle bundle) {
        Parcelable recyclerState = bundle.getParcelable(RECYCLER_STATE);
        MainViewState screenState = bundle.getParcelable(SCREEN_STATE);
        savedMenuState = bundle.getString(MENU_STATE);
        if (recyclerState != null && screenState != null) {
            screenState.tags = savedMenuState;
            screenState.extra = recyclerState;
            mainPresenter.restoreState(screenState);
        } else {
            mainPresenter.search("", false);
        }
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
        if (state.extra != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(state.extra);
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
