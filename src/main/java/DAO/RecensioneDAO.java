package DAO;

import Entity.Recensione;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RecensioneDAO {

    public ArrayList<Recensione> getListaRecensioniForRatificaFromDatabase() {
        String resultJSON = doQueryRatifica();
        ArrayList<Recensione> listaRecensioni = analizzaQuery(resultJSON);
        return listaRecensioni;
    }

    public ArrayList<Recensione> getListaRecensioniForGestioneFromDatabase(String nomeStruttura, String nickname, String citta, String indirizzo) {
        if (nomeStruttura.isEmpty() || nomeStruttura.isBlank())
            nomeStruttura="null";

        if (nickname.isEmpty() || nickname.isBlank())
            nickname="null";

        if (citta.isEmpty() || citta.isBlank())
            citta="null";

        if (indirizzo.isEmpty() || indirizzo.isBlank())
            indirizzo="null";

        String resultJSON = doQueryGestioneRecensioni(nomeStruttura,nickname,citta,indirizzo);
        ArrayList<Recensione> listaRecensioni = analizzaQuery(resultJSON);
        return listaRecensioni;
    }

    private String doQueryRatifica() {
        String functionName = "queryRecensioniBackOffice";

        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + "\"table\": \"recensioni\"\n}");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);
        System.out.println(resultJSON);
        return resultJSON;
    }

    private String doQueryGestioneRecensioni(String nomeStruttura, String nickname, String citta, String indirizzo) {
        String functionName = "queryGestioneRecensioniBackOffice";

        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"nomeStruttura\": \"" + nomeStruttura + "\",\"nickname\": \""+nickname+"\",\"citta\": \""+citta+"\",\"indirizzo\": \""+indirizzo+"\"\n }");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);
        System.out.println(resultJSON);
        return resultJSON;
    }

    private ArrayList<Recensione> analizzaQuery(String resultJSON) {
        String messageID,query;
        ArrayList<Recensione> listaRecensioni = new ArrayList<>();
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
            String nomeStruttura, nickname, indirizzo, nomeCitta, testoRecensione, idRecensione;
            if (jsonQuery != null) {
                while (flag == false) {
                    nomeStruttura = (String) jsonQuery.get("NomeStruttura" + i);
                    nickname = (String) jsonQuery.get("Nickname" + i);
                    indirizzo = (String) jsonQuery.get("Indirizzo" + i);
                    nomeCitta = (String) jsonQuery.get("NomeCitta" + i);
                    testoRecensione = (String) jsonQuery.get("TestoRecensione" + i);
                    idRecensione = (String) jsonQuery.get("IdRecensione" + i);
                    if (nickname != null) {
                        listaRecensioni.add(new Recensione(Integer.parseInt(idRecensione),nomeStruttura,nickname,indirizzo,nomeCitta,testoRecensione));
                        i++;
                    } else
                        flag = true;
                }
            }
        }
        return listaRecensioni;
    }

    public void deleteReviewFromDatabase(String idRecensione) {
        String functionName = "funzioneLambdaDeleteRecensione";
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"table\": \"" + idRecensione + "\"\n }");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);
        System.out.println(resultJSON);
    }

    public void acceptReviewDatabase(String idRecensione) {
        String functionName = "updateRecensioneBackOffice";
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        InvokeRequest lmbRequest = new InvokeRequest().withFunctionName(functionName).withPayload("{\n" + " \"idRecensione\": \"" + idRecensione + "\"\n}");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String resultJSON = new String(lmbResult.getPayload().array(), StandardCharsets.UTF_8);
        System.out.println(resultJSON);
    }



}
