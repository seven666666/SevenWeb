package com.seven.web.model.studyCell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class DailyPlanWork {
    int id;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date time;
    int plan_id;
    int plan_percent;
    boolean plan_finish;
    int study_id;
    boolean study_finish;
    String remarks;

    public DailyPlanWork() {
    }

    public DailyPlanWork(Date time, int study_id, boolean study_finish) {
        this.time = time;
        this.study_id = study_id;
        this.study_finish = study_finish;
    }
}
