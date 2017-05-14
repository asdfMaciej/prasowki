package io.maciej01.prasowki.presenter;

import android.util.Log;

import java.io.IOException;

import io.maciej01.prasowki.helper.PrasowkiFetcher;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPresenter {
    public interface ViewContract {
        void test();
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
    private void initializeView() {
        viewContract.test();
        /*if (viewContract.getSectionNumber() == 1) {
            Log.v("mainpresenter", "section number 1");
            fetch();
        } else {
            Log.v("mainpresenter", Integer.toString(viewContract.getSectionNumber()));
        }*/
    }
    private void onAttach() {}
    private void onDetach() {}
}
