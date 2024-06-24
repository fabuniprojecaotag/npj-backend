package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
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

    public List<UsuarioMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome", "email", "role", "status", "matricula", "semestre"};
        return findAll(collectionName, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (UsuarioMinDTO) snapshotToUsuario((DocumentSnapshot) o, true, false))
                .toList();
    }

    public List<UsuarioAutocomplete> findAllMin(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
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
