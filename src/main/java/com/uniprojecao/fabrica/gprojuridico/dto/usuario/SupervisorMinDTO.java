package com.uniprojecao.fabrica.gprojuridico.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SupervisorMinDTO {
    @Pattern(regexp = "^[a-z]{3,}\\.[a-z]{3,}@projecao\\.br$")
    private String id;

    @NotBlank
    @Size(min = 3)
    private String nome;
}
