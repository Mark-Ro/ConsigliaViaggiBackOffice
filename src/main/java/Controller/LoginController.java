package Controller;

import Cognito.LoginCognito;
import Entity.ProfiloAdmin;
import GUI.LoginPage;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import javafx.scene.control.Alert;

import java.lang.reflect.InvocationTargetException;

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
        try {
            loginCognito.doLoginAdmin(username, password);
            profiloAdmin.setUsername(username);
            openRatificaRecensioniPage();
            result = true;
            } catch (InvocationTargetException invocationTargetException) {
                showDialogError("Errore login", "Credenziali errate!");
            } catch (NotAuthorizedException notAuthorizedException) {
                showDialogError("Errore login", "Credenziali errate!");
        }
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
}
