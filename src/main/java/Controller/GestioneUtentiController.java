package Controller;

import DAO.AWSLambdaSettings;
import DAO.UtenteDAO;
import Entity.Utente;
import GUI.GestioneUtentiPage;
import GUI.UtenteTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;

public class GestioneUtentiController {

    private GestioneUtentiPage gestioneUtentiPage;

    private ArrayList<Utente> listaUtenti;
    private UtenteDAO utenteDAO = new UtenteDAO();
    private AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();

    public GestioneUtentiController(GestioneUtentiPage gestioneUtentiPage) {
        this.gestioneUtentiPage = gestioneUtentiPage;
    }

    public ArrayList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public void queryListaUtentiFromDatabase(String nickname, String nome, String cognome, String email) {
        if (awsLambdaSettings.checkInternetConnection())
            listaUtenti = utenteDAO.getListaUtentiFromDatabase(nickname,nome,cognome,email);
    }

    public ObservableList<UtenteTableView> getListaUtentiTable() {
        Utente utente;
        ObservableList<UtenteTableView> listaUtentiTabella = null;
        if (listaUtenti!=null) {
            listaUtentiTabella = FXCollections.observableArrayList();
            Iterator<Utente> iterator = listaUtenti.iterator();
            while (iterator.hasNext()) {
                utente = iterator.next();
                listaUtentiTabella.add(new UtenteTableView(utente.getNickname(), utente.getNome(), utente.getCognome(), utente.getEmail()));
            }
        }
        return listaUtentiTabella;
    }

    public Utente getUtenteFromListaUtentiByNickname(String nickname) {
        int trovatoUtente = 0;
        Iterator<Utente> iterator = listaUtenti.iterator();
        Utente utente = null;
        while (iterator.hasNext() && trovatoUtente == 0) {
            utente = iterator.next();
            if (utente.getNickname().equals(nickname))
                trovatoUtente = 1;
        }
        return utente;
    }

    public ObservableList<UtenteTableView> deleteUserFromTableViewList(String nickname, ObservableList<UtenteTableView> listaUtentiTableView) {
        Iterator<UtenteTableView> iterator = listaUtentiTableView.iterator();
        UtenteTableView utenteTableView;
        int trovatoUtente = 0;
        while (iterator.hasNext() && trovatoUtente == 0) {
            utenteTableView = iterator.next();
            if (utenteTableView.getNickname().equals(nickname)) {
                iterator.remove();
                trovatoUtente = 1;
            }
        }
        return listaUtentiTableView;
    }

    public ObservableList<UtenteTableView> modifyUserInTableViewList(String nickname, String nome, String cognome, String email, ObservableList<UtenteTableView> listaUtenti) {
        Iterator<UtenteTableView> iterator = listaUtenti.iterator();
        UtenteTableView utenteTableView;
        int trovatoUtente = 0;
        while (iterator.hasNext() && trovatoUtente == 0) {
            utenteTableView = iterator.next();
            if (utenteTableView.getNickname().equals(nickname)) {
                utenteTableView.setNome(nome);
                utenteTableView.setCognome(cognome);
                utenteTableView.setEmail(email);
                trovatoUtente = 1;
            }
        }
        return listaUtenti;
    }

    public void saveModifies(String nome, String cognome, String nomePubblico, String email, String stato, String nickname) {
        if (awsLambdaSettings.checkInternetConnection()) {
            utenteDAO.saveModifiesIntoDatabase(nome, cognome, nomePubblico, email, stato, nickname);
            utenteDAO.saveModifiesIntoCognito(nickname, email);
            if (stato.equals("banned"))
                utenteDAO.banUtenteCognito(nickname);
            gestioneUtentiPage.showDialogInformation("Esito Modifiche","L'utente è stato modificato con successo!");
            gestioneUtentiPage.updateTableViewAfterModifies();
        }
    }

    public void deleteUser(String nickname) {
        if (awsLambdaSettings.checkInternetConnection()) {
            utenteDAO.deleteUserFromDatabase(nickname);
            utenteDAO.deleteUserFromCognito(nickname);
            gestioneUtentiPage.showDialogInformation("Eliminazione utente","Utente eliminato con successo!");
            gestioneUtentiPage.updateTableViewAfterDeletes();
        }
    }
}
