package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.EstagiarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.filter;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.sleep;

@Repository
public class UserRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "usuarios";

    public Usuario saveWithCustomId(String customId, Usuario data) {
        try {
            firestore.collection(collectionName).document(customId).set(data).get();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<?> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter, Boolean returnEntities) {
        ApiFuture<QuerySnapshot> future;

        if (queryFilter != null) {
             future = firestore.collection(collectionName)
                    .where(filter(queryFilter))
                    .select("nome", "email", "role", "status", "matricula", "semestre").limit(limit).get();
        } else {
            future = firestore.collection(collectionName)
                    .select("nome", "email", "role", "status", "matricula", "semestre").limit(limit).get();
        }

        try {
            if (!returnEntities) {
                return future.get().getDocuments();
            }

            List<Object> list = new ArrayList<>();
            var result = future.get().getDocuments();
            for (QueryDocumentSnapshot document : result) {
                if (document.contains("matricula")) list.add(document.toObject(EstagiarioMinDTO.class));
                else list.add(document.toObject(UsuarioMinDTO.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario findById(String id) {
        try {
            DocumentReference document = firestore.collection(collectionName).document(id);
            DocumentSnapshot snapshot = document.get().get();
            if (!snapshot.exists()) return null;
            return (snapshot.contains("matricula")) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean update(String id, Map<String, Object> data) {
        firestore.collection(collectionName).document(id).update(data);
        return true;
    }

    public Boolean delete(String id) {
        try {
            firestore.collection(collectionName).document(id).delete();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean deleteAll(Integer limit, @Nullable QueryFilter queryFilter) {
        var result = (List<QueryDocumentSnapshot>) findAll(limit, queryFilter, false);
        for (QueryDocumentSnapshot document : result) {
            document.getReference().delete();
            sleep(200); // Eventualmente será necessário considerar usar programação reativa no sistema, pois a deleção assíncrona deve garantir a deleção em massa por completo.
        }
        return true;
    }
}
