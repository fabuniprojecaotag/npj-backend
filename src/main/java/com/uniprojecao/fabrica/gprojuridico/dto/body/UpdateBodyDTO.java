package com.uniprojecao.fabrica.gprojuridico.dto.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateBodyDTO<T> {
    T body;
    String classType;
}
