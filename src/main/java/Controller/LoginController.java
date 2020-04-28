package Controller;

import Cognito.LoginCognito;
import GUI.LoginPage;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;

import java.lang.reflect.InvocationTargetException;

public class LoginController {
    private LoginPage loginPage;
    private LoginCognito loginCognito;

    public LoginController(LoginPage loginPage) {
        this.loginPage = loginPage;
        this.loginCognito = new LoginCognito();
    }

    public void effettuaLogin(String username, String password) {
        try {
            loginCognito.doLoginAdmin(username, password);
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
