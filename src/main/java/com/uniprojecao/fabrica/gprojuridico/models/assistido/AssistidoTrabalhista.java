package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import com.uniprojecao.fabrica.gprojuridico.models.Ctps;
import com.uniprojecao.fabrica.gprojuridico.models.Endereco;
import com.uniprojecao.fabrica.gprojuridico.models.Filiacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AssistidoTrabalhista extends Assistido {
    @NotNull
    private Ctps ctps;

    @NotBlank
    private String pis;

    @NotNull
    private Boolean empregadoAtualmente;

    public AssistidoTrabalhista(
            String nome,
            String rg,
            String cpf,
            String nacionalidade,
            String escolaridade,
            String estadoCivil,
            String profissao,
            String telefone,
            String email,
            Filiacao filiacao,
            String remuneracao,
            Map<String, Endereco> endereco,
            Ctps ctps,
            String pis,
            Boolean empregadoAtualmente
    ) {
        super(
                nome,
                rg,
                cpf,
                nacionalidade,
                escolaridade,
                estadoCivil,
                profissao,
                telefone,
                email,
                filiacao,
                remuneracao,
                endereco
        );
        this.ctps = ctps;
        this.pis = pis;
        this.empregadoAtualmente = empregadoAtualmente;
    }
}
