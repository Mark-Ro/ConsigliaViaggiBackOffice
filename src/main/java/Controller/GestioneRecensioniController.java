package Controller;

import DAO.AWSLambdaSettings;
import DAO.RecensioneDAO;
import Entity.Recensione;
import GUI.GestioneRecensioniPage;
import GUI.RecensioneTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;

public class GestioneRecensioniController {

    private GestioneRecensioniPage gestioneRecensioniPage;
    private AWSLambdaSettings awsLambdaSettings;
    private RecensioneDAO recensioneDAO;

    private ArrayList<Recensione> listaRecensioni;

    public GestioneRecensioniController(GestioneRecensioniPage gestioneRecensioniPage) {
        this.gestioneRecensioniPage = gestioneRecensioniPage;
        this.awsLambdaSettings = AWSLambdaSettings.getIstance();
        this.recensioneDAO = new RecensioneDAO();
    }

    public boolean queryListaRecensioniFromDatabase(String nomeStruttura, String nickname, String citta, String indirizzo) {
        boolean result = false;
        System.out.println("Entrato nella Query!");
        if (awsLambdaSettings.checkInternetConnection()) {
            listaRecensioni = recensioneDAO.getListaRecensioniForGestioneFromDatabase(nomeStruttura,nickname,citta,indirizzo);
            result = true;
        }
        System.out.println("Query finita!");
        return result;
    }

    public ObservableList<RecensioneTableView> getListaRecensioniTableGestioneRecensioni() {
        Recensione recensione;
        ObservableList<RecensioneTableView> listaRecensioniTabella = null;
        if (listaRecensioni!=null) {
            listaRecensioniTabella = FXCollections.observableArrayList();
            Iterator<Recensione> iterator = listaRecensioni.iterator();
            while (iterator.hasNext()) {
                recensione = iterator.next();
                listaRecensioniTabella.add(new RecensioneTableView(recensione.getNomeStruttura(),recensione.getNickname(),recensione.getIndirizzo(),recensione.getNomeCitta()));
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

    public void openRatificaRecensioniPage() {
        gestioneRecensioniPage.loadingNextScreen("RatificaRecensioni.fxml");
    }

    public void openGestioneUtentiPage() {
        gestioneRecensioniPage.loadingNextScreen("GestioneUtentiApp.fxml");
    }

    public void openProfiloPage() {
        gestioneRecensioniPage.loadingNextScreen("ProfiloAdmin.fxml");
    }

}
