package io.maciej01.prasowki.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import io.maciej01.prasowki.activity.MainActivity;
import io.maciej01.prasowki.activity.MyApplication;
import io.maciej01.prasowki.helper.DBHelper;
import io.maciej01.prasowki.helper.PrasowkiFetcher;
import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPresenter {
    static int AMOUNT_OF_PAGES_FETCHED = 3;
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
        if (this.viewContract == null) {return;}
        PrasowkiFetcher pf = new PrasowkiFetcher(this);
        try {
            Log.v("mainpresenter", "begin fetch");
            pf.fetch_pages(AMOUNT_OF_PAGES_FETCHED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrasowkiList getListBySectionNumber() {
        if (this.viewContract == null) {return null;}
        PrasowkiList lis = null;
        PrasowkiList lista = DBHelper.getInstance().getLista();
        int n = viewContract.getSectionNumber();
        if (n == 1) {lis = lista;}
        else if (n == 2) {lis = lista.getPolska();}
        else if (n == 3) {lis = lista.getSwiat();}
        lis.sort();
        return lis;
    }

    public void sortList() {
        if (this.viewContract == null) {return;}
        DBHelper.getInstance().getLista().sort();
    }

    public void uponFetching() {
        viewContract.updateRecyclerView();
        Log.v("mainpresenter", "updaterecycler");
    }
    private void initializeView() {
        if (this.viewContract == null) {Log.v("mainpresenter", "init failed due to null viewcontract");return;}
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
