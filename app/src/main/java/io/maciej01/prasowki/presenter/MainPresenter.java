package io.maciej01.prasowki.presenter;

import android.util.Log;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPresenter {
    public interface ViewContract {
        void test();
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

    private void initializeView() {
        Log.v("mainpresenter", "initialize");
    }
    private void onAttach() {viewContract.test();}
    private void onDetach() {}
}
