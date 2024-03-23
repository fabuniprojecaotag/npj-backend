package com.uniprojecao.fabrica.gprojuridico.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.QueryFilter;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.UsuarioUtils.snapshotToUsuario;

@Repository
public class UsuarioRepository extends BaseRepository {

    @Autowired
    public Firestore firestore;

    private static final String collectionName = "usuarios";

    public Usuario save(String customId, Usuario data) {
        super.save(collectionName, customId, data);
        return data;
    }

    public List<UsuarioMinDTO> findAll(@Nonnull Integer limit, @Nullable QueryFilter queryFilter) {
        String[] list = {"nome", "email", "role", "status", "matricula", "semestre"};
        return super.findAll(collectionName, list, null, limit, queryFilter)
                .stream()
                .map(o -> (UsuarioMinDTO) snapshotToUsuario((DocumentSnapshot) o, true))
                .toList();
    }

    public Usuario findById(String id) {
        var snapshot = (DocumentSnapshot) super.findById(collectionName, null, id);
        return (Usuario) snapshotToUsuario(snapshot, false);
    }

    public void update(String id, Map<String, Object> data) {
        super.update(collectionName, id, data);
    }

    public void delete(String id) {
        super.delete(collectionName, id);
    }

    public void deleteAll(int limit, @Nullable QueryFilter queryFilter) {
        super.deleteAll(collectionName, null, limit, queryFilter);
    }
}
