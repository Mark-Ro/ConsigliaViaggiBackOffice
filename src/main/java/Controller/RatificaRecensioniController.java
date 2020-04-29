package Controller;

import DAO.AWSLambdaSettings;
import DAO.RecensioneDAO;
import Entity.Recensione;
import GUI.RecensioneTableViewRatifica;
import GUI.UtenteTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;

public class RatificaRecensioniController {

    private AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
    private RecensioneDAO recensioneDAO = new RecensioneDAO();

    private ArrayList<Recensione> listaRecensioni;

    public boolean queryListaRecensioniFromDatabase() {
        boolean result = false;
        System.out.println("Entrato nella Query!");
        if (awsLambdaSettings.checkInternetConnection()) {
            listaRecensioni = recensioneDAO.getListaRecensioniForRatificaFromDatabase();
            result = true;
        }
        System.out.println("Query finita!");
        return result;
    }

    public ObservableList<RecensioneTableViewRatifica> getListaRecensioniTable() {
        Recensione recensione;
        ObservableList<RecensioneTableViewRatifica> listaRecensioniTabella = null;
        if (listaRecensioni!=null) {
            listaRecensioniTabella = FXCollections.observableArrayList();
            Iterator<Recensione> iterator = listaRecensioni.iterator();
            while (iterator.hasNext()) {
                recensione = iterator.next();
                listaRecensioniTabella.add(new RecensioneTableViewRatifica(recensione.getNomeStruttura(),recensione.getNickname(),recensione.getIndirizzo(),recensione.getNomeCitta()));
            }
        }
        return listaRecensioniTabella;
    }

    public Recensione getRecensioneFromListaByIndirizzo(String indirizzo) {
        Iterator<Recensione> iterator = listaRecensioni.iterator();
        Recensione recensione = null;
        int trovataRecensione = 0;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensione = iterator.next();
            if (recensione.getIndirizzo().equals(indirizzo)) {
                trovataRecensione = 1;
            }
        }
        return recensione;
    }

    private int getIdRecensioneFromListaByIndirizzo(String indirizzo) {
        Iterator<Recensione> iterator = listaRecensioni.iterator();
        Recensione recensione = null;
        int trovataRecensione = 0,idRecensione = -1;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensione = iterator.next();
            if (recensione.getIndirizzo().equals(indirizzo)) {
                idRecensione = recensione.getIdRecensione();
                trovataRecensione = 1;
            }
        }
        return idRecensione;
    }

    public boolean deleteReview(String indirizzo) {
        boolean result = false;
        int idRecensione = -1;
        if (awsLambdaSettings.checkInternetConnection()) {
            idRecensione = getIdRecensioneFromListaByIndirizzo(indirizzo);
            recensioneDAO.deleteReviewFromDatabase(String.valueOf(idRecensione));
            result = true;
        }
        return result;
    }

    public ObservableList<RecensioneTableViewRatifica> deleteReviewFromTableViewList(String indirizzo, ObservableList<RecensioneTableViewRatifica> listaRecensioniTableView) {
        Iterator<RecensioneTableViewRatifica> iterator = listaRecensioniTableView.iterator();
        RecensioneTableViewRatifica recensioneTableViewRatifica;
        int trovataRecensione = 0;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensioneTableViewRatifica = iterator.next();
            if (recensioneTableViewRatifica.getIndirizzo().equals(indirizzo)) {
                iterator.remove();
                trovataRecensione = 1;
            }
        }
        return listaRecensioniTableView;
    }
}
