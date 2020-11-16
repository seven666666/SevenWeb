package com.seven.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.seven.web.model.studyCell.DailyPlanWork;
import com.seven.web.model.studyCell.PlanWork;
import com.seven.web.service.StudyCellService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/studyCell")
public class StudyCellController {
    private final Logger LOG = Logger.getLogger(StudyCellController.class);

    @Autowired
    private StudyCellService studyCellService;

    @RequestMapping(value = "/getPlanTable", method = RequestMethod.POST)
    public String getPlanTable() {
        return studyCellService.getPlanTable();
    }

    @RequestMapping(value = "/getStudyTable", method = RequestMethod.POST)
    public String getStudyTable() {
        return studyCellService.getStudyTable();
    }

    @RequestMapping(value = "/getTodayTodoTable", method = RequestMethod.POST)
    public String getTodayTodoTable() {
        return studyCellService.getTodayTodoTable();
    }

    @RequestMapping(value = "/getMonthCalendar", method = RequestMethod.POST)
    public String getMonthCalendar(Date date) {
        return studyCellService.getMonthCalendar(date);
    }

    @RequestMapping(value = "/getPlanDicData", method = RequestMethod.POST)
    public String getPlanDicData() {
        return studyCellService.getPlanDicData();
    }

    @RequestMapping(value = "/addTodayPlan", method = RequestMethod.POST)
    public String addTodayPlan(String project_name, String percent, String dailyRemarks, String flag) {
        LOG.info(String.format("addTodayPlan,planId:%s,percent:%s,flag:%s,dailyRemarks:%s", project_name, percent, flag, dailyRemarks));
        return studyCellService.addTodayPlan(project_name, percent, flag, dailyRemarks);
    }

    @RequestMapping(value = "/addTodayPlanAndPlan", method = RequestMethod.POST)
    public String addTodayPlanAndPlan(String project_name, String percent, String priority, String dailyRemarks, String remarks, String flag) {
        LOG.info(String.format("addTodayPlanAndPlan,planId:%s,percent:%s,priority:%s,dailyRemarks:%s,remarks:%s,flag:%s",
                project_name, percent, priority, dailyRemarks, remarks, flag));
        return studyCellService.addTodayPlanAndPlan(project_name, percent, priority, dailyRemarks, remarks, flag);
    }

    @RequestMapping(value = "/addStudyList", method = RequestMethod.POST)
    public String addStudyList(String project, Date start_time) {
        LOG.info(String.format("addStudyList,project:%s,start_time:%s", project, start_time.toString()));
        return studyCellService.addStudyList(project, new java.sql.Date(start_time.getTime()));
    }

    @RequestMapping(value = "/addPlanTable", method = RequestMethod.POST)
    public String addPlanTable(String project, String priority, String remarks) {
        LOG.info(String.format("addPlanTable,project:%s,priority:%s,remarks:%s", project, priority, remarks));
        return studyCellService.addPlanTable(project, priority, remarks);
    }

    @RequestMapping(value = "/delStudyList", method = RequestMethod.POST)
    public String delStudyList(String id) {
        LOG.info(String.format("delStudyList,id:%s", id));
        return studyCellService.delStudyList(id);
    }

    @RequestMapping(value = "/delPlanList", method = RequestMethod.POST)
    public String delPlanList(String id) {
        LOG.info(String.format("delPlanList,id:%s", id));
        return studyCellService.delPlanList(id);
    }

    @RequestMapping(value = "/updatePlanTable", method = RequestMethod.POST)
    public String updatePlanTable(PlanWork planWork) {
        LOG.info(String.format("updatePlanTable,planWork:%s", JSONObject.toJSONString(planWork)));
        return studyCellService.updatePlanTable(planWork);
    }

    @RequestMapping(value = "/updateTodayTodo", method = RequestMethod.POST)
    public String updateTodayTodo(int id, int percent, boolean finish, String dailyRemarks) {
        LOG.info(String.format("updatePlanTable,id:%s,percent:%s,finish:%s,dailyRemarks:%s", id, percent, finish,dailyRemarks));
        return studyCellService.updateTodayTodo(id, percent, finish,dailyRemarks);
    }
}
