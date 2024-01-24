package app.web.gprojuridico.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConnection.class);
    public static void initialization() {
        // aqui ele procura a key a partir da pasta back-end, então cuidado para já não estar dentro dela (editor)
        try (FileInputStream serviceAccount = new FileInputStream("./back-end/firebaseAccountKey.json")) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            logger.info("Firebase foi inicializado com sucesso!");
        } catch (IOException e) {
            logger.error("Error durante a inicialização do Firebase:", e);
            throw new RuntimeException("Error durante a inicialização do Firebase:", e);
        }
    }
}
