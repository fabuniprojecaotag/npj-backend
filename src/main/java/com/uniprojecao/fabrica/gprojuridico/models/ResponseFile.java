package com.uniprojecao.fabrica.gprojuridico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseFile {
    private String name;
    private String type;
    private long size;
}
