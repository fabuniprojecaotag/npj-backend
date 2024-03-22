package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.DocumentosDepositadosNpj;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.Reclamado;
import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.RelacaoEmpregaticia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FichaTrabalhistaDTO extends FichaDTO {
    private Reclamado reclamado;
    private RelacaoEmpregaticia relacaoEmpregaticia;
    private DocumentosDepositadosNpj documentosDepositadosNpj;
    private String outrasInformacoes;

    public FichaTrabalhistaDTO(String assinatura, Boolean dadosSensiveis, List<TestemunhaDTO> testemunhasDTO, Reclamado reclamado, RelacaoEmpregaticia relacaoEmpregaticia, DocumentosDepositadosNpj documentosDepositadosNpj, String outrasInformacoes) {
        super(assinatura, dadosSensiveis, testemunhasDTO);
        this.reclamado = reclamado;
        this.relacaoEmpregaticia = relacaoEmpregaticia;
        this.documentosDepositadosNpj = documentosDepositadosNpj;
        this.outrasInformacoes = outrasInformacoes;
    }
}
