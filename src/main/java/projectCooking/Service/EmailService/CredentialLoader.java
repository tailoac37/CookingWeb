package projectCooking.Service.EmailService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public class CredentialLoader {

    public static Credential getCredentials() throws Exception {
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        com.google.api.client.http.javanet.NetHttpTransport httpTransport =
                GoogleNetHttpTransport.newTrustedTransport();
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("credentials.json");

        if (in == null) {
            throw new java.io.FileNotFoundException("khong tim thayfile credentials.json trong resources/");
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientSecrets,
                Collections.singletonList("https://www.googleapis.com/auth/gmail.send")
        )
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
        .setAccessType("offline")
        .build();

        return flow.loadCredential("user");
    }
}
