package com.smj.testboot.service.impl;


import com.google.common.collect.Lists;
import com.smj.testboot.bean.ReportVo;
import com.smj.testboot.common.CommonUtils;
import com.smj.testboot.common.StatisReportRecordPageQuery;
import com.smj.testboot.mapper.AppointMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class StatisReportServiceImpl implements StatisReportService {


    @Resource
    private AppointMapper appointMapper;


    public void generateStatisReport(String logId, Date startDate, Date endDate) {
        long s = System.currentTimeMillis();

//1. 第一张表  statis_sch_appoint_channel
        // 1.1 首先在order表中获得所有的预约/挂号等数据(不包括渠道放号数)
//        List<ReportVo> reportOrder = schOrderDao.selectReportChannel(startDate, endDate);
        StatisReportRecordPageQuery statisReportRecordPageQuery = new StatisReportRecordPageQuery();
        statisReportRecordPageQuery.setStartDate(startDate);
        statisReportRecordPageQuery.setEndDate(endDate);
        List<ReportVo> reportOrder = CommonUtils.queryLargeRecordPageWrapper(statisReportRecordPageQuery, query -> appointMapper.selectOrderCountByLargeRecordPage(statisReportRecordPageQuery), query -> appointMapper.selectOrderNumByPageQuery(statisReportRecordPageQuery));


        // 1.2 过滤/补充数据
        List<ReportVo> reportOrderList = filterReportChannel(reportOrder, startDate, endDate);


        // 1.3 从数据库库中查询第一张表的数据  判断是否需要 新增或者更新
//        List<ReportVo> reportChannelDB = appointMapper.selectOneDay(startDate, endDate);
        List<ReportVo> reportChannelDB = CommonUtils.queryLargeRecordPageWrapper(statisReportRecordPageQuery, query -> appointMapper.selectDaysCountByLargeRecordPage(statisReportRecordPageQuery), query -> appointMapper.selectDaysByPageQuery(statisReportRecordPageQuery));

        reportChannelDB = reportChannelDB.stream().filter(reportVo -> reportVo.getSchResultId() != null && reportVo.getChannelType() != null).collect(Collectors.toList());
        //第一张表数据的 新增
        List<ReportVo> insertDB = insertAppoChannel(reportOrderList, reportChannelDB);
        //第一张表数据的 更新
        List<ReportVo> updateDB = updateAppoChannel(reportOrderList, reportChannelDB);


//2. 第二张表 statis_sch_appoint表
        //查询数据  包括各科室的 放号总量
        List<ReportVo> insertAppointDB = null;
        List<ReportVo> updateAppointDB = null;
        StatisReportRecordPageQuery appointRecordPageQuery = new StatisReportRecordPageQuery();
        appointRecordPageQuery.setStartDate(startDate);
        appointRecordPageQuery.setEndDate(endDate);
        if (CollectionUtils.isNotEmpty(insertDB)) {
            List<String> resultIds = insertDB.stream().map(ReportVo::getSchResultId).distinct().collect(Collectors.toList());
            appointRecordPageQuery.setResultIds(resultIds);
            insertAppointDB = CommonUtils.queryLargeRecordPageWrapper(appointRecordPageQuery, query -> { return appointMapper.selectAppointChannelCount(appointRecordPageQuery); }, query -> appointMapper.selectAppoint(appointRecordPageQuery));
//            insertAppointDB = statisSchAppointChannelDao.selectReleaseReport(resultIds, startDate, endDate);
        }
        if (CollectionUtils.isNotEmpty(updateDB)) {
            List<String> resultIds = updateDB.stream().map(ReportVo::getSchResultId).distinct().collect(Collectors.toList());
            appointRecordPageQuery.setResultIds(resultIds);
            updateAppointDB = CommonUtils.queryLargeRecordPageWrapper(appointRecordPageQuery, query -> appointMapper.selectAppointChannelCount(appointRecordPageQuery), query -> appointMapper.selectAppoint(appointRecordPageQuery));
//            updateAppointDB = statisSchAppointChannelDao.selectReleaseReport(resultIds, startDate, endDate);
        }
        if (CollectionUtils.isNotEmpty(insertAppointDB)) {
            //插入到数据库中 即插入到statis_sch_appoint表中
            try {
                List<List<ReportVo>> partition = Lists.partition(insertAppointDB, 400);
                for (List<ReportVo> list : partition) {
                    appointMapper.insertReport(list);
                }
            } catch (Exception e) {
                log.error("logId:{}, 插入统计报表数据异常, 异常信息为{}", logId, e);
            }
        }
        if (CollectionUtils.isNotEmpty(updateAppointDB)) {
            try {
                List<List<ReportVo>> partition = Lists.partition(updateAppointDB, 500);
                for (List<ReportVo> list : partition) {
                    appointMapper.updateReport(list);
                }
            } catch (Exception e) {
                log.error("logId:{}, 插入统计报表数据异常, 异常信息为{}", logId, e);
            }
        }
        long e = System.currentTimeMillis();
        System.out.println(e - s);
    }

    /**
     * @param reportOrderList 新增的数据  从order表中查询的预约挂号数据
     * @param reportChannelDB  数据库中的数据    两者对比是否有额外更新的数据
     * @return
     */
    private List<ReportVo> updateAppoChannel(List<ReportVo> reportOrderList, List<ReportVo> reportChannelDB) {
        //是否更新
        List<ReportVo> updateDB = null;
        if (CollectionUtils.isNotEmpty(reportChannelDB)) {
            Map<String, ReportVo> reportOrderMap = reportChannelDB.stream().collect(Collectors.toMap(ReportVo::getUniqueKey, Function.identity(), (k1, k2) -> k1));
            updateDB = reportOrderList.stream().filter(reportOrder -> {
                ReportVo reportVo = reportOrderMap.get(reportOrder.getUniqueKey());
                return reportVo != null;
            }).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(updateDB)) {
            try {
                List<List<ReportVo>> partition = Lists.partition(updateDB, 500);
                for (List<ReportVo> list : partition) {
                    appointMapper.updateReportChannel(list);
                }
            } catch (Exception e) {
                log.error("插入统计报表数据异常, 异常信息为{}", e);
            }
        }
        return updateDB;
    }

    /**
     * @param reportOrderList 新增的数据  从order表中查询的预约挂号数据
     * @param reportChannelDB  数据库中的数据    两者对比是否有额外新增的数据
     * @return
     */
    private List<ReportVo> insertAppoChannel(List<ReportVo> reportOrderList, List<ReportVo> reportChannelDB) {

        List<ReportVo> insertDB;
        if (CollectionUtils.isNotEmpty(reportChannelDB)) {
            List<String> collect = reportChannelDB.stream().map(ReportVo::getUniqueKey).distinct().collect(Collectors.toList());
            Map<String, ReportVo> reportOrderMap = reportChannelDB.stream().collect(Collectors.toMap(ReportVo::getUniqueKey, Function.identity(), (k1, k2) -> k1));
            insertDB = reportOrderList.stream().filter(reportVo -> {
                boolean isExist = reportChannelDB.stream().anyMatch(vo -> vo.getChannelType().equals(reportVo.getChannelType()) && vo.getSchResultId().equals(reportVo.getSchResultId()));
                return !isExist;
            }).collect(Collectors.toList());
            insertDB = reportOrderList.stream().filter(reportOrder -> {
                ReportVo reportVo = reportOrderMap.get(reportOrder.getUniqueKey());
                return reportVo == null;
            }).collect(Collectors.toList());
        }else {
            insertDB = reportOrderList;
        }

        if (CollectionUtils.isNotEmpty(insertDB)) {
            try {
                List<List<ReportVo>> partition = Lists.partition(insertDB, 500);
                for (List<ReportVo> list : partition) {
                    appointMapper.insertReportChannel(list);
                }
            } catch (Exception e) {
                log.error("插入统计报表数据异常, 异常信息为{}", e);
            }
        }
        return insertDB;
    }

    private List<ReportVo> filterReportChannel(List<ReportVo> reportOrder,Date startDate, Date endDate) {

        if (CollectionUtils.isNotEmpty(reportOrder)) {
            //过滤掉排班结果id为null 或者 渠道为空的数据
            reportOrder = reportOrder.stream().filter(reportVo -> reportVo.getSchResultId() != null && reportVo.getChannelType() != null).collect(Collectors.toList());
            // 只留下 统一排班 和 双号源模式 的数据
            List<String> sysCorpList = appointMapper.selectPlanDualCorp();
            reportOrder = reportOrder.stream().filter(reportVo -> sysCorpList.contains(reportVo.getCorpCode())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(reportOrder)) {
                log.info("不统计网关模式医院的数据，开始时间{},结束时间{}", startDate, endDate);
                return null;
            }
        }

        // 渠道表和结果表连表查询  统计出各个排班结果对应的渠道的数据(注: 在sql中判断是否为0)
        StatisReportRecordPageQuery statisReportRecordPageQuery = new StatisReportRecordPageQuery();
        statisReportRecordPageQuery.setStartDate(startDate);
        statisReportRecordPageQuery.setEndDate(endDate);
        List<ReportVo> resultChannelNums = CommonUtils.queryLargeRecordPageWrapper(statisReportRecordPageQuery, query -> appointMapper.selectCountByLargeRecordPage(statisReportRecordPageQuery), query -> appointMapper.selectChannelNumByPageQuery(statisReportRecordPageQuery));
        if (CollectionUtils.isEmpty(resultChannelNums)) {
            log.error("不统计网关模式医院的数据，开始时间{},结束时间{}", startDate, endDate);
            return null;
        }
        resultChannelNums = resultChannelNums.stream().filter(reportVo -> reportVo.getSchResultId() != null && reportVo.getChannelType() != null).collect(Collectors.toList());


        //筛选出 有预约的排班结果
        Map<String, ReportVo> reportOrderMap = reportOrder.stream().collect(Collectors.toMap(ReportVo::getUniqueKey, Function.identity(), (k1, k2) -> k1));
        List<ReportVo> hasOrderSch = resultChannelNums.stream().filter(reportVo ->reportOrderMap.containsKey(reportVo.getUniqueKey())).collect(Collectors.toList());
        // (有些排班结果可能没有预约)
        List<ReportVo> noOrderSch = resultChannelNums.stream().filter(reportVo ->!reportOrderMap.containsKey(reportVo.getUniqueKey())).collect(Collectors.toList());

        //对有预约的排班结果的数据进行补充--补充渠道数据
        if (CollectionUtils.isNotEmpty(hasOrderSch)) {
            Map<String, ReportVo> hasOrderMap = hasOrderSch.stream().collect(Collectors.toMap(ReportVo::getUniqueKey, Function.identity(), (k1, k2) -> k1));
            reportOrder.forEach(order -> {
                ReportVo hasOrder = hasOrderMap.get(order.getUniqueKey());
                if (hasOrder != null) {
                    order.setTotalNum(hasOrder.getTotalNum());
                }
            });
        }


        List<ReportVo> finalInsertReport = new ArrayList<>();
        finalInsertReport.addAll(reportOrder);
        finalInsertReport.addAll(noOrderSch);

        return finalInsertReport;
    }


}
