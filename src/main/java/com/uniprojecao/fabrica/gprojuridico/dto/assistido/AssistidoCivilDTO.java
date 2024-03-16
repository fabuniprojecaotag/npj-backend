package com.uniprojecao.fabrica.gprojuridico.dto.assistido;

import com.uniprojecao.fabrica.gprojuridico.domains.assistido.AssistidoCivil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
                new Endereco(a.getEndereco().getLogradouro(), a.getEndereco().getBairro(), a.getEndereco().getNumero(), a.getEndereco().getComplemento(), a.getEndereco().getCep(), a.getEndereco().getCidade()));
        naturalidade = a.getNaturalidade();
        dataNascimento = a.getDataNascimento();
        dependentes = a.getDependentes();
    }
}
