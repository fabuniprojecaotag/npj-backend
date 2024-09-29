package com.uniprojecao.fabrica.gprojuridico.dto.body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ListBodyDTO<T> {
    private List<T> list;
    private T firstDoc;
    private T lastDoc;
    private int pageSize;
    private int totalSize;
}

