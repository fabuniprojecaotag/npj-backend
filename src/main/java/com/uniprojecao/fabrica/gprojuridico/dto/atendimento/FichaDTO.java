package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import com.uniprojecao.fabrica.gprojuridico.dto.EnderecoDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FichaCivilDTO.class, name = "Civil"),
        @JsonSubTypes.Type(value = FichaTrabalhistaDTO.class, name = "Trabalhista")
})
public abstract class FichaDTO {
    private String assinatura;
    private Boolean dadosSensiveis;
    private List<TestemunhaDTO> testemunhas = new ArrayList<>();

    public void setTestemunhas(List<TestemunhaDTO> testemunhas) {
        this.testemunhas.addAll(testemunhas);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TestemunhaDTO {
        @NotBlank
        private String nome;
        private String qualificacao;
        private EnderecoDTO endereco; // TODO: Adicionar tipo EnderecoDTO
    }
}
