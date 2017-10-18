package com.ipvans.flickrgallery.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ipvans.flickrgallery.R;
import com.ipvans.flickrgallery.ui.App;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainPresenter<MainViewState> mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainViewComponent.builder()
                .applicationComponent(App.COMPONENT)
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.onAttach();
        mainPresenter.observe()
                .subscribe(state -> Log.d("flickr", state.toString()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.onDetach();
    }
}
