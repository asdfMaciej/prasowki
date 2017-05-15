package io.maciej01.prasowki.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.maciej01.prasowki.R;
import io.maciej01.prasowki.activity.MyApplication;
import io.maciej01.prasowki.helper.DBHelper;
import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;
import io.maciej01.prasowki.presenter.MainPresenter;


/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPrasowkaAdapter extends RecyclerView.Adapter {
    private RecyclerView recyclerView;
    private PrasowkiList prasowkiList;
    private Context context;
    private MainPresenter.ViewContract presenter;
    private int color;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tCategory;
        TextView tDate;
        TextView tSummary;
        TextView tTitle;
        public LinearLayout ll;

        public ViewHolder(View v) {
            super(v);
            tCategory = (TextView) v.findViewById(R.id.txtCategory);
            tDate = (TextView) v.findViewById(R.id.txtDate);
            tSummary = (TextView) v.findViewById(R.id.txtSummary);
            tTitle = (TextView) v.findViewById(R.id.txtTitle);
            ll = (LinearLayout) v.findViewById(R.id.llPrasowka);
            ll.setTransitionName("linear");
        }
    }

    public MainPrasowkaAdapter(RecyclerView recyclerView, PrasowkiList prasowkiList, Context context, MainPresenter.ViewContract presenter, int color) {
        this.recyclerView = recyclerView;
        this.prasowkiList = prasowkiList;
        this.context = context;
        this.presenter = presenter;
        this.color = color;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.frag_main_prasowka, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerView.getChildAdapterPosition(v);
                Prasowka p = prasowkiList.get(pos);
                MainPrasowkaAdapter.this.onClick(p, pos);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Prasowka p = prasowkiList.get(position);
        ((ViewHolder) holder).tDate.setText(p.getDateString());
        ((ViewHolder) holder).tCategory.setText(p.getCategory().toUpperCase());
        ((ViewHolder) holder).tSummary.setText(p.getSummary());
        ((ViewHolder) holder).tTitle.setText(p.getTitle());

        setColors(holder, position);
        //Pair<View, String> pair1 = Pair.create((View) ((ViewHolder) holder).ll, ((ViewHolder) holder).ll.getTransitionName());

        //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1, pair2);
    }

    public void setColors(RecyclerView.ViewHolder holder, int position) {
        int c;
        if (color == -1) {
            c = getColorByPosition(position);
        } else {c = getColorByPosition(color);}
        ((ViewHolder) holder).tCategory.setTextColor(c);
        ((ViewHolder) holder).ll.setBackgroundColor(c);
    }

    @Override
    public int getItemCount() {
        return prasowkiList.size();
    }

    public void onClick(Prasowka p, int n) {
        presenter.openPrasowka(p, n);
    }
    private int getColorByPosition(int n) {
        Integer[] kolory = {R.color.cRand1, R.color.cRand2, R.color.cRand3, R.color.cRand4};
        int kolor = context.getResources().getColor(kolory[n % 4]);
        return kolor;
    }

}
