package com.seven.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seven.web.constant.StudyCellConstant;
import com.seven.web.dao.StudyCellMapper;
import com.seven.web.model.studyCell.DailyPlanWork;
import com.seven.web.model.studyCell.PlanWork;
import com.seven.web.model.studyCell.StudyWork;
import com.seven.web.service.StudyCellService;
import org.apache.commons.lang3.StringUtils;
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
        List<Map<String, Object>> planDicDataList = studyCellMapper.getPlanDicData(StringUtils.EMPTY);
        return JSONObject.toJSONString(planDicDataList);
    }

    @Override
    public String addTodayPlan(String projectName, String percent, String flag, String dailyRemarks) {
        DailyPlanWork dailyPlanWork = new DailyPlanWork();
        dailyPlanWork.setPlan_id(Integer.valueOf(projectName));
        dailyPlanWork.setPlan_percent(Integer.valueOf(percent));
        dailyPlanWork.setRemarks(dailyRemarks);
        studyCellMapper.addTodayPlan(dailyPlanWork);
        if (flag.equals(StudyCellConstant.SWITCH_TRUE)) {
            updateTodayTodo(dailyPlanWork.getId(), Integer.valueOf(percent), true, dailyRemarks);
        }
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String addStudyList(String project, Date startTime) {
        StudyWork studyWork = new StudyWork();
        studyWork.setProject(project);
        studyWork.setStart_time(startTime);
        studyCellMapper.addStudyList(studyWork);
        // 给未来正在记忆曲线内的每天添加任务
        if (insertStudyListDaysPlan(studyWork)) {
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
        dailyPlanWorkList.add(new DailyPlanWork(date, studyId, true));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 1), studyId, false));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 2), studyId, false));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 4), studyId, false));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 7), studyId, false));
        dailyPlanWorkList.add(new DailyPlanWork(delayTime(date, 15), studyId, false));
        studyCellMapper.insertStudyListDaysPlan(dailyPlanWorkList);
        return true;
    }

    /**
     * 将时间推后某一固定天数
     *
     * @param date   原始日期
     * @param dayNum 往后推几天
     */
    private Date delayTime(Date date, int dayNum) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, dayNum); //把日期往后增加一天,整数  往后推,负数往前移动
        Date newDate = new Date(calendar.getTime().getTime()); //这个时间就是日期往后推一天的结果
        return newDate;
    }

    @Override
    public String addPlanTable(String project, String priority, String remarks) {
        // 检查是不是有名字相同的任务，若有，则不允许
        List<Map<String, Object>> nowPlan = studyCellMapper.getPlanDicData(project);
        if (nowPlan.size() > 0) {
            return StudyCellConstant.FAILED + ":有名字相同的任务，则不允许添加";
        }
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
        studyCellMapper.delDailyPlanByPlanId(id);
        studyCellMapper.delPlanList(id);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String updatePlanTable(PlanWork planWork) {
        if (planWork.getPercent() == 100) {
            planWork.setStatus(StudyCellConstant.END);
        } else if (planWork.getPercent() == 0) {
            planWork.setStatus(StudyCellConstant.NOT_START);
        } else if (planWork.getStatus() != StudyCellConstant.DEPENDS) {
            planWork.setStatus(StudyCellConstant.DOING);
        }
        studyCellMapper.updatePlanTable(planWork);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String updateTodayTodo(int id, int percent, boolean finish, String dailyRemarks) {
        DailyPlanWork dailyPlanWork = studyCellMapper.selectDailyPlanWorkById(id);
        dailyPlanWork.setRemarks(dailyRemarks);
        // 计划类待办
        if (dailyPlanWork.getPlan_id() != 0) {
            dailyPlanWork.setPlan_percent(percent);
            String flag = updatePlanStatus(finish, dailyPlanWork);
            if (!StringUtils.isEmpty(flag)) {
                return flag;
            }
        } else {
            // 学习类待办
            updateStudyStatus(finish, dailyPlanWork);
        }
        // 更新今日完成表的状态
        studyCellMapper.updateTodayTodo(dailyPlanWork);
        return StudyCellConstant.SUCCESS;
    }

    @Override
    public String addTodayPlanAndPlan(String project_name, String percent, String priority, String dailyRemarks, String remarks, String flag) {
        // 1.添加计划任务
        String status = addPlanTable(project_name, priority, remarks);
        if (!status.equals(StudyCellConstant.SUCCESS)) {
            return status;
        }
        // 2.获取任务id
        List<Map<String, Object>> nowPlan = studyCellMapper.getPlanDicData(project_name);
        if (nowPlan.size() < 1) {
            LOG.error("addTodayPlanAndPlan failed,addTodayPlanAndPlan failed");
            return StudyCellConstant.FAILED;
        }
        String projectId = nowPlan.get(0).get("value").toString();
        // 3.添加今日任务
        addTodayPlan(projectId, percent, flag, dailyRemarks);
        // 4.更新对应计划任务的状态
        PlanWork planWork = studyCellMapper.selectPlanWorkById(Integer.valueOf(projectId));
        updatePlanTable(planWork);
        return StudyCellConstant.SUCCESS;
    }

    private String updatePlanStatus(boolean finish, DailyPlanWork dailyPlanWork) {
        // 查询添加后计划总量是否超过100
        PlanWork planWork = studyCellMapper.selectPlanWorkById(dailyPlanWork.getPlan_id());
        // 获取上次计划待办的状态
        boolean finishPre = dailyPlanWork.isPlan_finish();

        Map<String, Object> params = new HashMap<>();
        params.put("plan_id", dailyPlanWork.getPlan_id());
        if (finishPre) {
            // 已经为完成: 1.若依旧更改为已完成状态不更改，2. 若更改为未完成状态则需减去百分比
            if (finish) {
                return StringUtils.EMPTY;
            }
            if (planWork.getPercent() - dailyPlanWork.getPlan_percent() < 0) {
                //减去后计划总量是否少于0了
                return StudyCellConstant.WARNING;
            }
            params.put("newPercent", planWork.getPercent() - dailyPlanWork.getPlan_percent());
            if (planWork.getPercent() == 100) {
                // 若原计划进度已到100，则减少后任务应当为未完成
                params.put("status", StudyCellConstant.DOING);
            }
        } else {
            // 未完成: 1.若更改为已完成状态则添加百分比，2. 若更改为未完成状态则不需要更改
            if (!finish) {
                return StringUtils.EMPTY;
            }
            if (planWork.getPercent() + dailyPlanWork.getPlan_percent() > 100) {
                //添加后计划总量是否超过100了
                return StudyCellConstant.WARNING;
            }
            params.put("newPercent", planWork.getPercent() + dailyPlanWork.getPlan_percent());
            if (params.get("newPercent").equals(100)) {
                // 若新计划进度已到100，则减少后任务应当为完成
                params.put("status", StudyCellConstant.END);
            }
        }
        studyCellMapper.updatePlanPercent(params);
        dailyPlanWork.setPlan_finish(finish);
        return StringUtils.EMPTY;
    }

    private void updateStudyStatus(boolean finish, DailyPlanWork dailyPlanWork) {
        dailyPlanWork.setStudy_finish(finish);
        // 1. 获取应学习项
        StudyWork studyWork = studyCellMapper.selectStudyWorkById(dailyPlanWork.getStudy_id());
        // 2. 根据时间确定今天为第几天的学习任务
        Date startDate = studyWork.getStart_time();
        Date endDate = dailyPlanWork.getTime();
        long day = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        if (day == 1L) {
            studyWork.setOne_day(finish);
        } else if (day == 2L) {
            studyWork.setTwo_day(finish);
        } else if (day == 4L) {
            studyWork.setFour_day(finish);
        } else if (day == 7L) {
            studyWork.setSeven_day(finish);
        } else if (day == 15L) {
            studyWork.setFifteen_day(finish);
        } else {
            LOG.error(String.format("there is an error, studyWorkId:{},dailyPlanWorkId:{},diffDay:{}",
                    studyWork.getId(), dailyPlanWork.getId(), day));
        }
        studyCellMapper.updateStudyDayFinish(studyWork);
    }
}
