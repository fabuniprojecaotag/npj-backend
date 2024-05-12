package com.uniprojecao.fabrica.gprojuridico.dto.min;

import com.google.cloud.Timestamp;
import com.uniprojecao.fabrica.gprojuridico.dto.EnvolvidoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AtendimentoMinDTO {
    private String id;
    private String area;
    private String status;
    private EnvolvidoDTO assistido;
    private Timestamp instante;
}
