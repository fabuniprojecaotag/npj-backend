package com.uniprojecao.fabrica.gprojuridico.dto.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.Assistido;
import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoFull;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistidoFullDTO extends AssistidoDTO {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @NotBlank
    @PositiveOrZero
    private Integer dependentes;

    @NotNull
    private Ctps ctps;

    @NotBlank
    private String pis;

    @NotNull
    private Boolean empregadoAtualmente;

    public AssistidoFullDTO(AssistidoFull a) {
        super(
                a.getNome(),
                a.getRg(),
                a.getCpf(),
                a.getNacionalidade(),
                a.getEscolaridade(),
                a.getEstadoCivil(),
                a.getProfissao(),
                a.getTelefone(),
                a.getEmail(),
                new Filiacao(a.getFiliacao().getMae(), a.getFiliacao().getPai()),
                a.getRemuneracao(),
                new Endereco(a.getEndereco().getLogradouro(), a.getEndereco().getBairro(), a.getEndereco().getNumero(), a.getEndereco().getComplemento(), a.getEndereco().getCep(), a.getEndereco().getCidade()));
        naturalidade = a.getNaturalidade();
        dataNascimento = a.getDataNascimento();
        dependentes = a.getDependentes();
        ctps = new Ctps(a.getCtps().getNumero(), a.getCtps().getSerie(), a.getCtps().getUf());
        pis = a.getPis();
        empregadoAtualmente = a.getEmpregadoAtualmente();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Ctps {
        @Positive
        private Integer numero;

        @Positive
        private Integer serie;

        @NotBlank
        @Size(min = 2, max = 2)
        private String uf;
    }
}
