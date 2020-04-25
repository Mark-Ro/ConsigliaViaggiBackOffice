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
public class GestioneRecensioniPage implements Initializable {

    private double xOffset,yOffset;
     
    @FXML private AnchorPane stageRecensioni;
    @FXML private ImageView gestioneRecensioniIcon;
    @FXML private ImageView gestioneUtentiIcon;
    @FXML private ImageView ratificaRecensioniIcon;
    @FXML private ImageView profiloAdminIcon;
    @FXML private AnchorPane pnlGestioneRecensioni;
    @FXML private ImageView idIconClose;
    @FXML private Text idIconIconify;
    @FXML private Button idBtnEliminaRecensione;

    @FXML private void handleBtnEliminaRecensioneClicked(ActionEvent evt){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Elimina Recensione");
        alert.setHeaderText("Elimina Recensione");
        alert.setContentText("sei sicuro di voler eliminare questa recensione?");
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
    
    @FXML private void handleRatifcaRecensioniIconClicked(MouseEvent evt){
        makeFadeOut("RatificaRecensioni.fxml");
    }
    
    @FXML private void handleProfiloAdminIconClicked(MouseEvent evt){
        makeFadeOut("ProfiloAdmin.fxml");
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageRecensioni);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }
    
    private void makeFadeOut(String fxml){
    FadeTransition fadeTrans = new FadeTransition();
    fadeTrans.setDuration(javafx.util.Duration.millis(500));
    fadeTrans.setNode(stageRecensioni);
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

        Stage curStage = (Stage) stageRecensioni.getScene().getWindow();

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
        stageRecensioni.setOpacity(0);
        makeFadeInTransition();
        pnlGestioneRecensioni.setStyle("-fx-background-color: #08202D");
    }
    
    
}
