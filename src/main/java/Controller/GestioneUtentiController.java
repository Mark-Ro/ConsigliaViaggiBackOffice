package Controller;

import Cognito.GestioneUtentiCognito;
import DAO.UtenteDAO;
import Entity.Utente;
import Exceptions.UserNotFoundException;
import GUI.GestioneUtentiPage;
import GUI.UtenteTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class GestioneUtentiController {

    private GestioneUtentiPage gestioneUtentiPage;
    private UtenteDAO utenteDAO;
    private GestioneUtentiCognito gestioneUtentiCognito;

    private ArrayList<Utente> listaUtenti;

    public GestioneUtentiController() {}

    public GestioneUtentiController(GestioneUtentiPage gestioneUtentiPage) {
        this.gestioneUtentiPage = gestioneUtentiPage;
        this.utenteDAO = new UtenteDAO();
        this.gestioneUtentiCognito = new GestioneUtentiCognito();
    }

    public boolean queryListaUtenti(String nickname, String nome, String cognome, String email) {
        boolean result = false;
        System.out.println("Entrato nella Query!");
        if (checkInternetConnection()) {
            listaUtenti = utenteDAO.getListaUtentiFromDatabase(nickname, nome, cognome, email);
            result = true;
        }
        System.out.println("Query finita!");
        return result;
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

    private boolean isEmailUnique(String nickname, String email) {
        int trovatoUtente = 0;
        boolean result = true;
        Iterator<Utente> iterator = listaUtenti.iterator();
        Utente utente;
        while (iterator.hasNext() && trovatoUtente == 0) {
            utente = iterator.next();
            if (!utente.getNickname().equals(nickname) && utente.getEmail().equals(email)) {
                result = false;
                trovatoUtente = 1;
            }
        }
        return result;
    }

    public String saveModifies(String nome, String cognome, String nomePubblico, String email, String stato, String nickname) {
        String result;
        if (checkInternetConnection()) {
            if (isEmailUnique(nickname,email)) {
                if (wasUserBanned(nickname,listaUtenti)) {
                    utenteDAO.saveModifiesIntoDatabase(nome, cognome, nomePubblico, email, stato, nickname);
                    if (stato.equals("unbanned")) {
                        gestioneUtentiCognito.unbanUtenteCognito(nickname);
                        gestioneUtentiCognito.saveModifiesIntoCognito(nickname, email);
                    } else {
                        gestioneUtentiCognito.unbanUtenteCognito(nickname);
                        gestioneUtentiCognito.saveModifiesIntoCognito(nickname, email);
                        gestioneUtentiCognito.banUtenteCognito(nickname);
                    }
                } else {
                    gestioneUtentiCognito.saveModifiesIntoCognito(nickname, email);
                    if (stato.equals("banned")) {
                        utenteDAO.banUtenteFromDatabase(nickname, email, nome, cognome, nomePubblico);
                        gestioneUtentiCognito.banUtenteCognito(nickname);
                    } else
                        utenteDAO.saveModifiesIntoDatabase(nome, cognome, nomePubblico, email, stato, nickname);
                }
                result = "Successfully";
            }
            else
                result = "Email invalid!";
        }
        else
            result = "No connection!";
        return result;
    }

    public boolean wasUserBanned(String nickname, ArrayList<Utente> listaUtenti) {

        if (nickname == null)
            throw new IllegalArgumentException("Nickname must be not null!");

        if (listaUtenti == null)
            throw new IllegalArgumentException("List must be not null!");

        for (Utente utente : listaUtenti)
            if (utente.getNickname().equals(nickname))
                return utente.getStato().equals("banned");

        throw new UserNotFoundException("User doesn't exist!");
    }

    public boolean deleteUser(String nickname) {
        boolean result = false;
        if (checkInternetConnection()) {
            utenteDAO.deleteUserFromDatabase(nickname);
            gestioneUtentiCognito.deleteUserFromCognito(nickname);
            result = true;
        }
        return result;
    }

    public void openRatificaRecensioniPage() {
        gestioneUtentiPage.loadNextScreen("RatificaRecensioni.fxml");
    }

    public void openGestioneRecensioniPage() {
        gestioneUtentiPage.loadNextScreen("GestioneRecensioniApp.fxml");
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
