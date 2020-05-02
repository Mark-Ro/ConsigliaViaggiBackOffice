/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.GestioneRecensioniController;
import Controller.RatificaRecensioniController;
import Entity.Recensione;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML private ImageView idIconCercaGestioneRecensioni;
    @FXML private AnchorPane pnlGestioneRecensioni;
    @FXML private ImageView idIconClose;
    @FXML private Text idIconIconify;
    @FXML private Button idBtnEliminaRecensione, idBtnCercaGestioneRecensioni;
    @FXML private TextField textFieldNomeStrutturaRecensioni, textFieldNicknameRecensioni, textFieldCittaRecensioni,textFieldIndirizzoRecensioni;
    @FXML private TextArea textAreaRecensioneGestioneRecensioni;
    @FXML private TableView<RecensioneTableView> tableViewGestioneRecensioni;
    @FXML private TableColumn<RecensioneTableView, String> columnNomeStrutturaRecensione, columnNicknameRecensione, columnIndirizzoRecensione, columnCittaRecensione;

    private ObservableList<RecensioneTableView> listaRecensioni;
    private GestioneRecensioniController gestioneRecensioniController = new GestioneRecensioniController();

    @FXML private void handleBtnEliminaRecensioneClicked(ActionEvent evt){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Recensione");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler eliminare la recensione?");
        Optional<ButtonType> bottoneConfermaDialog = alert.showAndWait();
        if (bottoneConfermaDialog.get() == ButtonType.OK) {
            deleteReview();
            idBtnEliminaRecensione.setDisable(true);
            textAreaRecensioneGestioneRecensioni.setDisable(true);
        }
    }

    @FXML
    private void handleEnterKeyPressed(KeyEvent evt){
        if(evt.getCode() == KeyCode.ENTER)
            idBtnCercaGestioneRecensioni.fire();
    }

    @FXML private void handleBtnCercaGestioneRecensioniClicked(ActionEvent evt) {
        doResearch();
    }

    @FXML private void handleIconCercaGestioneRecensioniClicked(MouseEvent evt) {
        doResearch();
    }

    private void setTextFields() {
        if (tableViewGestioneRecensioni.getSelectionModel().getSelectedItem() != null) {
            Recensione reviewSelected = gestioneRecensioniController.getRecensioneFromListaByIndirizzoAndNickname(tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getNickname());
            textAreaRecensioneGestioneRecensioni.setDisable(false);
            textAreaRecensioneGestioneRecensioni.setText(reviewSelected.getTestoRecensione());
        }
    }

    @FXML
    private void handleSelectedRowGestioneRecensioni(MouseEvent evt) {
        if (evt.getClickCount() > 0 && tableViewGestioneRecensioni.getSelectionModel().getSelectedItem() != null) {
            setTextFields();
            idBtnEliminaRecensione.setDisable(false);
        }
    }
    
    @FXML
    private void handleIconCloseClicked(MouseEvent evt) {
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


    private void openLoadingDialog(Stage stage) {
        StackPane stackPane = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("LoadingDialog.fxml"));
        try {
            stackPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene sc = new Scene(stackPane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(sc);
        stage.show();
    }

    private void closeLoadingDialog(Stage stage) {
        stage.close();
    }

    private void showDialogInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDialogError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void updateTableView(boolean operationComplete) {
        if (operationComplete == true) {
            System.out.println("Finito!");
            listaRecensioni = gestioneRecensioniController.getListaRecensioniTableGestioneRecensioni();
            if (listaRecensioni != null && listaRecensioni.size() > 0) {
                columnNomeStrutturaRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("NomeStruttura"));
                columnNicknameRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Nickname"));
                columnIndirizzoRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Indirizzo"));
                columnCittaRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Citta"));
                tableViewGestioneRecensioni.setItems(listaRecensioni);
            } else if (listaRecensioni != null && listaRecensioni.size() == 0) {
                showDialogInformation("Risultato Ricerca", "La ricerca non ha prodotto risultati!");
                tableViewGestioneRecensioni.setItems(null);
            }
        } else
            showDialogError("Errore di connessione", "Connessione Internet non disponibile!");
    }

    private void doResearch() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = gestioneRecensioniController.queryListaRecensioniFromDatabase(textFieldNomeStrutturaRecensioni.getText(),textFieldNicknameRecensioni.getText(),textFieldCittaRecensioni.getText(),textFieldIndirizzoRecensioni.getText());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                closeLoadingDialog(stage);
                updateTableView(operationComplete);
            }
        }).start();
    }

    private void deleteReview() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = gestioneRecensioniController.deleteReview(tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getNickname());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                closeLoadingDialog(stage);
                if (operationComplete == true) {
                    showDialogInformation("Eliminazione recensione", "Recensione eliminata con successo!");
                    updateTableViewAfterDeletes();
                } else
                    showDialogError("Errore di connessione", "Connessione Internet non disponibile!");
            }
        }).start();
    }

    private void updateTableViewAfterDeletes() {

        listaRecensioni = gestioneRecensioniController.deleteReviewFromTableViewList(tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewGestioneRecensioni.getSelectionModel().getSelectedItem().getNickname(),listaRecensioni);
        columnNomeStrutturaRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("NomeStruttura"));
        columnNicknameRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Nickname"));
        columnIndirizzoRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Indirizzo"));
        columnCittaRecensione.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Citta"));
        tableViewGestioneRecensioni.setItems(listaRecensioni);
        resetTextViews();
    }

    private void resetTextViews() {
        textAreaRecensioneGestioneRecensioni.setText("");
    }





}
