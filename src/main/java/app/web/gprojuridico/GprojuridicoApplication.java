package app.web.gprojuridico;

import app.web.gprojuridico.repository.FirebaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GprojuridicoApplication {

	public static void main(String[] args) {
		FirebaseConnection.initialization();
		SpringApplication.run(GprojuridicoApplication.class, args);
	}
}
