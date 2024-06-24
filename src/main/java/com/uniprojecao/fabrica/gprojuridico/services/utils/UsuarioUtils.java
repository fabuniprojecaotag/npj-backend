package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.Autocomplete.UsuarioAutocomplete;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.EstagiarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;

public class UsuarioUtils {

    public enum UserUniqueField { EMAIL }

    public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO,
                                           Boolean returnAutocomplete) {
        if (snapshot == null) return null;

        boolean dadosEstagiario = snapshot.contains("matricula");

        if (returnMinDTO) {
            if (dadosEstagiario) {
                var object = snapshot.toObject(EstagiarioMinDTO.class);
                object.setId(snapshot.getId());
                return object;
            } else {
                var object = snapshot.toObject(UsuarioMinDTO.class);
                object.setId(snapshot.getId());
                return object;
            }
        }

        if (returnAutocomplete) {
            var object = snapshot.toObject(UsuarioAutocomplete.class);
            return object;
        }

        return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
    }
}
