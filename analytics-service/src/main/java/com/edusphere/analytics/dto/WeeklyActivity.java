package com.edusphere.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklyActivity {
    private String week;
    private int hoursSpent;
    private int assignmentsCompleted;
}
