package com.tima.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class BaseObject implements Serializable {
    @Id
    private String id;
    @CreatedDate
    private Instant createdOn;
    @LastModifiedDate
    private Instant lastUpdatedOn;
}
