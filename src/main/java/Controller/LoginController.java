package Controller;

import GUI.LoginPage;

public class LoginController {
    private LoginPage loginPage;

    public LoginController(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void effettuaLogin(String username, String password) {
        if (username.equals("admin") && password.equals("admin"))
            openRatificaRecensioniPage();
        else
            loginPage.openDialogErroreLogin();
    }

    private void openRatificaRecensioniPage() {
        loginPage.loadNextScreen("RatificaRecensioni.fxml");
    }


}
