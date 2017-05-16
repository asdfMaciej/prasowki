package io.maciej01.prasowki.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.maciej01.prasowki.R;
import io.maciej01.prasowki.adapter.MainPrasowkaAdapter;
import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;
import io.maciej01.prasowki.presenter.MainPresenter;

/**
 * Created by Maciej on 2017-05-15.
 */

public class PrasowkaActivity extends AppCompatActivity implements MainPresenter.ViewContract {
    RecyclerView recyclerView;
    MainPrasowkaAdapter adapter = null;
    Prasowka prasowka;
    MainPresenter mainPresenter;
    int presenterSection;
    SpinnerDialog spinnerDialog = null;
    boolean dontdouble = true;
    int list_pos;
    @BindView(R.id.txtPrasowkaDesc) TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_prasowka);
        ButterKnife.bind(this);
        Intent i = getIntent();
        prasowka = (Prasowka) i.getSerializableExtra("prasowka");
        presenterSection = (int) i.getSerializableExtra("presenter");
        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        initActivity();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActivity() {
        if (dontdouble) {dontdouble = false;} else {return;}
        recyclerView = (RecyclerView) findViewById(R.id.recPrasowka);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        dontdouble = true;
        initAdapter();
    }
    private void initAdapter() {
        if (dontdouble) {dontdouble = false;} else {return;}
        if (recyclerView != null) {
            ArrayList<Prasowka> _l = new ArrayList<>();
            _l.add(prasowka);
            PrasowkiList meme = new PrasowkiList(_l);
            list_pos = mainPresenter.getListBySectionNumber().findFastIndex(prasowka);
            adapter = new MainPrasowkaAdapter(recyclerView, meme, this, this, list_pos);
            recyclerView.setAdapter(adapter);
            desc.setText(prasowka.getDesc());
        }
        dontdouble = true;
    }

    public Prasowka getPrasowka() {
        return prasowka;
    }

    @Override
    public String getActivityName() {
        return "PrasowkaActivity";
    }

    @Override
    public void updateRecyclerView() {
        int n = mainPresenter.getListBySectionNumber().findFastIndex(prasowka);
        prasowka = mainPresenter.getListBySectionNumber().get(n);
        initAdapter();
    }

    @Override
    public void _refreshRecyclerView(String url) {

    }

    @Override
    public void showSpinner() {
        android.app.FragmentManager fm = getFragmentManager();
        if (spinnerDialog != null) {
            spinnerDialog.dismiss();
        }
        spinnerDialog = new SpinnerDialog();
        spinnerDialog.show(fm, "some_tag");
    }

    @Override
    public void hideSpinner() {
        if (spinnerDialog != null) {
            spinnerDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("position", list_pos);
        i.putExtra("url", prasowka.getUrlArticle());
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }
    @Override
    public void openPrasowka(Prasowka p, int n) {
        return;
    }

    @Override
    public void showSnackbar(String txt, int length) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.llPrasowka);
        Snackbar snackbar = Snackbar
                .make(ll, txt, length);
        snackbar.show();
    }

    @Override
    public Activity getAct() {
        return this;
    }

    @Override
    public int getSectionNumber() {return presenterSection;}
}
