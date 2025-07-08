package com.tima.model;

import com.tima.enums.JobStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job extends BaseObject {
    private JobStatus status;
    private String errorFileName;
}
