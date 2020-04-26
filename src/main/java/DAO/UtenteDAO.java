package DAO;

import Entity.Utente;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

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
                    nickname = (String) jsonQuery.get("Nickname" + String.valueOf(i));
                    email = (String) jsonQuery.get("Email" + String.valueOf(i));
                    nome = (String) jsonQuery.get("Nome" + String.valueOf(i));
                    cognome = (String) jsonQuery.get("Cognome" + String.valueOf(i));
                    stato = (String) jsonQuery.get("Stato" + String.valueOf(i));
                    media = (String) jsonQuery.get("Media" + String.valueOf(i));
                    numeroRecensioni = (String) jsonQuery.get("NumeroRecensioni" + String.valueOf(i));
                    nomePubblico = (String) jsonQuery.get("NomePubblico" + String.valueOf(i));

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

    public void deleteUserFromCognito(String nickname) {
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AdminDeleteUserRequest req = new AdminDeleteUserRequest();
        req.setUsername(nickname);
        req.setUserPoolId(awsLambdaSettings.getUserPoolId());
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        credentialsProvider.getCredentials();
        req.setRequestCredentialsProvider(credentialsProvider);
        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.EU_CENTRAL_1).build();
        provider.adminDeleteUser(req);
        System.out.println("Eliminato Utente Cognito");
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

    public void saveModifiesIntoCognito(String nickname, String email) {
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AdminUpdateUserAttributesRequest req = new AdminUpdateUserAttributesRequest();

        AttributeType attributeType1 = new AttributeType();
        attributeType1.setName("email");
        attributeType1.setValue(email);

        AttributeType attributeType2 = new AttributeType();
        attributeType2.setName("email_verified");
        attributeType2.setValue("true");

        Collection<AttributeType> userAttributes = new ArrayList<>();
        userAttributes.add(attributeType1);
        userAttributes.add(attributeType2);

        req.setUserAttributes(userAttributes);
        req.setUsername(nickname);
        req.setUserPoolId(awsLambdaSettings.getUserPoolId());
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        credentialsProvider.getCredentials();
        req.setRequestCredentialsProvider(credentialsProvider);
        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.EU_CENTRAL_1).build();
        provider.adminUpdateUserAttributes(req);
    }

    public void banUtenteCognito(String nickname) {
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AdminDisableUserRequest req = new AdminDisableUserRequest();
        req.setUsername(nickname);
        req.setUserPoolId(awsLambdaSettings.getUserPoolId());
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        credentialsProvider.getCredentials();
        req.setRequestCredentialsProvider(credentialsProvider);
        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.EU_CENTRAL_1).build();
        provider.adminDisableUser(req);
    }

    public void unbanUtenteCognito(String nickname) {
        AWSLambdaSettings awsLambdaSettings = AWSLambdaSettings.getIstance();
        AdminEnableUserRequest req = new AdminEnableUserRequest();
        req.setUsername(nickname);
        req.setUserPoolId(awsLambdaSettings.getUserPoolId());
        AWSCredentials credentials = new BasicAWSCredentials(awsLambdaSettings.getAWS_ACCESS_KEY_ID(), awsLambdaSettings.getAWS_SECRET_ACCESS_KEY());
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        credentialsProvider.getCredentials();
        req.setRequestCredentialsProvider(credentialsProvider);
        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.EU_CENTRAL_1).build();
        provider.adminEnableUser(req);
    }
    
}
