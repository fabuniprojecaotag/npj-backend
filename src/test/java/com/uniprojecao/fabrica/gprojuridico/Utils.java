package com.uniprojecao.fabrica.gprojuridico;

import com.google.cloud.NoCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import jakarta.annotation.Nonnull;

public class Utils {
    @Nonnull
    public static Firestore getFirestore() {
        return FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost("localhost:8090")
                .setCredentials(NoCredentials.getInstance())
                .setProjectId("gprojuridico-dev")
                .build()
                .getService();
    }
}
