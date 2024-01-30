package app.web.gprojuridico.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirebaseConnection {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConnection.class);

    public static void initialization() {

        try {
            // Use the application default credentials
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            String projectId = "gprojuridico";
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();
            FirebaseApp.initializeApp(options);

            logger.info("Firebase foi inicializado com sucesso!");
        } catch (IOException e) {
            logger.error("Error durante a inicialização do Firebase:", e);
        }
    }
}
