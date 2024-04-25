package com.uniprojecao.fabrica.gprojuridico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessoDTO {
    @NotBlank
    private String numero;
    @NotBlank
    private String nome;
    @NotBlank
    private String dataDistribuicao;
    @NotBlank
    private String vara;
    @NotBlank
    private String forum;

    @Pattern(regexp = "^ATE\\d{5,}$") // exemplo[]: ["ATE00032", "ATE1234567"]
    private String atendimentoId;
    @NotBlank
    private String status;
}
