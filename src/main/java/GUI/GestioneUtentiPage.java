/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.GestioneUtentiController;
import Entity.Utente;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author mark9
 */
public class GestioneUtentiPage implements Initializable {
    
    private double xOffset,yOffset;
    
    @FXML private ComboBox comboBoxStato;
    @FXML private AnchorPane stageUtenti;
    @FXML private ImageView gestioneRecensioniIcon,gestioneUtentiIcon,ratificaRecensioniIcon, profiloAdminIcon, ricercaIcon;
    @FXML private Text gestioneRecensioniText, gestioneUtentiText,ratificaRecensioniText,profiloAdminText;
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


    @FXML private void handleButtonCercaUtentiClicked(ActionEvent evt) {
        doResearch();
    }

    @FXML private void handleSerachImageClicked(MouseEvent evt){
        doResearch();
    }


    private void doResearch(){
        gestioneUtentiController.queryListaUtentiFromDatabase(textFieldNicknameRicerca.getText(),textFieldNomeRicerca.getText(),textFieldCognomeRicerca.getText(),textFieldEmailRicerca.getText());
        listaUtenti = gestioneUtentiController.getListaUtentiTable();
        if (listaUtenti!=null && listaUtenti.size()>0) {
            columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Nickname"));
            columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Nome"));
            columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Cognome"));
            columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView, String>("Email"));
            tableViewGestioneUtenti.setItems(listaUtenti);
        }
        else if (listaUtenti!=null && listaUtenti.size() == 0)
            showDialogInformation("Risultato Ricerca","La ricerca non ha prodotto risultati!");
    }

    @FXML private void handleSelectedRow(MouseEvent evt){
        if (evt.getClickCount()>0) {
            setTextFields();
            idBtnSalvaModifiche.setDisable(false);
            idBtnEliminaUtente.setDisable(false);
        }
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

    @FXML private void handleButtonSalvaModificheClicked(ActionEvent evt){
        if (textFieldNomeDati.getText().isEmpty() || textFieldCognomeDati.getText().isEmpty() || textFieldEmailDati.getText().isEmpty() || textFieldNomePubblico.getText().isEmpty())
            showDialogError("Errore Salva Modifiche","Riempire i campi!");
        else
            gestioneUtentiController.saveModifies(textFieldNomeDati.getText(),textFieldCognomeDati.getText(),textFieldNomePubblico.getText(),textFieldEmailDati.getText(),comboBoxStato.getSelectionModel().getSelectedItem().toString(),textFieldNicknameDati.getText());
    }
    
    @FXML private void handleButtonEliminaUtenteClicked(ActionEvent evt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina Utente");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler eliminare l'utente?? ");
        Optional<ButtonType> bottoneConfermaDialog = alert.showAndWait();
        if (bottoneConfermaDialog.get() == ButtonType.OK)
            gestioneUtentiController.deleteUser(textFieldNicknameDati.getText());
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
        makeFadeOut("GestioneRecensioniApp.fxml");
    }
    
    @FXML private void handleRatificaRecensioniIconClicked(MouseEvent evt){
        makeFadeOut("RatificaRecensioni.fxml");
    }
    
    @FXML private void handleProfiloAdminIconClicked(MouseEvent evt){
        makeFadeOut("ProfiloAdmin.fxml");
    }
    
    private void makeFadeInTransition() {
        FadeTransition fadeTrans = new FadeTransition();
        fadeTrans.setDuration(javafx.util.Duration.millis(500));
        fadeTrans.setNode(stageUtenti);
        fadeTrans.setFromValue(0);
        fadeTrans.setToValue(1);     
        fadeTrans.play();    
    }
    
    private void makeFadeOut(String fxml){
    FadeTransition fadeTrans = new FadeTransition();
    fadeTrans.setDuration(javafx.util.Duration.millis(500));
    fadeTrans.setNode(stageUtenti);
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
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageUtenti.setOpacity(0);
        makeFadeInTransition();
        pnlGestioneUsers.setStyle("-fx-background-color: #08202D");
        ObservableList<String> statiUtente = FXCollections.observableArrayList("unbanned","banned");
        comboBoxStato.setItems(statiUtente);
    }

    public void showDialogInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showDialogError(String title, String message) {
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

    public void updateTableViewAfterDeletes() {
        listaUtenti = gestioneUtentiController.deleteUserFromTableViewList(textFieldNicknameDati.getText(),listaUtenti);
        columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nickname"));
        columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nome"));
        columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Cognome"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Email"));
        tableViewGestioneUtenti.setItems(listaUtenti);
        resetTextViews();
    }

    public void updateTableViewAfterModifies() {
        listaUtenti = gestioneUtentiController.modifyUserInTableViewList(textFieldNicknameDati.getText(),textFieldNomeDati.getText(),textFieldCognomeDati.getText(),textFieldEmailDati.getText(),listaUtenti);
        columnNickname.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nickname"));
        columnNome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Nome"));
        columnCognome.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Cognome"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<UtenteTableView,String>("Email"));
        tableViewGestioneUtenti.setItems(listaUtenti);
        tableViewGestioneUtenti.refresh();
    }
    
}
