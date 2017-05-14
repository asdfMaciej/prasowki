package io.maciej01.prasowki.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainFragment extends Fragment implements MainPresenter.ViewContract {
    /*
    TO-DO:
        * Implement savedInstanceState
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    MainPresenter mainPresenter;
    View rootView;
    RecyclerView recyclerView;
    MainPrasowkaAdapter adapter;
    MainActivity context;

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
    public void test() {
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
    }

    @Override
    public int getSectionNumber() {
        return getArguments().getInt(ARG_SECTION_NUMBER);
    }

    private void initFragment() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        PrasowkiList lista = DBHelper.getInstance().getLista();
        adapter = new MainPrasowkaAdapter(recyclerView, lista, context);
        recyclerView.setAdapter(adapter);
    }
}