package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.snapshotToUsuario;

@Repository
public class UsuarioRepository {

    private final String COLLECTION_NAME = "usuarios";

    public Usuario save(String customId, Usuario usuario) {
        BaseRepository.save(COLLECTION_NAME, customId, usuario);
        return usuario;
    }

    public List<UsuarioMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] columnList = {"nome", "email", "role", "status", "matricula", "semestre"};
        return BaseRepository.findAll(COLLECTION_NAME, columnList, null, limit, queryFilter)
                .stream()
                .map(o -> (UsuarioMinDTO) snapshotToUsuario((DocumentSnapshot) o, true))
                .toList();
    }

    public Usuario findById(String id) {
        var snapshot = (DocumentSnapshot) BaseRepository.findById(COLLECTION_NAME, null, id);
        return (Usuario) snapshotToUsuario(snapshot, false);
    }

    public void update(String id, Map<String, Object> data) {
        BaseRepository.update(COLLECTION_NAME, id, data);
    }

    public void delete(String id) {
        BaseRepository.delete(COLLECTION_NAME, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        BaseRepository.deleteAll(COLLECTION_NAME, null, limit, queryFilter);
    }
}
