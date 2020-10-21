package com.seven.web.service;

import com.seven.web.model.studyCell.DailyPlanWork;
import com.seven.web.model.studyCell.PlanWork;

import java.sql.Date;

public interface StudyCellService {

    /**
     * 获取所有计划表单
     *
     * @return 所有计划表单
     */
    String getPlanTable();

    /**
     * 获取所有学习表单
     *
     * @return 所有学习表单
     */
    String getStudyTable();

    /**
     * 获取今天的表单
     *
     * @return 今天的表单
     */
    String getTodayTodoTable();

    /**
     * 获取当前月的日历中完成的日期
     *
     * @param date 某日日期
     * @return 当前月的日历中完成的日期
     */
    String getMonthCalendar(java.util.Date date);

    /**
     * 获取当前没有完成的计划
     *
     * @return [{label:planName,value:planId}]
     */
    String getPlanDicData();

    /**
     * 添加今日计划完成任务的比例
     *
     * @return 成功or失败
     */
    String addTodayPlan(String projectName, String percent);

    /**
     * 添加学习计划表
     *
     * @param project   学习项目名称
     * @param startTime 开始时间
     * @return 是否成功
     */
    String addStudyList(String project, Date startTime);

    /**
     * 添加计划表
     *
     * @param project  项目
     * @param priority 优先级
     * @param remarks  备注
     * @return 是否成功
     */
    String addPlanTable(String project, String priority, String remarks);

    /**
     * 删除学习任务
     *
     * @param id 学习任务id
     * @return 是否成功
     */
    String delStudyList(String id);

    /**
     * 删除计划任务
     *
     * @param id 学习任务id
     * @return 是否成功
     */
    String delPlanList(String id);

    /**
     * 编辑计划任务
     *
     * @param planWork 计划任务 当前更新属性 项目project，进度percent，
     *                 阻断信息depends_on,状态status, 优先级priority，备注remarks
     * @return 是否成功
     */
    String updatePlanTable(PlanWork planWork);

    /**
     * 更新今日需完成状态（计划需更新进度，学习需要更新那天的学习成果）
     *
     * @param id 需要更新的工作id
     * @param finish 当前该项是否完成
     * @return 是否成功
     */
    String updateTodayTodo(int id, boolean finish);
}
