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
    }

    public void add(Prasowka p) {
        lista.add(p);
        p.save();
    }
    public void add(int n, Prasowka p) {
        lista.add(n, p);
        p.save();
    }

    public Integer findIndex(Prasowka pras) {
        Integer index = null;
        for (Prasowka p : lista) {
            if (index == null) {index = 0;}
            if (pras.equals(p)) {return index;}
            index += 1;
        }
        return null;
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
