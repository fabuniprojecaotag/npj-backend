package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.SupervisorMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsuarioData {
    public static List<UsuarioDTO> seedWithUsuarioDTO(@Nullable Class<?> type) {
        var unidade = "Taguatinga";
        var senha = "123456";
        var status = true;

        if (type == EstagiarioDTO.class) {
            var role = "ESTAGIARIO";
            var semestre = "8";
            var supervisor = new SupervisorMinDTO("marcos.silva@projecao.br", "Marcos Silva");

            return List.of(
                    new EstagiarioDTO("202102100@projecao.edu.br", "Aurélio Lima Gonçalves", "829.281.342-93", unidade, senha, status, role, "202102100", semestre, supervisor),
                    new EstagiarioDTO("202102302@projecao.edu.br", "Ana Passos Lima", "440.332.095-93", unidade, senha, status, role, "202102302", semestre, supervisor),
                    new EstagiarioDTO("202102299@projecao.edu.br", "Marcos Moura Santos Ferreira", "029.119.234-04", unidade, senha, status, role, "202102299", semestre, supervisor),
                    new EstagiarioDTO("202101055@projecao.edu.br", "Emilly Letícia Cordeiro", "288.231.842-45", unidade, senha, status, role, "202101055", semestre, new SupervisorMinDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva")));
        }

        return List.of(
                new UsuarioDTO("marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("isaque.costa@projecao.br", "Isaque Costa Carvalho", "773.224.145-03", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", unidade, senha, status, "SECRETARIA"));
    }

    public static List<Usuario> seedWithUsuario(@Nullable Class<?> type) {
        return (type == Estagiario.class) ?
                seedWithUsuarioDTO(EstagiarioDTO.class).stream().map(dto -> passDtoToEntity(dto)).toList() :
                seedWithUsuarioDTO(null).stream().map(UsuarioData::passDtoToEntity).toList();
    }

    private static Usuario passDtoToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
        usuario.setSenha(dto.getSenha());
        usuario.setStatus(dto.getStatus());
        usuario.setRole(dto.getRole());

        if (dto instanceof EstagiarioDTO estagiarioDTO) {
            var estagiario = new Estagiario(usuario);
            var supervisorMinDTO = estagiarioDTO.getSupervisor();

            estagiario.setMatricula(estagiarioDTO.getMatricula());
            estagiario.setSemestre(estagiarioDTO.getSemestre());
            estagiario.setSupervisor(new SupervisorMin(supervisorMinDTO.getId(), supervisorMinDTO.getNome()));

            return estagiario;
        }

        return usuario;
    }
}
