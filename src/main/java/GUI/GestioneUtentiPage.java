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
import Controller.GestioneUtentiController;
import Entity.Utente;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
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
public class GestioneUtentiPage implements Initializable {
    
    private double xOffset,yOffset;
    
    @FXML private ComboBox comboBoxStato;
    @FXML private AnchorPane stageUtenti;
    @FXML private ImageView gestioneRecensioniIcon,gestioneUtentiIcon,ratificaRecensioniIcon, ricercaIcon;
    @FXML private Text gestioneRecensioniText, gestioneUtentiText,ratificaRecensioniText;
    @FXML private AnchorPane pnlGestioneUsers;
    @FXML private ImageView idIconClose;
    @FXML private Text idIconIconify;
    @FXML private Button idBtnSalvaModifiche,idBtnEliminaUtente,btnCercaGestioneUtenti;
    @FXML private TextField textFieldNicknameRicerca,textFieldNomeRicerca,textFieldCognomeRicerca,textFieldEmailRicerca;
    @FXML private TextField textFieldNicknameDati, textFieldNomeDati, textFieldCognomeDati, textFieldEmailDati,textFieldNomePubblico,textFieldNumeroRecensioniStatistiche,textFieldMediaValutazioniStatistiche;
    @FXML private TableView<UtenteTableView> tableViewGestioneUtenti;
    @FXML private TableColumn<UtenteTableView, String> columnNickname,columnNome,columnCognome,columnEmail;

    private GestioneUtentiController gestioneUtentiController = new GestioneUtentiController(this);
    private ObservableList<UtenteTableView> listaUtenti;

    @FXML private void handleButtonCercaUtentiClicked(MouseEvent evt) {
        doResearch();
    }

    @FXML private void handleSearchImageClicked(MouseEvent evt){
        doResearch();
    }

    @FXML private void handleSelectedRow(MouseEvent evt){
        if (evt.getClickCount()>0 && tableViewGestioneUtenti.getSelectionModel().getSelectedItem() != null) {
            setTextFields();
            idBtnSalvaModifiche.setDisable(false);
            idBtnEliminaUtente.setDisable(false);
        }
    }

    @FXML private void handleButtonSalvaModificheClicked(MouseEvent evt){
        if (textFieldNomeDati.getText().isEmpty() || textFieldCognomeDati.getText().isEmpty() || textFieldEmailDati.getText().isEmpty() || textFieldNomePubblico.getText().isEmpty())
            showDialogError("Errore Salva Modifiche","Riempire i campi!");
        else if (!textFieldEmailDati.getText().contains("@") || !textFieldEmailDati.getText().contains("."))
            showDialogError("Errore Salva Modifiche","Inserire una mail valida!");
        else
            updateUtenteAttributes();
    }
    
    @FXML private void handleButtonEliminaUtenteClicked(MouseEvent evt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Utente");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler eliminare l'utente?? ");
        Optional<ButtonType> bottoneConfermaDialog = alert.showAndWait();
        if (bottoneConfermaDialog.get() == ButtonType.OK)
            deleteUser();
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
    
    @FXML private void handleGestioneRecensioniIconClicked(MouseEvent evt){
        gestioneUtentiController.openGestioneRecensioniPage();
    }
    
    @FXML private void handleRatificaRecensioniIconClicked(MouseEvent evt){
        gestioneUtentiController.openRatificaRecensioniPage();
    }
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageUtenti);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }
    
    public void loadNextScreen(String fxml){
    FadeTransition fadeTrans = new FadeTransition();
    fadeTrans.setDuration(javafx.util.Duration.millis(500));
    fadeTrans.setNode(stageUtenti);
    fadeTrans.setFromValue(1);
    fadeTrans.setToValue(0);

    fadeTrans.setOnFinished((ActionEvent t) -> {
        Parent secondView = null;
        try {
            secondView =(AnchorPane)FXMLLoader.load(getClass().getResource(fxml));
        }catch (IOException ex) {}

        Scene newScene= new Scene (secondView);

        Stage curStage = (Stage) stageUtenti.getScene().getWindow();

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
    });       
    fadeTrans.play();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageUtenti.setOpacity(0);
        makeFadeInTransition();
        pnlGestioneUsers.setStyle("-fx-background-color: #08202D");
        ObservableList<String> statiUtente = FXCollections.observableArrayList("unbanned","banned");
        comboBoxStato.setItems(statiUtente);
    }

    private void setTextFields() {
        if (tableViewGestioneUtenti.getSelectionModel().getSelectedItem() != null) {

            UtenteTableView selectedUser = tableViewGestioneUtenti.getSelectionModel().getSelectedItem();
            Utente utenteSelezionato = gestioneUtentiController.getUtenteFromListaUtentiByNickname(selectedUser.getNickname());
            textFieldNicknameDati.setText(utenteSelezionato.getNickname());
            textFieldNomeDati.setText(utenteSelezionato.getNome());
            textFieldCognomeDati.setText(utenteSelezionato.getCognome());
            textFieldEmailDati.setText(utenteSelezionato.getEmail());
            textFieldNomePubblico.setText(utenteSelezionato.getNomePubblico());
            comboBoxStato.setValue(utenteSelezionato.getStato());
            textFieldNumeroRecensioniStatistiche.setText(String.valueOf(utenteSelezionato.getNumeroRecensioni()));
            textFieldMediaValutazioniStatistiche.setText(String.valueOf(utenteSelezionato.getMedia()));
        }
    }

    private void doResearch() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = gestioneUtentiController.queryListaUtenti(textFieldNicknameRicerca.getText(),textFieldNomeRicerca.getText(),textFieldCognomeRicerca.getText(),textFieldEmailRicerca.getText());
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

    private void updateUtenteAttributes() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private String resultOperation;

            @Override
            protected Object call() throws Exception {
                resultOperation = gestioneUtentiController.saveModifies(textFieldNomeDati.getText(),textFieldCognomeDati.getText(),textFieldNomePubblico.getText(),textFieldEmailDati.getText(),comboBoxStato.getSelectionModel().getSelectedItem().toString(),textFieldNicknameDati.getText());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                System.out.println("Entrato in onSucceded");
                closeLoadingDialog(stage);
                if (resultOperation.contains("Successfully")) {
                    showDialogInformation("Esito Modifiche", "L'utente è stato modificato con successo!");
                    updateTableViewAfterModifies();
                    gestioneUtentiController.queryListaUtenti(textFieldNicknameRicerca.getText(),textFieldNomeRicerca.getText(),textFieldCognomeRicerca.getText(),textFieldEmailRicerca.getText());
                }
                else if (resultOperation.contains("Email invalid!"))
                    showDialogError("Errore modifica","Email già utilizzata da un altro utente!");
                else if (resultOperation.contains("No connection!"))
                    showDialogError("Errore di connessione","Connessione Internet non disponibile!");
            }
        }).start();
    }

    private void deleteUser() {
        Stage stage = new Stage();
        openLoadingDialog(stage);
        new Thread(new Task<>() {

            private boolean operationComplete = false;

            @Override
            protected Object call() throws Exception {
                operationComplete = gestioneUtentiController.deleteUser(textFieldNicknameDati.getText());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                closeLoadingDialog(stage);
                if (operationComplete == true) {
                    showDialogInformation("Eliminazione utente","Utente eliminato con successo!");
                    updateTableViewAfterDeletes();
                }
                else
                    showDialogError("Errore di connessione","Connessione Internet non disponibile!");
            }
        }).start();
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
        textFieldNomeRicerca.setText("");
        textFieldCognomeRicerca.setText("");
        textFieldNicknameRicerca.setText("");
        textFieldEmailRicerca.setText("");
        textFieldNicknameDati.setText("");
        textFieldNomeDati.setText("");
        textFieldCognomeDati.setText("");
        textFieldEmailDati.setText("");
        textFieldNomePubblico.setText("");
        comboBoxStato.setValue("");
        textFieldNumeroRecensioniStatistiche.setText("");
        textFieldMediaValutazioniStatistiche.setText("");
    }

    private void updateTableViewAfterDeletes() {
        listaUtenti = gestioneUtentiController.deleteUserFromTableViewList(textFieldNicknameDati.getText(),listaUtenti);
        columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nickname"));
        columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nome"));
        columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Cognome"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Email"));
        tableViewGestioneUtenti.setItems(listaUtenti);
        resetTextViews();
    }

    private void updateTableViewAfterModifies() {
        listaUtenti = gestioneUtentiController.modifyUserInTableViewList(textFieldNicknameDati.getText(),textFieldNomeDati.getText(),textFieldCognomeDati.getText(),textFieldEmailDati.getText(),listaUtenti);
        columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nickname"));
        columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nome"));
        columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Cognome"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Email"));
        tableViewGestioneUtenti.setItems(listaUtenti);
        tableViewGestioneUtenti.refresh();
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

    private void updateTableView(boolean operationComplete) {
        if (operationComplete == true) {
            System.out.println("Finito!");
            listaUtenti = gestioneUtentiController.getListaUtentiTable();
            if (listaUtenti != null && listaUtenti.size() > 0) {
                columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Nickname"));
                columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Nome"));
                columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Cognome"));
                columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Email"));
                tableViewGestioneUtenti.setItems(listaUtenti);
            } else if (listaUtenti != null && listaUtenti.size() == 0) {
                showDialogInformation("Risultato Ricerca", "La ricerca non ha prodotto risultati!");
                tableViewGestioneUtenti.setItems(null);
            }
        }
        else
            showDialogError("Errore di connessione","Connessione Internet non disponibile!");
    }
}
