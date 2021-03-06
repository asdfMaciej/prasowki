package io.maciej01.prasowki.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.maciej01.prasowki.R;
import io.maciej01.prasowki.adapter.MainPrasowkaAdapter;
import io.maciej01.prasowki.helper.DBHelper;
import io.maciej01.prasowki.model.Prasowka;
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
    SpinnerDialog spinnerDialog = null;
    boolean visible;

    @SuppressLint("ValidFragment")
    public MainFragment() {}
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
    public String getActivityName() {
        return "MainFragment";
    }

    @Override
    public void updateRecyclerView() {
        context.updateRecyclerViews();
    }

    @Override
    public void _refreshRecyclerView(String url) {
        Prasowka prasowka = new Prasowka();
        prasowka.setUrlArticle(url);
        ArrayList<Prasowka> _l = new ArrayList<>();
        _l.add(prasowka);
        if (mainPresenter != null) {
            Integer list_pos = mainPresenter.getListBySectionNumber(getSectionNumber()).findFastIndex(prasowka);
            if (list_pos != null) {
                adapter.notifyItemChanged(list_pos);
            }
        }
    }

    public void _updateRecyclerView() {
        if (adapter != null) {
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
    public Activity getAct() { return getActivity(); }

    @Override
    public void onStop() {
        mainPresenter.detachView();
        super.onStop();
    }

    @Override
    public void showSpinner() {
        FragmentManager fm = context.getFragmentManager();
        if (spinnerDialog != null) {
            spinnerDialog.dismiss();
        }
        if (visible) {
            spinnerDialog = new SpinnerDialog();
            spinnerDialog.show(fm, "some_tag");
        } else {
            Log.v("mainfragment", "attempted to showspinner without being visible");
        }
    }

    @Override
    public void hideSpinner() {
        if (spinnerDialog != null) {
            spinnerDialog.dismiss();
        }
    }

    @Override
    public void openPrasowka(Prasowka p, int n) {
        Intent i = new Intent(getAct(), PrasowkaActivity.class);
        i.putExtra("prasowka", p);
        i.putExtra("presenter", getSectionNumber());
        LinearLayout ll = (LinearLayout) recyclerView.findViewHolderForAdapterPosition(n).itemView.findViewById(R.id.llPrasowka);
        ll.setTransitionName("linear");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ll, "linear");
        startActivityForResult(i, 1, options.toBundle());
    }

    @Override
    public void showSnackbar(String txt, int length) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) context.findViewById(R.id.main_content);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, txt, length);
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //adapter.notifyItemChanged(data.getIntExtra("position", -1));
            context.refreshRecyclerViewsByUrl(data.getStringExtra("url"));
            //updateRecyclerView();
            //adapter.setPrasowkiList(mainPresenter.getListBySectionNumber(getSectionNumber()));
            //adapter.notifyDataSetChanged();
            Log.v("mainfragment", "returned from prasowkaactivity");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visible = isVisibleToUser;
    }

    private void initFragment() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        initAdapter();
    }
    private void initAdapter() {
        PrasowkiList lis = mainPresenter.getListBySectionNumber();
        adapter = new MainPrasowkaAdapter(recyclerView, lis, getAct(), this, -1);
        recyclerView.setAdapter(adapter);
    }

}