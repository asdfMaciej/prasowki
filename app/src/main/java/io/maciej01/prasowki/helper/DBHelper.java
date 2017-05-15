package io.maciej01.prasowki.helper;

import io.maciej01.prasowki.model.Prasowka;
import io.maciej01.prasowki.model.PrasowkiList;

/**
 * Created by Maciej on 2017-05-14.
 */
public class DBHelper {
    private static DBHelper ourInstance = new DBHelper();
    private PrasowkiList lista = new PrasowkiList();

    public static DBHelper getInstance() {
        return ourInstance;
    }

    public PrasowkiList getLista() {
        return lista;
    }
    public void sql_delete_table() {
        Prasowka.executeQuery("DELETE FROM PRASOWKA;");
        this.lista.clear();
    }
    private DBHelper() {
    }
}
