module consigliaviaggi {
    requires javafx.controls;
    requires javafx.fxml;

    requires json.simple;
    requires aws.java.sdk.cognitoidp;
    requires aws.java.sdk.core;
    requires aws.java.sdk.lambda;

    opens GUI to javafx.fxml;
    exports GUI;
}