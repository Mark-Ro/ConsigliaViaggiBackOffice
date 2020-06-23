package DAO;

import Entity.Utente;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.InvokeRequest;


import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UtenteDAO {

    public ArrayList<Utente> getListaUtentiFromDatabase(String nickname, String nome, String cognome, String email) {
        if (nickname.isEmpty() || nickname.isBlank())
            nickname = "null";
        if (nome.isEmpty() || nome.isBlank())
            nome = "null";
        if (cognome.isEmpty() || cognome.isBlank())
            cognome = "null";
        if (email.isEmpty() || email.isBlank())
            email = "null";

        String resultJson = doQuery(nickname,nome,cognome,email);
        ArrayList<Utente> listaUtenti = analizzaQuery(resultJson);
        return listaUtenti;
    }
    
    private String doQuery(String nickname, String nome, String cognome, String email) {
        String functionName = "funzioneLambdaQueryUtenteBackOffice";

        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"nickname\": \"" + nickname + "\",\"nome\": \""+nome+"\",\"cognome\": \""+cognome+"\",\"email\": \""+email+"\"\n }");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
        System.out.println(resultJSON);
        return resultJSON;
    }

    private ArrayList<Utente> analizzaQuery(String resultJSON) {
        String messageID,query;
        ArrayList<Utente> listaUtenti = new ArrayList<>();
        Object objOutputLambda = null;

        try {
            objOutputLambda = new JSONParser().parse(resultJSON);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        JSONObject jsonOutputLambda = (JSONObject) objOutputLambda;
        messageID=(String)jsonOutputLambda.get("messageID");

        if (messageID!=null && messageID.equals("1")) {
            query = (String) jsonOutputLambda.get("resultQuery");
            Object objQuery = null;
            try {
                objQuery = new JSONParser().parse(query);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
            JSONObject jsonQuery = (JSONObject) objQuery;

            boolean flag = false;
            int i = 1;
            String nickname, email, nome, cognome, stato, media, numeroRecensioni, nomePubblico;
            if (jsonQuery != null) {
                while (flag == false) {
                    nickname = (String) jsonQuery.get("Nickname" + i);
                    email = (String) jsonQuery.get("Email" + i);
                    nome = (String) jsonQuery.get("Nome" + i);
                    cognome = (String) jsonQuery.get("Cognome" + i);
                    stato = (String) jsonQuery.get("Stato" + i);
                    media = (String) jsonQuery.get("Media" + i);
                    numeroRecensioni = (String) jsonQuery.get("NumeroRecensioni" + i);
                    nomePubblico = (String) jsonQuery.get("NomePubblico" + i);

                    if (nickname != null) {
                        listaUtenti.add(new Utente(nickname, email, nome, cognome, stato, nomePubblico, Float.parseFloat(media), Integer.parseInt(numeroRecensioni)));
                        i++;
                    } else
                        flag = true;
                }
            }
        }
        return listaUtenti;
    }

    public void deleteUserFromDatabase(String nickname) {
        String functionName = "deleteUserBackOffice";
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"nickname\": \"" + nickname + "\"\n }");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
        System.out.println(resultJSON);
    }

    public void saveModifiesIntoDatabase(String nome, String cognome, String nomePubblico, String email, String stato, String nickname) {
        String functionName = "updateUserBackOffice";
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"nome\": \"" + nome + "\",\"cognome\": \""+cognome+"\",\"nomePubblico\": \""+nomePubblico+"\",\"email\": \""+email+"\",\"stato\": \"" +stato+"\",\"nickname\": \""+nickname+"\"\n}");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
        System.out.println(resultJSON);
    }

    public void banUtenteFromDatabase(String nickname, String email, String nome, String cognome, String nomePubblico) {
        String functionName = "banUtenteBackOffice";
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"nickname\": \"" + nickname + "\",\"email\": \""+email+"\",\"nome\": \""+nome+"\",\"cognome\": \""+cognome+"\",\"nomePubblico\": \""+nomePubblico+"\"\n}");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
        System.out.println(resultJSON);
    }
}
