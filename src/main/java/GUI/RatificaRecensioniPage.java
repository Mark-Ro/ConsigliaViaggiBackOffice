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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author mark9
 */
public class RatificaRecensioniPage implements Initializable {

    private double xOffset, yOffset;

    @FXML
    private AnchorPane stageRatifica;
    @FXML
    private ImageView ratificaRecensioniIcon;
    @FXML
    private ImageView gestioneUtentiIcon;
    @FXML
    private ImageView gestioneRecensioniIcon;
    @FXML
    private AnchorPane pnlRatificaRec;
    @FXML
    private ImageView idIconClose;
    @FXML
    private Text idIconIconify;
    @FXML
    private Button idBtnRifiutaRecensioneRatifica, idBtnAccettaRecensioneRatifica, idBtnVisualizzaRecensioniRatifica;
    @FXML
    private TableView<RecensioneTableView> tableViewRatifica;
    @FXML
    private TableColumn<RecensioneTableView, String> columnNomeStruttura, columnNickname, columnIndirizzo, columnCitta;
    @FXML
    private TextField textFieldNomeStrutturaRatifica, textFieldNicknameRatifica;
    @FXML
    private TextArea textAreaTestoRecensioneRatifica;
    @FXML private Text gestioneRecensioniText, gestioneUtentiText,ratificaRecensioniText;

    private ObservableList<RecensioneTableView> listaRecensioni;
    private RatificaRecensioniController ratificaRecensioniController = new RatificaRecensioniController(this);


    @FXML
    private void handleBtnRifiutaRecensioneClicked(MouseEvent evt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Recensione");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler eliminare la recensione?");
        Optional<ButtonType> bottoneConfermaDialog = alert.showAndWait();
        if (bottoneConfermaDialog.get() == ButtonType.OK) {
            idBtnAccettaRecensioneRatifica.setDisable(true);
            idBtnRifiutaRecensioneRatifica.setDisable(true);
            textAreaTestoRecensioneRatifica.setDisable(true);
            deleteReview();
            idBtnAccettaRecensioneRatifica.setDisable(true);
            idBtnRifiutaRecensioneRatifica.setDisable(true);
            textAreaTestoRecensioneRatifica.setDisable(true);
        }
    }

    @FXML
    private void handleBtnAccettaRecensioneClicked(MouseEvent evt) {
        acceptNewReview();
        idBtnAccettaRecensioneRatifica.setDisable(true);
        idBtnRifiutaRecensioneRatifica.setDisable(true);
        textAreaTestoRecensioneRatifica.setDisable(true);
    }

    @FXML
    private void handleBtnVisualizzaRecensioniClicked(MouseEvent evt) {
            visualizeReviews();
    }

    @FXML
    private void handleSelectedRowRatifica(MouseEvent evt) {
        if (evt.getClickCount() > 0 && tableViewRatifica.getSelectionModel().getSelectedItem() != null) {
            setTextFields();
            idBtnAccettaRecensioneRatifica.setDisable(false);
            idBtnRifiutaRecensioneRatifica.setDisable(false);
            textAreaTestoRecensioneRatifica.setDisable(false);
        }
    }

    @FXML
    private void handleIconCloseClicked(MouseEvent evt) {
        Stage stage = (Stage) idIconClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleIconIconifyClicked(MouseEvent evt) {
        Stage stage = (Stage) idIconIconify.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleGestioneUtentiIconClicked(MouseEvent evt) {
        ratificaRecensioniController.openGestioneUtentiPage();
    }

    @FXML
    private void handleGestioneRecensioniIconClicked(MouseEvent evt) {
        ratificaRecensioniController.openGestioneRecensioniPage();
    }

    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageRatifica);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);
        fadeTrans.play();
    }

    public void loadNextScreen(String fxml) {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageRatifica);
        fadeTrans.setFromValue(1);
        fadeTrans.setToValue(0);

        fadeTrans.setOnFinished((ActionEvent t) -> {
            try {
                Parent secondView = (AnchorPane) FXMLLoader.load(getClass().getResource(fxml));
                Scene newScene = new Scene(secondView);
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
        });
        fadeTrans.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageRatifica.setOpacity(0);
        makeFadeInTransition();
        pnlRatificaRec.setStyle("-fx-background-color: #08202D");
    }

    private void visualizeReviews() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = ratificaRecensioniController.queryListaRecensioni();
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

    private void updateTableView(boolean operationComplete) {
        if (operationComplete == true) {
            System.out.println("Finito!");
            listaRecensioni = ratificaRecensioniController.getListaRecensioniTable();
            if (listaRecensioni != null && listaRecensioni.size() > 0) {
                columnNomeStruttura.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("NomeStruttura"));
                columnNickname.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Nickname"));
                columnIndirizzo.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Indirizzo"));
                columnCitta.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Citta"));
                tableViewRatifica.setItems(listaRecensioni);
            } else if (listaRecensioni != null && listaRecensioni.size() == 0)
                showDialogInformation("Risultato Ricerca", "La ricerca non ha prodotto risultati!");
        } else
            showDialogError("Errore di connessione", "Connessione Internet non disponibile!");
    }

    private void setTextFields() {
        if (tableViewRatifica.getSelectionModel().getSelectedItem() != null) {

            Recensione reviewSelected = ratificaRecensioniController.getRecensioneFromListaByIndirizzoAndNickname(tableViewRatifica.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewRatifica.getSelectionModel().getSelectedItem().getNickname());
            textFieldNomeStrutturaRatifica.setText(reviewSelected.getNomeStruttura());
            textFieldNicknameRatifica.setText(reviewSelected.getNickname());
            textAreaTestoRecensioneRatifica.setText(reviewSelected.getTestoRecensione());
        }
    }

    private void openLoadingDialog(Stage stage) {
        stage.initModality(Modality.APPLICATION_MODAL);
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

    private void resetTextViews() {
        textFieldNomeStrutturaRatifica.setText("");
        textFieldNicknameRatifica.setText("");
        textAreaTestoRecensioneRatifica.setText("");
    }

    private void updateTableViewAfterDeletes() {
        listaRecensioni = ratificaRecensioniController.deleteReviewFromTableViewList(tableViewRatifica.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewRatifica.getSelectionModel().getSelectedItem().getNickname(),listaRecensioni);
        columnNomeStruttura.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("NomeStruttura"));
        columnNickname.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Nickname"));
        columnIndirizzo.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Indirizzo"));
        columnCitta.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Citta"));
        tableViewRatifica.setItems(listaRecensioni);
        resetTextViews();
    }

    private void deleteReview() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = ratificaRecensioniController.deleteReview(tableViewRatifica.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewRatifica.getSelectionModel().getSelectedItem().getNickname());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                closeLoadingDialog(stage);
                if (operationComplete == true) {
                    showDialogInformation("Eliminazione recensione", "Recensione rifiutata con successo!");
                    updateTableViewAfterDeletes();
                } else
                    showDialogError("Errore di connessione", "Connessione Internet non disponibile!");
            }
        }).start();
    }

    private void updateTableViewAfterModifies() {
        listaRecensioni = ratificaRecensioniController.deleteReviewFromTableViewList(tableViewRatifica.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewRatifica.getSelectionModel().getSelectedItem().getNickname(),listaRecensioni);
        columnNomeStruttura.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("NomeStruttura"));
        columnNickname.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Nickname"));
        columnIndirizzo.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Indirizzo"));
        columnCitta.setCellValueFactory(new PropertyValueFactory<RecensioneTableView, String>("Citta"));
        tableViewRatifica.setItems(listaRecensioni);
        tableViewRatifica.refresh();
    }

    private void acceptNewReview() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = ratificaRecensioniController.acceptReview(tableViewRatifica.getSelectionModel().getSelectedItem().getIndirizzo(),tableViewRatifica.getSelectionModel().getSelectedItem().getNickname());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                System.out.println("Entrato in onSucceded");
                closeLoadingDialog(stage);
                if (operationComplete == true) {
                    showDialogInformation("Esito Ratifica", "La recensione è stata accettata!");
                    updateTableViewAfterModifies();
                    resetTextViews();
                }
                else
                    showDialogError("Errore di connessione","Connessione Internet non disponibile!");
            }
        }).start();
    }
}
