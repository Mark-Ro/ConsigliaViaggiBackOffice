
package DAO;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AWSLambdaSettings {
    private final String userPoolId = "eu-central-1_zOS8labm0";
    private final String AWS_ACCESS_KEY_ID = "AKIAJYPRFLV3MFY3NPIQ";
    private final String AWS_SECRET_ACCESS_KEY = "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";
    
    private static AWSLambdaSettings istance=null;
    
    public static AWSLambdaSettings getIstance() {
        if(istance==null)
            istance = new AWSLambdaSettings();
        return istance;
    }
    
    public String getAWS_ACCESS_KEY_ID() {
        return AWS_ACCESS_KEY_ID;
    }

    public String getAWS_SECRET_ACCESS_KEY() {
        return AWS_SECRET_ACCESS_KEY;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public boolean checkInternetConnection() {
        boolean result = false;
        try {
            URL url = new URL("https://aws.amazon.com/it/");
            URLConnection connection = url.openConnection();
            connection.connect();
            result = true;
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected");
        } catch (IOException e) {
            System.out.println("Internet is not connected");
        }

        if (result==false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore Connessione Internet");
            alert.setContentText("Connessione Internet non disponibile!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        return result;
    }
}
