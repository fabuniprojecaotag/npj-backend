package com.uniprojecao.fabrica.gprojuridico.dto.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AssistidoCivilDTO extends AssistidoDTO {
    @NotBlank
    private String naturalidade;

    @NotBlank
    private String dataNascimento;

    @NotBlank
    @PositiveOrZero
    private Integer dependentes;

    public AssistidoCivilDTO(AssistidoCivil a) {
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
    }
}
