package Controller;

import DAO.RecensioneDAO;
import Entity.Recensione;
import GUI.RatificaRecensioniPage;
import GUI.RecensioneTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class RatificaRecensioniController {

    private RatificaRecensioniPage ratificaRecensioniPage;
    private RecensioneDAO recensioneDAO;

    private ArrayList<Recensione> listaRecensioni;

    public RatificaRecensioniController(RatificaRecensioniPage ratificaRecensioniPage) {
        this.ratificaRecensioniPage = ratificaRecensioniPage;
        this.recensioneDAO = new RecensioneDAO();
    }

    public boolean queryListaRecensioni() {
        boolean result = false;
        System.out.println("Entrato nella Query!");
        if (checkInternetConnection()) {
            listaRecensioni = recensioneDAO.getListaRecensioniForRatificaFromDatabase();
            result = true;
        }
        System.out.println("Query finita!");
        return result;
    }

    public ObservableList<RecensioneTableView> getListaRecensioniTable() {
        ObservableList<RecensioneTableView> listaRecensioniTabella = null;
        if (listaRecensioni!=null) {
            listaRecensioniTabella = FXCollections.observableArrayList();
            for (Recensione recensione :listaRecensioni)
                listaRecensioniTabella.add(new RecensioneTableView(recensione.getNomeStruttura(), recensione.getNickname(), recensione.getIndirizzo(), recensione.getNomeCitta()));
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
        if (checkInternetConnection()) {
            idRecensione = getIdRecensioneFromListaByIndirizzoAndNickname(indirizzo,nickname);
            recensioneDAO.deleteReviewFromDatabase(String.valueOf(idRecensione));
            result = true;
        }
        return result;
    }

    public ObservableList<RecensioneTableView> deleteReviewFromTableViewList(String indirizzo, String nickname, ObservableList<RecensioneTableView> listaRecensioniTableView) {
        Iterator<RecensioneTableView> iterator = listaRecensioniTableView.iterator();
        RecensioneTableView recensioneTableView;
        int trovataRecensione = 0;
        while (iterator.hasNext() && trovataRecensione == 0) {
            recensioneTableView = iterator.next();
            if (recensioneTableView.getIndirizzo().equals(indirizzo) && recensioneTableView.getNickname().equals(nickname)) {
                iterator.remove();
                trovataRecensione = 1;
            }
        }
        return listaRecensioniTableView;
    }

    public boolean acceptReview(String indirizzo, String nickname) {
        boolean result = false;
        int idRecensione = getIdRecensioneFromListaByIndirizzoAndNickname(indirizzo,nickname);

        if (checkInternetConnection()) {
            recensioneDAO.acceptReviewDatabase(String.valueOf(idRecensione));
            result = true;
        }
        return result;
    }

    public void openGestioneUtentiPage() {
        ratificaRecensioniPage.loadNextScreen("GestioneUtentiApp.fxml");
    }

    public void openGestioneRecensioniPage() {
        ratificaRecensioniPage.loadNextScreen("GestioneRecensioniApp.fxml");
    }

    private boolean checkInternetConnection() {
        boolean result = false;
        try {
            URL url = new URL("https://aws.amazon.com/it/");
            URLConnection connection = url.openConnection();
            connection.connect();
            result = true;
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected");
        } catch (IOException e) {
            System.out.println("Internet is not connected");
        }

        return result;
    }
}
