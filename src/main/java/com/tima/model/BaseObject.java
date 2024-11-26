package com.tima.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseObject implements Serializable {
    private int id;
    private String createdBy;
    private LocalDateTime createdOn;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedOn;
}
