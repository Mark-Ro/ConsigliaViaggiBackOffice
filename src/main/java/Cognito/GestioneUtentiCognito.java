package Cognito;

import DAO.AWSLambdaSettings;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

import java.util.ArrayList;
import java.util.Collection;

public class GestioneUtentiCognito {

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
