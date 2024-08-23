package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Filiacao {
    @NotBlank
    @Size(min = 3, max = 60)
    private String mae;

    @NotBlank
    @Size(min = 3, max = 60)
    private String pai;
}
