package com.uniprojecao.fabrica.gprojuridico.dto.atendimento;

import com.uniprojecao.fabrica.gprojuridico.domains.atendimento.ParteContraria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class FichaCivilDTO extends FichaDTO {
    private ParteContraria parteContraria;

    public FichaCivilDTO(String assinatura, Boolean dadosSensiveis, List<TestemunhaDTO> testemunhasDTO, ParteContraria parteContraria, String medidaJuridica) {
        super(assinatura, medidaJuridica, dadosSensiveis, testemunhasDTO);
        this.parteContraria = parteContraria;
    }
}
