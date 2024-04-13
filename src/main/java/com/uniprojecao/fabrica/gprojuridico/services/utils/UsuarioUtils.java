package com.uniprojecao.fabrica.gprojuridico.services.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.min.EstagiarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.min.UsuarioMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.SupervisorMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;

public class UsuarioUtils {

    public static Usuario dtoToUsuario(UsuarioDTO dto) {
        if (dto instanceof EstagiarioDTO e) {
            var estagiario = new Estagiario();
            var supervisorMinDTO = e.getSupervisor();

            estagiario.setId(dto.getId());
            estagiario.setEmail(dto.getEmail());
            estagiario.setNome(dto.getNome());
            estagiario.setCpf(dto.getCpf());
            estagiario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
            estagiario.setSenha(dto.getSenha());
            estagiario.setStatus(dto.getStatus());
            estagiario.setRole(dto.getRole());
            estagiario.setMatricula(e.getMatricula());
            estagiario.setSemestre(e.getMatricula());
            estagiario.setSupervisor(new SupervisorMin(supervisorMinDTO.getId(), supervisorMinDTO.getNome()));

            return estagiario;
        }
        Usuario usuario = new Usuario();

        usuario.setId(dto.getId());
        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
        usuario.setSenha(dto.getSenha());
        usuario.setStatus(dto.getStatus());
        usuario.setRole(dto.getRole());

        return usuario;
    }

    public enum UserUniqueField { EMAIL }

    public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO) {
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

        return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
    }

    public static UsuarioDTO usuarioToDto(Usuario u) {
        if (u instanceof Estagiario e) {
            var eDto = new EstagiarioDTO();

            eDto.setId(u.getId());
            eDto.setEmail(u.getEmail());
            eDto.setNome(u.getNome());
            eDto.setCpf(u.getCpf());
            eDto.setUnidadeInstitucional(u.getUnidadeInstitucional());
            eDto.setSenha(u.getSenha());
            eDto.setStatus(u.getStatus());
            eDto.setRole(u.getRole());
            eDto.setMatricula(e.getMatricula());
            eDto.setSemestre(e.getSemestre());
            eDto.setSupervisor(new SupervisorMinDTO(
                    e.getSupervisor().getId(),
                    e.getSupervisor().getNome()
            ));

            return eDto;
        }
        var dto = new UsuarioDTO();

        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setNome(u.getNome());
        dto.setCpf(u.getCpf());
        dto.setUnidadeInstitucional(u.getUnidadeInstitucional());
        dto.setSenha(u.getSenha());
        dto.setStatus(u.getStatus());
        dto.setRole(u.getRole());

        return dto;
    }
}
