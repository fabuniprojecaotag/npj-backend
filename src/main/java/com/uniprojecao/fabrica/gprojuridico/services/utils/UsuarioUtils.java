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
import com.uniprojecao.fabrica.gprojuridico.repository.UsuarioRepository;
import com.uniprojecao.fabrica.gprojuridico.services.UsuarioService;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;

import java.util.Objects;

public class UsuarioUtils {
    public static Usuario dtoToUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
        usuario.setSenha(dto.getSenha());
        usuario.setStatus(dto.getStatus());
        usuario.setRole(dto.getRole());

        if (dto instanceof EstagiarioDTO estagiarioDTO) {
            var estagiario = (Estagiario) usuario;
            var supervisorMinDTO = estagiarioDTO.getSupervisor();

            estagiario.setMatricula(estagiarioDTO.getMatricula());
            estagiario.setSemestre(estagiarioDTO.getMatricula());
            estagiario.setSupervisor(new SupervisorMin(supervisorMinDTO.getId(), supervisorMinDTO.getNome()));

            return estagiario;
        }

        return usuario;
    }

    public static void verifyIfExistsUserInDatabase(UsuarioDTO dto, String id) {
        var user = new UsuarioService(new UsuarioRepository());
        var foundedUser = user.findById(id);
        if (foundedUser != null) {
            String userEmail = foundedUser.getEmail();
            String userCpf = foundedUser.getCpf();
            Boolean equalEmails = Objects.equals(userEmail, id);
            Boolean equalCpfs = Objects.equals(userCpf, dto.getCpf());

            if (equalEmails && equalCpfs) {
                throw new UserAlreadyCreatedException(userEmail, userCpf);
            } else {
                throw new UserAlreadyCreatedException(UserUniqueField.EMAIL, userEmail);
            }
        }
    }

    public enum UserUniqueField { EMAIL }

    public static Object snapshotToUsuario(DocumentSnapshot snapshot, Boolean returnMinDTO) {
        boolean dadosEstagiario = snapshot.contains("matricula");

        if (returnMinDTO) return (dadosEstagiario) ? snapshot.toObject(EstagiarioMinDTO.class) : snapshot.toObject(UsuarioMinDTO.class);

        return (dadosEstagiario) ? snapshot.toObject(Estagiario.class) : snapshot.toObject(Usuario.class);
    }

    public static UsuarioDTO usuarioToDto(Usuario u) {
        var dto = new UsuarioDTO();

        dto.setEmail(u.getEmail());
        dto.setNome(u.getNome());
        dto.setCpf(u.getCpf());
        dto.setUnidadeInstitucional(u.getUnidadeInstitucional());
        dto.setSenha(u.getSenha());
        dto.setStatus(u.getStatus());
        dto.setRole(u.getRole());

        if (u instanceof Estagiario e) {
            var eDto = (EstagiarioDTO) dto;

            eDto.setMatricula(e.getMatricula());
            eDto.setSemestre(e.getSemestre());
            eDto.setSupervisor(new SupervisorMinDTO(
                    e.getSupervisor().getId(),
                    e.getSupervisor().getNome()
            ));

            return eDto;
        }

        return dto;
    }
}
