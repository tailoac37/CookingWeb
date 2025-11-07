package projectCooking.Service.EmailService;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import java.io.*;
import java.util.Collections;

public class GmailAuth {
    private static final String TOKENS_DIR_PATH = "tokens";

    public static void main(String[] args) throws Exception {
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        com.google.api.client.http.javanet.NetHttpTransport httpTransport =
                GoogleNetHttpTransport.newTrustedTransport();

        InputStream in = GmailAuth.class.getClassLoader().getResourceAsStream("credentials.json");
        if (in == null) {
            throw new FileNotFoundException("khong tim thay file credentials.json trong resources/");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets,
                Collections.singletonList(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIR_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        System.out.println("Token  da duoc luu /tokens");
    }
}
