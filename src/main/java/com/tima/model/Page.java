package com.tima.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    private Long count;
    private List<T> content;
}
