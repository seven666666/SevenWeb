package com.seven.web.model.studyCell;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class StudyWork {
    int id;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    Date start_time;
    String project;
    boolean one_day;
    boolean two_day;
    boolean four_day;
    boolean seven_day;
    boolean fifteen_day;
}
