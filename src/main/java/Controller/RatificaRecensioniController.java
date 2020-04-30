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

    public Recensione getRecensioneFromListaByIndirizzoAndNickname(String indirizzo, String nickname) {
        Iterator<Recensione> iterator = listaRecensioni.iterator();
        Recensione recensione = null;
        int trovataRecensione = 0;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensione = iterator.next();
            if (recensione.getIndirizzo().equals(indirizzo) && recensione.getNickname().equals(nickname)) {
                trovataRecensione = 1;
            }
        }
        return recensione;
    }

    private int getIdRecensioneFromListaByIndirizzoAndNickname(String indirizzo, String nickname) {
        Iterator<Recensione> iterator = listaRecensioni.iterator();
        Recensione recensione = null;
        int trovataRecensione = 0,idRecensione = -1;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensione = iterator.next();
            if (recensione.getIndirizzo().equals(indirizzo) && recensione.getNickname().equals(nickname)) {
                idRecensione = recensione.getIdRecensione();
                trovataRecensione = 1;
            }
        }
        return idRecensione;
    }

    public boolean deleteReview(String indirizzo, String nickname) {
        boolean result = false;
        int idRecensione = -1;
        if (awsLambdaSettings.checkInternetConnection()) {
            idRecensione = getIdRecensioneFromListaByIndirizzoAndNickname(indirizzo,nickname);
            recensioneDAO.deleteReviewFromDatabase(String.valueOf(idRecensione));
            result = true;
        }
        return result;
    }

    public ObservableList<RecensioneTableViewRatifica> deleteReviewFromTableViewList(String indirizzo, String nickname, ObservableList<RecensioneTableViewRatifica> listaRecensioniTableView) {
        Iterator<RecensioneTableViewRatifica> iterator = listaRecensioniTableView.iterator();
        RecensioneTableViewRatifica recensioneTableViewRatifica;
        int trovataRecensione = 0;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensioneTableViewRatifica = iterator.next();
            if (recensioneTableViewRatifica.getIndirizzo().equals(indirizzo) && recensioneTableViewRatifica.getNickname().equals(nickname)) {
                iterator.remove();
                trovataRecensione = 1;
            }
        }
        return listaRecensioniTableView;
    }

    public boolean acceptReview(String indirizzo, String nickname) {
        boolean result = false;
        int idRecensione = getIdRecensioneFromListaByIndirizzoAndNickname(indirizzo,nickname);

        if (awsLambdaSettings.checkInternetConnection()) {
            recensioneDAO.acceptReviewDatabase(String.valueOf(idRecensione));
            result = true;
        }
        return result;
    }
}
