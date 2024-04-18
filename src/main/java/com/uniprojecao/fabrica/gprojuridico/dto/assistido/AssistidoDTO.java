package com.uniprojecao.fabrica.gprojuridico.dto.assistido;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssistidoCivilDTO.class, name = "Civil"),
        @JsonSubTypes.Type(value = AssistidoTrabalhistaDTO.class, name = "Trabalhista"),
        @JsonSubTypes.Type(value = AssistidoFullDTO.class, name = "Full"),
})
public abstract class AssistidoDTO {
    @NotBlank
    @Size(min = 3, max = 60)
    private String nome;

    @Pattern(regexp = "^\\d\\.\\d{3}\\.\\d{3}$") // exemplo: 0.000.000
    private String rg;

    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$") // exemplo: 000.000.000-00
    private String cpf;

    @NotBlank
    private String nacionalidade;
    private String escolaridade; // Enum Escolaridade convertido em String
    private String estadoCivil; // Enum EstadoCivil convertido em String

    @NotBlank
    @Size(min = 3, max = 30)
    private String profissao;

    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{5}-\\d{4}$") // exemplo: (00) 90000-0000
    private String telefone;

    @Email
    private String email;
    private Filiacao filiacao;

    @Pattern(regexp = "^(R\\$)\\s\\d+(\\.\\d{1,3})*$") // exemplo[]: ["R$ 9000", "R$ 90.000", "R$ 90.000.000"]
    private String remuneracao;
    private Map<String, Endereco> endereco;

    public AssistidoDTO(String nome, String rg, String cpf, String nacionalidade, String escolaridade, String estadoCivil, String profissao, String telefone, String email, Filiacao filiacao, String remuneracao, Map<String, Endereco> endereco) {
        this.nome = nome;
        this.rg = rg;
        this.cpf = cpf;
        this.nacionalidade = nacionalidade;
        setEscolaridade(escolaridade);
        setEstadoCivil(estadoCivil);
        this.profissao = profissao;
        this.telefone = telefone;
        this.email = email;
        this.filiacao = filiacao;
        this.remuneracao = remuneracao;
        this.endereco = endereco;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = Escolaridade.valueOf(escolaridade
                .replace(" ", "_")
                .replace("é", "e")
                .replace("ó", "o")
                .replace("ç", "c")
                .replace("ã", "a")
                .toUpperCase()).getValue();
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = EstadoCivil.valueOf(estadoCivil
                .replace("ú", "u")
                .toUpperCase()).getValue();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Filiacao {
        @NotBlank
        @Size(min = 3, max = 60)
        private String mae;

        @NotBlank
        @Size(min = 3, max = 60)
        private String pai;
    }

    @Getter
    enum Escolaridade {
        FUNDAMENTAL("Fundamental"),
        MEDIO("Médio"),
        SUPERIOR("Superior"),
        POS_GRADUACAO("Pós Graduação"),
        MESTRADO("Mestrado"),
        DOUTORADO("Doutorado");

        private final String value;

        Escolaridade(String value) {
            this.value = value;
        }
    }

    @Getter
    enum EstadoCivil {
        SOLTEIRO("Solteiro"),
        SOLTEIRA("Solteira"),
        CASADO("Casado"),
        CASADA("Casada"),
        SEPARADO("Separado"),
        SEPARADA("Separada"),
        DIVORCIADO("Divorciado"),
        DIVORCIADA("Divorciada"),
        VIUVO("Viúvo"),
        VIUVA("Viúva");

        private final String value;

        EstadoCivil(String value) {
            this.value = value;
        }
    }
}
