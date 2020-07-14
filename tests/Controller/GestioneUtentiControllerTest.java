package Controller;

import Entity.Utente;
import Exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GestioneUtentiControllerTest {

    private GestioneUtentiController gestioneUtentiController;

    @Before
    public void setUp() {
        gestioneUtentiController = new GestioneUtentiController();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedUsernameNullAndListNull() {
        String nickname = null;
        ArrayList<Utente> listaUtenti = null;
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedUsernameNullListEmpty() {
        String nickname = null;
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedUsernameNullListNotEmpty() {
        String nickname = null;
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedUsernameEmptyListNull() {
        String nickname = "";
        ArrayList<Utente> listaUtenti = null;
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = UserNotFoundException.class)
    public void testWasUserBannedUsernameEmptyListEmpty() {
        String nickname = "";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = UserNotFoundException.class)
    public void testWasUserBannedUsernameEmptyListNotEmpty() {
        String nickname = "";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedUsernameNotEmptyListNull() {
        String nickname = "User";
        ArrayList<Utente> listaUtenti = null;
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = UserNotFoundException.class)
    public void testWasUserBannedUsernameNotEmptyListEmpty() {
        String nickname = "User";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test
    public void testWasUserBannedUsernameNotEmptyListNotEmptyWithMatchBanned() {
        String nickname = "marco";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        boolean expected = true,output;
        output = gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
        assertEquals(expected,output);
    }

    @Test
    public void testWasUserBannedUsernameNotEmptyListNotEmptyWithMatchUnbanned() {
        String nickname = "marco";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","unbanned","marco romano",0.0f,0));
        boolean expected = false,output;
        output = gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
        assertEquals(expected,output);
    }

    @Test (expected = UserNotFoundException.class)
    public void testWasUserBannedUsernameNotEmptyListNotEmptyWithNoMatch() {
        String nickname = "Massi";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    //Branch coverage

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedPath_2_3() {
        String nickname = null;
        ArrayList<Utente> listaUtenti = null;
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWasUserBannedPath_2_4_5() {
        String nickname = "Matteo";
        ArrayList<Utente> listaUtenti = null;
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test (expected = UserNotFoundException.class)
    public void testWasUserBannedPath_2_4_6_9() {
        String nickname = "Matteo";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
    }

    @Test
    public void testWasUserBannedPath_2_4_6_7_8() {
        boolean output;
        String nickname = "marco";
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente("marco","emailprova@email.it","marco","romano","banned","marco romano",0.0f,0));
        output = gestioneUtentiController.wasUserBanned(nickname,listaUtenti);
        assertEquals(output,true);
    }
}