package com.tima.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserSummary {
    private Integer totalAdminUsers;
    private Integer totalActiveAdminUsers;
    private Integer totalInActiveAdminUsers;
    private Integer totalSuspendedAdminUsers;
}
