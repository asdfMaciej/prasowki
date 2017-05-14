package io.maciej01.prasowki.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import io.maciej01.prasowki.activity.MainActivity;
import io.maciej01.prasowki.activity.MyApplication;
import io.maciej01.prasowki.helper.PrasowkiFetcher;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPresenter {
    public interface ViewContract {
        void updateRecyclerView();
        Activity getAct();
        int getSectionNumber();
    }

    ViewContract viewContract;

    public void attachView(ViewContract viewContract) {
        this.viewContract = viewContract;
        onAttach();
        initializeView();
    }

    public void detachView() {
        onDetach();
        this.viewContract = null;
    }

    public void fetch() {
        PrasowkiFetcher pf = new PrasowkiFetcher(this);
        try {
            Log.v("mainpresenter", "begin fetch");
            pf.fetch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uponFetching() {
        viewContract.updateRecyclerView();
        Log.v("mainpresenter", "updaterecycler");
    }
    private void initializeView() {
        MyApplication app = ((MyApplication) viewContract.getAct().getApplication());
        if (!app.getFetched()) {
            app.setFetched(true);
            Log.v("mainpresenter", "fetching");
            fetch();
        }
    }
    private void onAttach() {}
    private void onDetach() {}
}
