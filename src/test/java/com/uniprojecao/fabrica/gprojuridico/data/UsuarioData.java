package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.EstagiarioDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.SupervisorMinDTO;
import com.uniprojecao.fabrica.gprojuridico.dto.usuario.UsuarioDTO;

import java.util.List;

import static com.uniprojecao.fabrica.gprojuridico.services.utils.Utils.ModelMapper.toEntity;

public class UsuarioData {
    public static List<UsuarioDTO> seedWithUsuarioDTO() {
        var unidade = "Taguatinga";
        var senha = "123456";
        var status = true;
        var role = "ESTAGIARIO";
        var semestre = "8";
        var supervisor = new SupervisorMinDTO("rebeca.lopes", "Rebeca Lopes Silva");

        return List.of(
                new EstagiarioDTO("202102100", "202102100@projecao.edu.br", "Aurélio Lima Gonçalves", "829.281.342-93", unidade, senha, status, role, "202102100", semestre, supervisor),
                new EstagiarioDTO("202101055", "202101055@projecao.edu.br", "Emilly Letícia Cordeiro", "288.231.842-45", unidade, senha, status, role, "202101055", semestre, supervisor),

                new UsuarioDTO("rebeca.lopes", "rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", unidade, senha, status, "PROFESSOR"),
                new UsuarioDTO("leticia.alves", "leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", unidade, senha, status, "SECRETARIA")
        );
    }

    public static UsuarioDTO seedWithOneUsuarioDTO() {
        return new UsuarioDTO(null, "marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR");
    }

    public static Usuario seedWithOneUsuario() {
        return new Usuario(null, "marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR");
    }

    public static List<Usuario> seedWithUsuario() {
        return seedWithUsuarioDTO().stream().map(dto -> toEntity(dto)).toList();
    }
}
