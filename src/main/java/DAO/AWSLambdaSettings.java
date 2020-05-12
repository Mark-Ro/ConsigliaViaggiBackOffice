
package DAO;

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
}
