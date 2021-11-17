package com.smj.testboot.mapper;


import com.smj.testboot.bean.LargeRecordPageQuery;
import com.smj.testboot.bean.ReportVo;
import com.smj.testboot.common.StatisReportRecordPageQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointMapper {

    List<ReportVo> selectAppointChannelInfoByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<ReportVo> select(@Param("list") LargeRecordPageQuery largeRecordPageQuery);

    List<ReportVo> selectAppointInfoByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    void updateAppointInfo(@Param("list") List<ReportVo> reportVos);

    void updateAppointChannelInfo(@Param("list") List<ReportVo> reportVos);


    void updateReport(@Param("list") List<ReportVo> reportVos);

    void insertReport(@Param("list") List<ReportVo> reportVos);

    long selectAppointChannelCount(@Param("appoint")StatisReportRecordPageQuery statisReportRecordPageQuery);

    List<ReportVo> selectAppoint(@Param("appoint")StatisReportRecordPageQuery statisReportRecordPageQuery);



    long selectOrderCountByLargeRecordPage(StatisReportRecordPageQuery statisReportRecordPageQuery);

    List<ReportVo> selectOrderNumByPageQuery(StatisReportRecordPageQuery statisReportRecordPageQuery);

    long selectDaysCountByLargeRecordPage(StatisReportRecordPageQuery statisReportRecordPageQuery);

    List<ReportVo> selectDaysByPageQuery(StatisReportRecordPageQuery statisReportRecordPageQuery);

    int insertReportChannel(@Param("list")List<ReportVo> reportVos);

    int updateReportChannel(List<ReportVo> updateDB);

    List<String> selectPlanDualCorp();

    int selectCountByLargeRecordPage(StatisReportRecordPageQuery statisReportRecordPageQuery);

    List<ReportVo> selectChannelNumByPageQuery(StatisReportRecordPageQuery statisReportRecordPageQuery);


    List<ReportVo> selectOneDay(Date startDate, Date endDate);
}
