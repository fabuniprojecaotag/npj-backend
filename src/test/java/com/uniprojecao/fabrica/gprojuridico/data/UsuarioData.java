package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Estagiario;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.SupervisorMin;
import com.uniprojecao.fabrica.gprojuridico.domains.usuario.Usuario;

import java.util.List;

public class UsuarioData {
    public static List<Usuario> seedWithUsuario() {
        var unidade = "Taguatinga";
        var senha = "123456";
        var status = true;
        var role = "ESTAGIARIO";
        var semestre = "8";
        var supervisor = new SupervisorMin("rebeca.lopes", "Rebeca Lopes Silva");

        return List.of(
                new Estagiario("202102100", "202102100@projecao.edu.br", "Aurélio Lima Gonçalves", "829.281.342-93", unidade, senha, status, role, "202102100", semestre, supervisor),
                new Estagiario("202101055", "202101055@projecao.edu.br", "Emilly Letícia Cordeiro", "288.231.842-45", unidade, senha, status, role, "202101055", semestre, supervisor),

                new Usuario("rebeca.lopes", "rebeca.lopes@projecao.br", "Rebeca Lopes Silva", "392.234.119-93", unidade, senha, status, "PROFESSOR"),
                new Usuario("leticia.alves", "leticia.alves@projecao.br", "Letícia Alves Martins", "723.125.889-73", unidade, senha, status, "SECRETARIA")
        );
    }

    public static Usuario seedWithOneUsuario() {
        return new Usuario(null, "marcos.silva@projecao.br", "Marcos Silva", "028.923.381-02", "Taguatinga", "123456", true, "PROFESSOR");
    }
}
