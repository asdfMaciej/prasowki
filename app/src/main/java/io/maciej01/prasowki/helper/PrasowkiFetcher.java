package io.maciej01.prasowki.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;
import io.maciej01.prasowki.presenter.MainPresenter;

/**
 * Created by Maciej on 2017-05-14.
 */

public class PrasowkiFetcher {

    MainPresenter callback;

    public PrasowkiFetcher(MainPresenter callback) {
        this.callback = callback;
    }
    private class FetchTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            Log.v("prasowkifetcher", "doinbackground");
            String url = strings[0];
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Log.v("prasowkifetcher", "connecturl");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document result) {
            Log.v("prasowkifetcher", "onpostexecute");
            PrasowkiFetcher.this.parse(result);

        }
    }
    public void fetch() throws IOException {
        String url = "http://prasowki.org/";
        new FetchTask().execute(url);
    }

    public void parse(Document doc) {
        Log.v("prasowkifetcher", "parse");
        Elements h2s = doc.select("article.cb-blog-style-a");
        Elements big3 = doc.select("div.cb-grid-feature");
        ArrayList<Prasowka> tempArray = new ArrayList<>();
        for (Element e : h2s) { tempArray.add(prasowkaFromElement(e)); }
        for (Element e : big3) { tempArray.add(prasowkaFromBig(e)); }

        DBHelper help = DBHelper.getInstance();
        PrasowkiList lista = help.getLista();

        for (Prasowka p : tempArray) {
            if (!lista.isIn(p)) {lista.add(p);}
        }
        Log.v("prasowkifetcher", doc.title());
        callback.uponFetching();

    }

    private Prasowka prasowkaFromElement(Element e) {
        String title = e.select("h2.cb-post-title").get(0).text();
        String dateString = e.select("span.cb-date").get(0).text();
        String summary = e.select("div.cb-excerpt").get(0).text();
        String category = categoryFromClass(e.className());
        String urlArticle = e.select("div.cb-mask").get(0).select("a").attr("href");
        String urlImage = e.select("div.cb-mask").get(0).select("img").attr("src");

        Prasowka p = new Prasowka();
        p.setEssentials(title, summary, category, dateString, urlArticle, urlImage);
        return p;
    }

    private Prasowka prasowkaFromBig(Element e) {
        String title = e.select("h2").get(0).text();
        String dateString = e.select("span.cb-date").get(0).text();
        String summary = "";
        String category = categoryFromClass(e.className());
        String urlArticle = e.select("div.cb-grid-img").get(0).select("a").attr("href");
        String urlImage = e.select("div.cb-grid-img").get(0).select("img").attr("src");

        Prasowka p = new Prasowka();
        p.setEssentials(title, summary, category, dateString, urlArticle, urlImage);
        return p;
    }

    private String categoryFromClass(String s) {
        String category = null;
        if (s.contains("category-polska")) { category = "polska"; }
        if (s.contains("category-swiat")) { category = "świat"; }
        return category;
    }
}
