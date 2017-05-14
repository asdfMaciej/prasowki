package io.maciej01.prasowki.adapter;

import android.content.Context;
import android.content.res.Resources;
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


/**
 * Created by Maciej on 2017-05-14.
 */

public class MainPrasowkaAdapter extends RecyclerView.Adapter {
    private RecyclerView recyclerView;
    private PrasowkiList prasowkiList;
    private Context context;

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView tCategory;
        TextView tDate;
        TextView tSummary;
        TextView tTitle;
        LinearLayout ll;

        public ViewHolder(View v) {
            super(v);
            tCategory = (TextView) v.findViewById(R.id.txtCategory);
            tDate = (TextView) v.findViewById(R.id.txtDate);
            tSummary = (TextView) v.findViewById(R.id.txtSummary);
            tTitle = (TextView) v.findViewById(R.id.txtTitle);
            ll = (LinearLayout) v.findViewById(R.id.llPrasowka);
        }
    }

    public MainPrasowkaAdapter(RecyclerView recyclerView, PrasowkiList prasowkiList, Context context) {
        this.recyclerView = recyclerView;
        this.prasowkiList = prasowkiList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.frag_main_prasowka, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Prasowka p = prasowkiList.get(position);
        ((ViewHolder) holder).ll.setBackgroundColor(getColorByPosition(position));
        ((ViewHolder) holder).tDate.setText(p.getDateString());
        ((ViewHolder) holder).tCategory.setText(p.getCategory().toUpperCase());
        ((ViewHolder) holder).tSummary.setText(p.getSummary());
        ((ViewHolder) holder).tTitle.setText(p.getTitle());
        ((ViewHolder) holder).tCategory.setTextColor(getColorByPosition(position));
    }

    @Override
    public int getItemCount() {
        return prasowkiList.size();
    }

    private int getColorByPosition(int n) {
        Integer[] kolory = {R.color.cRand1, R.color.cRand2, R.color.cRand3, R.color.cRand4};
        int kolor = context.getResources().getColor(kolory[n % 4]);
        return kolor;
    }

}
