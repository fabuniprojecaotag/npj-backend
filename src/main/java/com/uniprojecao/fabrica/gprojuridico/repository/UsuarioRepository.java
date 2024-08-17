package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.uniprojecao.fabrica.gprojuridico.services.DocumentSnapshotService.snapshotToUsuario;
import static com.uniprojecao.fabrica.gprojuridico.utils.Constants.USUARIOS_COLLECTION;

@Repository
@DependsOn("baseRepository")
public class UsuarioRepository extends BaseRepository {

    public Usuario findById(String id) {
        DocumentReference document = firestore.collection(USUARIOS_COLLECTION).document(id);
        DocumentSnapshot snapshot = null;
        try {
            snapshot = document.get().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (!snapshot.exists()) return null;
        return (Usuario) snapshotToUsuario(snapshot, false, false);
    }

    public List<UsuarioAutocomplete> findAllMin(@Nonnull Integer limit, @Nullable Filter queryFilter) {
        String[] columnList = {"nome", "role"};
        return findAll(USUARIOS_COLLECTION, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (UsuarioAutocomplete) snapshotToUsuario((DocumentSnapshot) o, false, true))
                .toList();
    }
}
