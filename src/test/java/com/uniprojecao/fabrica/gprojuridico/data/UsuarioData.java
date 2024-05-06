package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.SupervisorMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UsuarioData {

    @Autowired
    private ModelMapper modelMapper;

    public static List<UsuarioDTO> seedWithUsuarioDTO() {
        var unidade = "Taguatinga";
        var senha = "123456";
        var status = true;
        var role = "ESTAGIARIO";
        var semestre = "8";
        var supervisor = new SupervisorMinDTO("marcos.silva", "Marcos Silva");

        return List.of(
                new EstagiarioDTO("202102100", "202102100@projecao.edu.br", "Aurélio Lima Gonçalves", "829.281.342-93", unidade, senha, status, role, "202102100", semestre, supervisor),
                new EstagiarioDTO("202102302", "202102302@projecao.edu.br", "Ana Passos Lima", "440.332.095-93", unidade, senha, status, role, "202102302", semestre, supervisor),
                new EstagiarioDTO("202101055", "202101055@projecao.edu.br", "Emilly Letícia Cordeiro", "288.231.842-45", unidade, senha, status, role, "202101055", semestre, new SupervisorMinDTO("rebeca.lopes@projecao.br", "Rebeca Lopes Silva")),

                new UsuarioDTO("marcos.silva", "marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("rebeca.lopes", "rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("leticia.alves", "leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", unidade, senha, status, "SECRETARIA")
        );
    }

    public static UsuarioDTO seedWithOneUsuarioDTO() {
        return new UsuarioDTO("marcos.silva", "marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR");
    }

    public static List<Usuario> seedWithUsuario() {
        return seedWithUsuarioDTO().stream().map(dto -> dtoToEntity(dto)).toList();
    }

    private static Usuario dtoToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setUnidadeInstitucional(dto.getUnidadeInstitucional());
        usuario.setSenha(dto.getSenha());
        usuario.setStatus(dto.getStatus());
        usuario.setRole(dto.getRole());

//        if (dto instanceof EstagiarioDTO estagiarioDTO) {
//            var estagiario = new Estagiario(usuario);
//            var supervisorMinDTO = estagiarioDTO.getSupervisor();
//
//            estagiario.setMatricula(estagiarioDTO.getMatricula());
//            estagiario.setSemestre(estagiarioDTO.getSemestre());
//            estagiario.setSupervisor(new SupervisorMin(supervisorMinDTO.getId(), supervisorMinDTO.getNome()));
//
//            return estagiario;
//        }

        return usuario;
    }
}
