package Cognito;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LoginCognito {

    public void doLoginAdmin(String username, String password) throws InvocationTargetException, NotAuthorizedException {
        String userPoolId="eu-central-1_lU4xtGPPe";
        String amazonAWSAccessKey="AKIAJYPRFLV3MFY3NPIQ";
        String amazonAWSSecretKey="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";
        String clientId="5jhst4j81qn2tkt6u2ojfljle";

        HashMap authParameters = new HashMap<>();
        authParameters.put("USERNAME",username);
        authParameters.put("PASSWORD",password);
        AdminInitiateAuthRequest req = new AdminInitiateAuthRequest().withClientId(clientId).withUserPoolId(userPoolId).withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(authParameters);
        AWSCredentials credentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        credentialsProvider.getCredentials();
        req.setRequestCredentialsProvider(credentialsProvider);
        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.EU_CENTRAL_1).build();
        provider.adminInitiateAuth(req);
    }
}
