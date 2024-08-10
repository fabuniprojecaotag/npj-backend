package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.models.autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.models.usuario.Usuario;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Constants.USUARIOS_COLLECTION;
import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.snapshotToUsuario;

@Repository
@DependsOn("baseRepository")
public class UsuarioRepository extends BaseRepository {

    private final String collectionName = USUARIOS_COLLECTION;

    public List<UsuarioMinDTO> findAll(@Nonnull Integer limit, @Nullable Filter filter) {
        String[] columnList = {"nome", "email", "role", "status", "matricula", "semestre"};
        return findAll(collectionName, columnList, null, limit, filter)
                .stream()
                .map(o -> (UsuarioMinDTO) snapshotToUsuario((DocumentSnapshot) o, true, false))
                .toList();
    }

    public List<UsuarioAutocomplete> findAllMin(@Nonnull Integer limit, @Nullable Filter queryFilter) {
        String[] columnList = {"nome", "role"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (UsuarioAutocomplete) snapshotToUsuario((DocumentSnapshot) o, false, true))
                .toList();
    }

    public Usuario findById(String id) {
        var snapshot = (DocumentSnapshot) findById(collectionName, null, id);
        return (Usuario) snapshotToUsuario(snapshot, false, false);
    }
}
