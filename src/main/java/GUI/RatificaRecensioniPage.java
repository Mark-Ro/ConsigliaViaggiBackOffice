/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mark9
 */
public class RatificaRecensioniPage implements Initializable {

    private double xOffset,yOffset;
     
    @FXML private AnchorPane stageRatifica;
    @FXML private ImageView ratificaRecensioniIcon;  
    @FXML private ImageView gestioneUtentiIcon;
    @FXML private ImageView gestioneRecensioniIcon;
    @FXML private ImageView profiloAdminIcon;
    @FXML private AnchorPane pnlRatificaRec;
    @FXML private ImageView idIconClose;
    @FXML private Text idIconIconify;
    @FXML private Button idBtnRifiutaRecensione;
    @FXML private Button idBtnAccettaRecensione;


    @FXML private void handleBtnRifiutaRecensioneClicked(ActionEvent evt){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rifiuta Recensione");
        alert.setHeaderText("Rifiuta Recensione");
        alert.setContentText("sei sicuro di voler rifiutare questa recensione?");
        alert.showAndWait();
    }

    @FXML private void handleBtnAccettaRecensioneClicked(ActionEvent evt){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Accetta Recensione");
        alert.setHeaderText("Accetta Recensione");
        alert.setContentText("La recensione è stata pubblicata ");
        alert.showAndWait();

    }
    
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
    
    @FXML private void handleGestioneRecensioniIconClicked(MouseEvent evt){
        makeFadeOut("GestioneRecensioniApp.fxml");
    }
    
    @FXML private void handleProfiloAdminIconClicked(MouseEvent evt){
        makeFadeOut("ProfiloAdmin.fxml");
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageRatifica);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }
    
    private void makeFadeOut(String fxml){
    FadeTransition fadeTrans = new FadeTransition();
    fadeTrans.setDuration(javafx.util.Duration.millis(500));
    fadeTrans.setNode(stageRatifica);
    fadeTrans.setFromValue(1);
    fadeTrans.setToValue(0);

    fadeTrans.setOnFinished((ActionEvent t) -> {
        loadNextScreen(fxml);
    });       
    fadeTrans.play();
    }
    
    private void loadNextScreen(String fxml){
        try {
            Parent secondView =(AnchorPane)FXMLLoader.load(getClass().getResource(fxml));
            Scene newScene= new Scene (secondView);
            Stage curStage = (Stage) stageRatifica.getScene().getWindow();
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
            ex.printStackTrace();
        }
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageRatifica.setOpacity(0);
        makeFadeInTransition();
        pnlRatificaRec.setStyle("-fx-background-color: #08202D");
    }
    
    
}
