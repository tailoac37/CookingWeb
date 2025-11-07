package projectCooking.Service.EmailService;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message; // Gmail API message
import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

public class GmailSendService {

    public static void sendEmail(Credential credential, String to, String subject, String bodyText) throws Exception {
        Gmail service = new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("CookingApp")
                .build();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("taih1112004@gmail.com", "CookingApp Support"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject, "UTF-8");
        email.setText(bodyText, "UTF-8");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        // Gmail API message
        com.google.api.services.gmail.model.Message gmailMessage = new com.google.api.services.gmail.model.Message();
        gmailMessage.setRaw(encodedEmail);

        service.users().messages().send("me", gmailMessage).execute();
        System.out.println("Email da duoc gui den: " + to);
    }
}

