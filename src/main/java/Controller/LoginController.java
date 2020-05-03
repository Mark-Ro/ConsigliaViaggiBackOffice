package Controller;

import Cognito.LoginCognito;
import Entity.ProfiloAdmin;
import GUI.LoginPage;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;

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

    public void effettuaLogin(String username, String password) {
        try {
            loginCognito.doLoginAdmin(username, password);
            profiloAdmin.setUsername(username);
            openRatificaRecensioniPage();
        }
        catch (InvocationTargetException invocationTargetException) {
            loginPage.openDialogErroreLogin();
        }
        catch (NotAuthorizedException notAuthorizedException) {
            loginPage.openDialogErroreLogin();
        }
    }

    private void openRatificaRecensioniPage() {
        loginPage.loadNextScreen("RatificaRecensioni.fxml");
    }

}
