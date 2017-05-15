package io.maciej01.prasowki.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    ArrayList<QueueTask> queue = new ArrayList<>();
    MainPresenter callback;
    String fetch_url; // referenced class-wide - this is probably a bad idea
    boolean fetch_single_article; // 0 for pages, 1 for article/s
    boolean working = false;
    boolean last_one;

    public interface PrasowkiFetcherCallback {
        void doneLoading();
        void noInternetConnection();
    }

    public PrasowkiFetcher(MainPresenter callback) {
        this.callback = callback;
    }
    private class QueueTask {
        public boolean f_single;
        public String page;
        public int amount;
        public QueueTask(boolean f_single, String page, int amount) {
            this.f_single = f_single;
            this.page = page;
            this.amount = amount;
        }
    }
    private class FetchTask extends AsyncTask<String, Void, Document> {
        int amount;

        @Override
        protected Document doInBackground(String... strings) {
            Log.v("prasowkifetcher", "doinbackground");
            String url = strings[0];
            amount = Integer.valueOf(strings[1]);
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
            amount -= 1;
            if (!(amount <= 0)) {
                PrasowkiFetcher.this.last_one = false;
                try {
                    Log.v("fetcher", "fetching n: "+Integer.toString(amount));
                    PrasowkiFetcher.this.fetch(page_to_url(amount), amount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                PrasowkiFetcher.this.last_one = true;
            }
            PrasowkiFetcher.this.parse(result);

        }
    }

    public boolean isWorking() {
        return working;
    }

    public void fetch_pages(int amount) throws IOException {
        if (checkForInternet()) {return;}
        if (!(1 > amount) && !(amount > 50)) {
            if (working) {
                Log.v("PrasowkiFetcher", "Will fetch later - added to queue");
                queue.add(new QueueTask(false, page_to_url(amount), amount));
            } else {
                working = true;
                fetch_single_article = false;
                fetch(page_to_url(amount), amount);
            }
        } else {
            throw new IOException("Amount of pages fetched must be 1 > n > 50!");
        }
    }

    public void fetch_article(String article_url) throws IOException {
        if (checkForInternet()) {return;}
        if (working) {
            Log.v("PrasowkiFetcher", "Will fetch later - added to queue");
            queue.add(new QueueTask(true, article_url, 1));
        } else {
            working = true;
            fetch_single_article = true;
            fetch(article_url, 1);
        }
    }


    public void parse(Document doc) {
        Log.v("prasowkifetcher", "parse");
        DBHelper help = DBHelper.getInstance();
        PrasowkiList lista = help.getLista();

        if (!this.fetch_single_article) {
            Elements h2s = doc.select("article.cb-blog-style-a");
            Elements big3 = doc.select("div.cb-grid-feature");
            ArrayList<Prasowka> tempArray = new ArrayList<>();
            for (Element e : h2s) {
                tempArray.add(prasowkaFromElement(e));
            }
            for (Element e : big3) {
                tempArray.add(prasowkaFromBig(e));
            }

            for (Prasowka p : tempArray) {
                if (!lista.isIn(p)) {
                    lista.add(p);
                } else {
                    int n = lista.findFastIndex(p);
                    if (lista.get(n).getSummary().isEmpty() && !p.getSummary().isEmpty()) {
                        lista.get(n).setSummary(p.getSummary());
                    }
                }
            }
        } else {
            String desc = doc.select("section.cb-entry-content").get(0).text();
            Prasowka _ptemp = new Prasowka();
            _ptemp.setUrlArticle(fetch_url);
            Integer n = lista.findFastIndex(_ptemp);
            lista.get(n).setDetails(desc);
            Log.v("PrasowkiFetcher", Integer.toString(n)+ " SetDetails: "+desc);
        }
        if (last_one) {done(); Log.v("prasowkifetcher", "last one");}
        Log.v("prasowkifetcher", doc.title());
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

    private void done() {
        callback.uponFetching();
        if (!queue.isEmpty()) {
            use_queue();
        } else {
            working = false;
            callback.doneLoading();
        }
    }
    private void use_queue() {
        Log.v("PrasowkiFetcher", "getting queue element..");
        QueueTask request = queue.get(0);
        queue.remove(0);
        fetch_single_article = request.f_single;
        String page = request.page;
        int amo = request.amount;
            try {
                fetch(page, amo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        //queue.add(new Object[]{fetch_single_article, page_to_url(amount), amount});
    }
    private void fetch(String url, int amount) throws IOException {
        fetch_url = url;
        new FetchTask().execute(fetch_url, Integer.toString(amount));
    }
    private String page_to_url(int page) {
        return "http://prasowki.org/page/"+Integer.toString(page)+"/";
    }
    private boolean checkForInternet() {
        if (!isOnline()) {
            Log.v("internet", "no internet connection");
            done();
            callback.noInternetConnection();
            return true;
        }
        return false;
    }
    private boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) callback.getViewContract().getAct().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return false;
    }
}
