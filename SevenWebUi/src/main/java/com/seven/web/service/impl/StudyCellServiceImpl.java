package com.seven.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seven.web.constant.StudyCellConstant;
import com.seven.web.dao.StudyCellMapper;
import com.seven.web.model.studyCell.DailyPlanWork;
import com.seven.web.model.studyCell.PlanWork;
import com.seven.web.model.studyCell.StudyWork;
import com.seven.web.service.StudyCellService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.sql.Date;

@Service
public class StudyCellServiceImpl implements StudyCellService {
    private final Logger LOG = Logger.getLogger(StudyCellService.class);

    @Autowired
    private StudyCellMapper studyCellMapper;

    @Override
    public String getPlanTable() {
        List<PlanWork> tmp = studyCellMapper.getPlanTable();
        return JSONObject.toJSONString(tmp);
    }

    @Override
    public String getStudyTable() {
        List<StudyWork> tmp = studyCellMapper.getStudyTable();
        return JSONObject.toJSONString(tmp);
    }

    @Override
    public String getTodayTodoTable() {
        // 每日需进行表单 project finish
        List<Map<String, String>> resultPlanMap = studyCellMapper.getTodayTodoPlanTable();
        List<Map<String, String>> resultStudyMap = studyCellMapper.getTodayTodoStudyTable();
        resultPlanMap.addAll(resultStudyMap);
        return JSONObject.toJSONString(resultPlanMap);
    }

    @Override
    public String getMonthCalendar(java.util.Date date) {
        // 1.获取唯一的完成了任务的日期
        LinkedList<String> dateList = studyCellMapper.getMonthFinishedDay(date);
        List<String> unfinishedDateList = studyCellMapper.getMonthUnFinishedDay(date);
        // 获取没有完全完成任务的日期indexList
        for (String unfinishedDate : unfinishedDateList) {
            for (int i = 0; i < dateList.size(); i++) {
                if (dateList.get(i).equals(unfinishedDate)) {
                    dateList.remove(i);
                    i--;
                }
            }
        }
        // 2.拼接格式
        List<Map<String, String>> dateMapList = new ArrayList<>(dateList.size());
        for (String dateFinished : dateList) {
            Map<String, String> dateMap = new HashMap<>();
            dateMap.put("date", dateFinished);
            dateMap.put("content", null);
            dateMap.put("cid", null);
            dateMapList.add(dateMap);
        }
        return JSONObject.toJSONString(dateMapList);
    }

    @Override
    public String getPlanDicData() {
        List<Map<String, String>> planDicDataList = studyCellMapper.getPlanDicData();
        return JSONObject.toJSONString(planDicDataList);
    }

    @Override
    public String addTodayPlan(String projectName, String percent) {
        studyCellMapper.addTodayPlan(projectName, percent);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String addStudyList(String project, Date startTime) {
        StudyWork studyWork = new StudyWork();
        studyWork.setProject(project);
        studyWork.setStart_time(startTime);
        studyCellMapper.addStudyList(studyWork);
        // 给未来正在记忆曲线内的每天添加任务
        if(insertStudyListDaysPlan(studyWork)) {
            return StudyCellConstant.SUCCESS;
        }
        return StudyCellConstant.FAILED;
    }

    /**
     * 给未来正在记忆曲线内的每天添加任务
     *
     * @param studyWork
     */
    private boolean insertStudyListDaysPlan(StudyWork studyWork) {
        int studyId = studyWork.getId();
        if (studyId == 0) {
            LOG.error("insertStudyList Failed,insertStudyListDaysPlan stop");
            return false;
        }
        Date date = studyWork.getStart_time();
        List<DailyPlanWork> dailyPlanWorkList = new ArrayList<>();
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 1), studyId));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 2), studyId));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 4), studyId));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 7), studyId));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 15), studyId));
        studyCellMapper.insertStudyListDaysPlan(dailyPlanWorkList);
        return true;
    }

    /**
     * 将时间推后某一固定天数
     *
     * @param date 原始日期
     * @param dayNum 往后推几天
     */
    private Date delayTime(Date date,int dayNum) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,dayNum); //把日期往后增加一天,整数  往后推,负数往前移动
        Date newDate=new Date(calendar.getTime().getTime()); //这个时间就是日期往后推一天的结果
        return newDate;
    }

    @Override
    public String addPlanTable(String project, String priority, String remarks) {
        studyCellMapper.addPlanTable(project, StudyCellConstant.NOT_START, priority, remarks);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String delStudyList(String id) {
        studyCellMapper.delStudyList(id);
        // 同时需要删除对应每日任务
        studyCellMapper.delDailyPlanByStudyId(id);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String delPlanList(String id) {
        studyCellMapper.delPlanList(id);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String updatePlanTable(PlanWork planWork) {
        studyCellMapper.updatePlanTable(planWork);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String updateTodayTodo(int id, boolean finish) {
        DailyPlanWork dailyPlanWork = studyCellMapper.selectDailyPlanWorkById(id);
        // 计划类待办
        if(dailyPlanWork.getPlan_id() != 0) {
            dailyPlanWork.setPlan_finish(finish);
            // 查询添加后计划总量是否超过100
            PlanWork planWork = studyCellMapper.selectPlanWorkById(dailyPlanWork.getPlan_id());
            if (planWork.getPercent() + dailyPlanWork.getPlan_percent() > 100) {
                //添加后计划总量是否超过100了
                return StudyCellConstant.WARNING;
            }

            studyCellMapper.updatePlanPercent(dailyPlanWork);
        } else {
            dailyPlanWork.setStudy_finish(finish);
            // 1. 获取应学习项
            StudyWork studyWork = studyCellMapper.selectStudyWorkById(dailyPlanWork.getStudy_id());
            // 2. 根据时间确定今天为第几天的学习任务
            Date startDate = studyWork.getStart_time();
            Date endDate = dailyPlanWork.getTime();
            long day = (endDate.getTime() - startDate.getTime())/(1000*60*60*24);
            studyCellMapper.updateStudyDayFinish(studyWork);
        }
        // 更新今日完成表的状态
        studyCellMapper.updateTodayTodo(dailyPlanWork);
        return StudyCellConstant.SUCCESS;
    }
}
