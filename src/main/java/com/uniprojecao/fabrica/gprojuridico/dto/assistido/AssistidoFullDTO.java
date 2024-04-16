package com.uniprojecao.fabrica.gprojuridico.dto.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoFull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
                Map.of(
                        "residencial", a.getEndereco().get("residencial"),
                        "comercial", a.getEndereco().get("comercial")
                )
        );
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
        @NotBlank
        private String numero;

        @NotBlank
        private String serie;

        @NotBlank
        @Size(min = 2, max = 2)
        private String uf;
    }
}
