package Controller;

import Cognito.LoginCognito;
import Entity.ProfiloAdmin;
import GUI.LoginPage;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoginController {
    private LoginPage loginPage;
    private LoginCognito loginCognito;
    private ProfiloAdmin profiloAdmin;

    public LoginController(LoginPage loginPage) {
        this.loginPage = loginPage;
        this.loginCognito = new LoginCognito();
        this.profiloAdmin = ProfiloAdmin.getIstance();
    }

    public boolean effettuaLogin(String username, String password) {
        boolean result = false;
        if (checkInternetConnection()) {
            try {
                loginCognito.doLoginAdmin(username, password);
                profiloAdmin.setUsername(username);
                result = true;
                openRatificaRecensioniPage();
            } catch (InvocationTargetException invocationTargetException) {
                showDialogError("Errore login", "Credenziali errate!");
            } catch (NotAuthorizedException notAuthorizedException) {
                showDialogError("Errore login", "Credenziali errate!");
            }
        }
        else
            showDialogError("Errore login","Connessione Internet non disponibile!");
        return result;
    }

    private void showDialogError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void openRatificaRecensioniPage() {
        loginPage.loadNextScreen("RatificaRecensioni.fxml");
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
