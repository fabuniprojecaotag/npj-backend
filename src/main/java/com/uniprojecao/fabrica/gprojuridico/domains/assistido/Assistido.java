package com.uniprojecao.fabrica.gprojuridico.domains.assistido;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.cloud.firestore.annotation.DocumentId;
import com.uniprojecao.fabrica.gprojuridico.domains.Endereco;
import com.uniprojecao.fabrica.gprojuridico.domains.Filiacao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssistidoCivil.class, name = "Civil"),
        @JsonSubTypes.Type(value = AssistidoTrabalhista.class, name = "Trabalhista"),
        @JsonSubTypes.Type(value = AssistidoFull.class, name = "Full"),
})
public abstract class Assistido {
    @NotBlank
    @Size(min = 3, max = 60)
    private String nome;

    @Pattern(regexp = "^\\d\\.\\d{3}\\.\\d{3}$") // exemplo: 0.000.000
    private String rg;

    @DocumentId
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$") // exemplo: 000.000.000-00
    private String cpf;

    private String nacionalidade;
    private String escolaridade;
    private String estadoCivil;

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
}
