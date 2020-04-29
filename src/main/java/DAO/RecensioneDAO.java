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

import java.nio.charset.Charset;
import java.util.ArrayList;

public class RecensioneDAO {

    public ArrayList<Recensione> getListaRecensioniForRatificaFromDatabase() {
        String resultJSON = doQueryRatifica();
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
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
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
                    nomeStruttura = (String) jsonQuery.get("NomeStruttura" + String.valueOf(i));
                    nickname = (String) jsonQuery.get("Nickname" + String.valueOf(i));
                    indirizzo = (String) jsonQuery.get("Indirizzo" + String.valueOf(i));
                    nomeCitta = (String) jsonQuery.get("NomeCitta" + String.valueOf(i));
                    testoRecensione = (String) jsonQuery.get("TestoRecensione" + String.valueOf(i));
                    idRecensione = (String) jsonQuery.get("IdRecensione" + String.valueOf(i));
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
        String resultJSON = new String(lmbResult.getPayload().array(), Charset.forName("UTF-8"));
        System.out.println(resultJSON);
    }

}
