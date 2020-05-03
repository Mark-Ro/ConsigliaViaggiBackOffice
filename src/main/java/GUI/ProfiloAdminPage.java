/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.ProfiloAdmin;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mark9
 */
public class ProfiloAdminPage implements Initializable {

    private double xOffset,yOffset;
     
    @FXML private AnchorPane stageProfilo;
    @FXML private ImageView gestioneRecensioniIcon;
    @FXML private ImageView gestioneUtentiIcon;
    @FXML private ImageView ratificaRecensioniIcon;
    @FXML private AnchorPane pnlProfilo;
    @FXML private ImageView idIconClose;
    @FXML private Text idIconIconify;
    @FXML private TextField textFieldNicknameProfilo;

    private ProfiloAdmin profiloAdmin = ProfiloAdmin.getIstance();
    
    @FXML
    private void handleIconCloseClicked(MouseEvent evt){
        Stage stage = (Stage) idIconClose.getScene().getWindow();
        stage.close();
    }
    
    @FXML 
    private void handleIconIconifyClicked (MouseEvent evt){
        Stage stage = (Stage) idIconIconify.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML private void handleGestioneUtentiIconClicked(MouseEvent evt){
        makeFadeOut("GestioneUtentiApp.fxml");
    }
    
    @FXML private void handleRatificaRecensioniIconClicked(MouseEvent evt){
        makeFadeOut("RatificaRecensioni.fxml");
    }
    
    @FXML private void handleGestioneRecensioniIconClicked(MouseEvent evt){
        makeFadeOut("GestioneRecensioniApp.fxml");
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageProfilo);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }
    
    private void makeFadeOut(String fxml){
    FadeTransition fadeTrans = new FadeTransition();
    fadeTrans.setDuration(javafx.util.Duration.millis(500));
    fadeTrans.setNode(stageProfilo);
    fadeTrans.setFromValue(1);
    fadeTrans.setToValue(0);

    fadeTrans.setOnFinished((ActionEvent t) -> {
        loadNextScreen(fxml);
    });       
    fadeTrans.play();
    }
    
   private void loadNextScreen(String fxml){
            
        Parent secondView = null;
        try {
            secondView =(AnchorPane)FXMLLoader.load(getClass().getResource(fxml));
        }catch (IOException ex) {}

        Scene newScene= new Scene (secondView);

        Stage curStage = (Stage) stageProfilo.getScene().getWindow();

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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageProfilo.setOpacity(0);
        makeFadeInTransition();
        pnlProfilo.setStyle("-fx-background-color: #08202D");
        setTextViews();
    }

    private void setTextViews() {
        textFieldNicknameProfilo.setText(profiloAdmin.getUsername());
    }
}
