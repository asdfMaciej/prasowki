package io.maciej01.prasowki.model;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maciej on 2017-05-14.
 */

public class Prasowka extends SugarRecord implements Serializable, Cloneable, Comparable<Prasowka> {
    private String pTitle = "";
    private String pDateString = "";
    private String pCategory = ""; // świat/polska
    private String pDesc = "";
    private String pSummary = ""; // mini desc from main page
    private String pUrlArticle = "";
    private String pUrlImage = "";  // unused? idk, depends on how i'll implement view

    public Prasowka() {}
    public Prasowka(String title, String dateString, String summary) {
        this.pTitle = title;
        this.pDateString = dateString;
        this.pSummary = summary;
    }

    public Prasowka clone() {
        try {
            return (Prasowka) super.clone();
        } catch (CloneNotSupportedException e) { e.printStackTrace(); }
        return new Prasowka();
    }

    public boolean equals(Prasowka p) {
        boolean bEquals = true;
        if (!p.getTitle().equals(this.pTitle)) {bEquals = false;}
        if (!p.getDateString().equals(this.pDateString)) {bEquals = false;}
        if (!p.getCategory().equals(this.pCategory)) {bEquals = false;}
        if (!p.getDesc().equals(this.pDesc)) {bEquals = false;}
        if (!p.getSummary().equals(this.pSummary)) {bEquals = false;}
        if (!p.getUrlArticle().equals(this.pUrlArticle)) {bEquals = false;}
        if (!p.getUrlImage().equals(this.pUrlImage)) {bEquals = false;}
        return bEquals;
    }

    public boolean fEquals(Prasowka p) {
        boolean bEquals = true;
        if (!p.getUrlArticle().equals(this.pUrlArticle)) {bEquals = false;}
        return bEquals;
    }

    public void setEssentials(String title, String summary,
                              String date, String urlArticle, String urlImage) {
        this.pTitle = title;
        this.pSummary = summary;
        this.pDateString = date;
        this.pUrlArticle = urlArticle;
        this.pUrlImage = urlImage;
    }

    public void setDetails(String category, String desc) {
        this.pCategory = category;
        this.pDesc = desc;
    }

    public Date getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");
        Date d = null;
        try {
            d = dateFormat.parse(pDateString);
        } catch (ParseException e) { // this literally won't ever happen
            e.printStackTrace();
        }
        return d;
    }

    public String getTitle() { return this.pTitle; }
    public String getDateString() { return this.pDateString; }
    public String getCategory() { return this.pCategory; }
    public String getDesc() { return this.pDesc; }
    public String getSummary() { return this.pSummary; }
    public String getUrlArticle() { return this.pUrlArticle; }
    public String getUrlImage() { return this.pUrlImage; }

    @Override
    public int compareTo(Prasowka p) {
        return getDate().compareTo(p.getDate());
    }

}
