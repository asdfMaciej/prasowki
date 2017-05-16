package io.maciej01.prasowki.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

import io.maciej01.prasowki.activity.MainActivity;
import io.maciej01.prasowki.activity.MyApplication;
import io.maciej01.prasowki.activity.PrasowkaActivity;
import io.maciej01.prasowki.helper.DBHelper;
import io.maciej01.prasowki.helper.PrasowkiFetcher;
import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPresenter implements PrasowkiFetcher.PrasowkiFetcherCallback {
    static int AMOUNT_OF_PAGES_FETCHED = 3;

    @Override
    public void doneLoading() {
        viewContract.hideSpinner();
    }

    @Override
    public void noInternetConnection() {
        viewContract.showSnackbar("Brak połączenia z internetem!", Snackbar.LENGTH_INDEFINITE);
    }

    public interface ViewContract {
        String getActivityName();
        void updateRecyclerView();
        void _refreshRecyclerView(String url);
        void showSpinner();
        void hideSpinner();
        void openPrasowka(Prasowka p, int n);
        void showSnackbar(String txt, int length);
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

    public ViewContract getViewContract() { return viewContract; }

    public void fetch() {
        if (this.viewContract == null) {return;}
        PrasowkiFetcher pf = new PrasowkiFetcher(this);
        try {
            viewContract.showSpinner();
            Log.v("mainpresenter", "begin fetch");
            if (viewContract.getActivityName().equals("MainFragment")) {
                pf.fetch_pages(AMOUNT_OF_PAGES_FETCHED);
            } else if (viewContract.getActivityName().equals("PrasowkaActivity")) {
                if (((PrasowkaActivity) viewContract).getPrasowka().getDesc().isEmpty()) {
                    pf.fetch_article(((PrasowkaActivity) viewContract).getPrasowka().getUrlArticle());
                } else {
                    doneLoading();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrasowkiList getListBySectionNumber() {
        int n = viewContract.getSectionNumber();
        return getListBySectionNumber(n);
    }
    public PrasowkiList getListBySectionNumber(int n) {
        PrasowkiList lis = null;
        PrasowkiList lista = DBHelper.getInstance().getLista();
        if (n == 1) {lis = lista;}
        else if (n == 2) {lis = lista.getPolska();}
        else if (n == 3) {lis = lista.getSwiat();}
        lis.sort();
        return lis;
    }

    public void uponFetching() {
        viewContract.updateRecyclerView();
        Log.v("mainpresenter", "updaterecycler");
    }
    private void initializeView() {
        if (this.viewContract == null) {Log.v("mainpresenter", "init failed due to null viewcontract");return;}
        MyApplication app = ((MyApplication) viewContract.getAct().getApplication());
        if (!app.getFetched()) {
            // check if internet
            if ((new PrasowkiFetcher(this)).isOnline()) {
                app.setFetched(true);
                Log.v("mainpresenter", "fetching");
                fetch();
            } else {
                noInternetConnection();
            }
        } else if (viewContract.getActivityName() != "MainFragment") {
            Log.v("mainpresenter", "fetching other than main");
            fetch();
        }
    }
    private void onAttach() {}
    private void onDetach() {}
}
