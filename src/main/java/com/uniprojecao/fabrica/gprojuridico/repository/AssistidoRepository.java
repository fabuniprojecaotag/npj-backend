package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.enums.FilterType;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;

@Repository
public class AssistidoRepository extends BaseRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "usuarios";

    public Assistido saveWithCustomId(String customId, Assistido data) {
        try {
            firestore.collection(collectionName).document(customId).set(data).get();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
