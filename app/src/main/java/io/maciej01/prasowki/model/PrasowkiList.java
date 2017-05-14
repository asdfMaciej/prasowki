package io.maciej01.prasowki.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Maciej on 2017-05-14.
 */

public class PrasowkiList implements Serializable {
    private ArrayList<Prasowka> lista = new ArrayList<>();

    public PrasowkiList() {
        load();
    }
    public PrasowkiList(ArrayList<Prasowka> lista) {
        this.lista = lista;
    }

    public void load() {
        load_db();
        sort();
    }

    public void sort() {
        Collections.sort(lista);
        Collections.reverse(lista); // date descending
    }

    public void add(Prasowka p) {
        lista.add(p);
        p.save();
    }
    public void add(int n, Prasowka p) {
        lista.add(n, p);
        p.save();
    }


    public PrasowkiList getPolska() { return getByCategory("polska");}
    public PrasowkiList getSwiat() { return getByCategory("świat");}

    public Integer findIndex(Prasowka pras) {
        Integer index = null;
        for (Prasowka p : lista) {
            if (index == null) {index = 0;}
            if (pras.equals(p)) {return index;}
            index += 1;
        }
        return null;
    }

    public boolean isIn(Prasowka pras) {
        boolean jest = false;
        for (Prasowka p: lista) {
            if (pras.fEquals(p)) {jest = true;} // URL match only
        }
        return jest;
    }

    private PrasowkiList getByCategory(String category) {
        ArrayList<Prasowka> arr = new ArrayList<>();
        for (Prasowka p : lista) {
            if (p.getCategory().equals(category)) {
                arr.add(p);
            }
        }
        return new PrasowkiList(arr);
    }

    public int size() {return lista.size();}
    public Prasowka get(int n) {return lista.get(n);}
    public ArrayList<Prasowka> getLista() { return lista; }

    public void clear() {lista.clear();}
    public void remove(int n) {lista.remove(n);}
    public void set(int n, Prasowka p) {lista.set(n, p);}

    private void load_db() {
        lista = (ArrayList<Prasowka>) Prasowka.listAll(Prasowka.class);
    }

}
