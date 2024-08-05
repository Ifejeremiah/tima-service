package com.tima.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Stream {
    private Integer id;
    private Integer recordCount;
    private Integer preparedCount;
    private String status;
    private LocalDateTime lastUpdatedOn;
}
