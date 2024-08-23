package com.uniprojecao.fabrica.gprojuridico.data;

import com.uniprojecao.fabrica.gprojuridico.models.atendimento.Ctps;
import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Filiacao;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoCivil;
import com.uniprojecao.fabrica.gprojuridico.models.assistido.AssistidoTrabalhista;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistidoData {
    public static List<Assistido> seedWithAssistido() {
        Map<String, Endereco> endereco = new HashMap<>();
        return List.of(
                new AssistidoCivil("Cleyton Pina Auzier", "14.762.687-0", "288.610.170-29", "Brasileiro", "Superior", "Casado", "Advogado", "(61) 92413-0161", "cleyton.pina@example.com", new Filiacao("Thayanna Moraes Elias", "José Mário Sena Santos"), "R$ 9.000", endereco, "Brasília", "21/02/1985", 3),
                new AssistidoCivil("Carmen Azevedo Dores", "30.578.754-8", "037.886.591-90", "Brasileira", "Superior", "Casada", "Professora", "(61) 92152-4592", "carmen.azevedo@example.com", new Filiacao("Rosiméri Aguiar Rubi Teixeira", "Bento Leonicio da Mota Ervano"), "R$ 5.200", endereco, "Brasília", "14/04/1993", 1),
                new AssistidoTrabalhista("Rogério Bezerra Brito", "48.433.953-9", "223.128.851-66", "Brasileiro", "Mestrado", "Solteiro", "Gerente financeiro", "(61) 93872-0425", "rogerio.bezerra@example.com", new Filiacao("Kamila Ervano Quintanilha", "Osvaldo dos Anjos Cordeiro"), "R$ 11.100", endereco, new Ctps("0921002", "001-0", "DF"), "276.65825.76-7", true)
        );
    }

    public static Assistido seedWithOneAssistido() {
        Map<String, Endereco> endereco = new HashMap<>();
        return new AssistidoCivil("Cleyton Pina Auzier", "14.762.687-0", "288.610.170-29", "Brasileiro", "Superior", "Casado", "Advogado", "(61) 92413-0161", "cleyton.pina@example.com", new Filiacao("Thayanna Moraes Elias", "José Mário Sena Santos"), "R$ 9.000", endereco, "Brasília", "21/02/1985", 3);
    }
}
