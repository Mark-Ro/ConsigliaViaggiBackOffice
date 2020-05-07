package GUI;

import Controller.LoginController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginPage implements Initializable{
    
    private LoginController loginController;
    
    private double xOffset;
    private double yOffset;
    
    @FXML
    private AnchorPane stageLogin;
    @FXML 
    private ImageView idIconClose;
    @FXML
    private Button btnAccediLogin;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    
    @FXML
    private void handleIconCloseClicked(MouseEvent evt){
        Stage stage = (Stage) idIconClose.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleButtonClickAccedi (ActionEvent evt) throws IOException {
        btnAccediLogin.setDisable(true);
        doAccess();

    }

    @FXML
    private void handleEnterKeyPressed(KeyEvent evt){
        if(evt.getCode() == KeyCode.ENTER)
            btnAccediLogin.fire();
    }

    private void doAccess(){
        loginController = new LoginController(this);
        loginController.effettuaLogin(textFieldUsername.getText(), textFieldPassword.getText());
    }

    public void loadNextScreen(String nextScreen){
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(1000));
        fadeTrans.setNode(stageLogin);
        fadeTrans.setFromValue(1);
        fadeTrans.setToValue(0);

            fadeTrans.setOnFinished((ActionEvent t) -> {
                Parent secondView = null;
                try {
                    secondView=(AnchorPane)FXMLLoader.load(getClass().getResource(nextScreen));
                    Scene newScene= new Scene (secondView);
                    Stage curStage = (Stage) stageLogin.getScene().getWindow();
                    curStage.setScene(newScene);
                    curStage.centerOnScreen();
                    newScene.setOnMousePressed((MouseEvent event) -> {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    });
                    newScene.setOnMouseDragged((MouseEvent event) -> {
                        curStage.setX(event.getScreenX() - xOffset);
                        curStage.setY(event.getScreenY() - yOffset);
                    });
                } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            });       
            fadeTrans.play();
    }
        
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(1000));
        fadeTrans.setNode(stageLogin);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }

    public void resetGraphics() {
        btnAccediLogin.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageLogin.setOpacity(0);
        makeFadeInTransition();
    }
}
