package com.uniprojecao.fabrica.gprojuridico.models.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupervisorMin {
    @Pattern(regexp = "^([a-z]{3,}\\.[a-z]{3,}@projecao\\.br)$")
    private String id;

    @NotBlank
    @Size(min = 3)
    private String nome;
}
