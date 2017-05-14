package io.maciej01.prasowki.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.maciej01.prasowki.R;
import io.maciej01.prasowki.adapter.MainPrasowkaAdapter;
import io.maciej01.prasowki.helper.DBHelper;
import io.maciej01.prasowki.model.PrasowkiList;
import io.maciej01.prasowki.presenter.MainPresenter;

/**
 * Created by Maciej on 2017-05-13.
 */

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements MainPresenter.ViewContract {
    /*
    TO-DO:
        * Implement savedInstanceState
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    MainPresenter mainPresenter;
    View rootView;
    RecyclerView recyclerView;
    MainPrasowkaAdapter adapter = null;
    MainActivity context;
    PrasowkiList lista = DBHelper.getInstance().getLista();

    @SuppressLint("ValidFragment")
    public MainFragment(MainActivity context) {
        this.context = context;
    }

    public static MainFragment newInstance(MainActivity context, int sectionNumber) {
        MainFragment fragment = new MainFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main, container, false);
        rootView = root;
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        initFragment();
        return rootView;
    }

    @Override
    public void updateRecyclerView() {
        context.updateRecyclerViews();
    }

    public void _updateRecyclerView() {
        if (adapter != null) {
            lista.sort();
            initAdapter();
            adapter.notifyDataSetChanged();
        } else {
            Log.v("mainfragment", "adapter is null");
        }
    }

    @Override
    public int getSectionNumber() {
        return getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public Activity getAct() { return context; }

    private void initFragment() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        initAdapter();
    }
    private void initAdapter() {
        PrasowkiList lis = null;
        int n = getSectionNumber();
        if (n == 1) {lis = lista;}
        else if (n == 2) {lis = lista.getPolska();}
        else if (n == 3) {lis = lista.getSwiat();}
        adapter = new MainPrasowkaAdapter(recyclerView, lis, context);
        recyclerView.setAdapter(adapter);
    }
}