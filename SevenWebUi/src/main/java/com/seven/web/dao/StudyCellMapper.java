package com.seven.web.dao;

import com.seven.web.model.studyCell.DailyPlanWork;
import com.seven.web.model.studyCell.PlanWork;
import com.seven.web.model.studyCell.StudyWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface StudyCellMapper {
    List<PlanWork> getPlanTable();

    List<StudyWork> getStudyTable();

    LinkedList<String> getMonthFinishedDay(@Param("queryDate") Date queryDate);

    List<String> getMonthUnFinishedDay(@Param("queryDate") Date queryDate);

    List<Map<String, String>> getPlanDicData();

    void addTodayPlan(@Param("projectName") String projectName, @Param("percent") String percent);

    void addStudyList(StudyWork studyWork);

    void addPlanTable(@Param("project") String project, @Param("status") String status, @Param("priority") String priority, @Param("remarks") String remarks);

    void delStudyList(@Param("studyId") String id);

    void delPlanList(@Param("planId") String id);

    List<Map<String, String>> getTodayTodoPlanTable();

    List<Map<String, String>> getTodayTodoStudyTable();

    void updatePlanTable(PlanWork planWork);

    void insertStudyListDaysPlan(@Param("dailyPlanWorkList") List<DailyPlanWork> dailyPlanWorkList);

    void delDailyPlanByStudyId(@Param("studyId") String studyId);

    void updateTodayTodo(@Param("dailyPlanWork") DailyPlanWork dailyPlanWork);

    DailyPlanWork selectDailyPlanWorkById(@Param("id") int id);

    void updatePlanPercent(@Param("dailyPlanWork")DailyPlanWork dailyPlanWork);

    PlanWork selectPlanWorkById(@Param("plan_id") int plan_id);

    StudyWork selectStudyWorkById(@Param("study_id") int study_id);

    void updateStudyDayFinish(@Param("studyWork")StudyWork studyWork);
}
