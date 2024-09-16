package com.uniprojecao.fabrica.gprojuridico.models.assistido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filiacao {
    @NotBlank
    @Size(min = 3, max = 60)
    private String mae;

    @NotBlank
    @Size(min = 3, max = 60)
    private String pai;
}
