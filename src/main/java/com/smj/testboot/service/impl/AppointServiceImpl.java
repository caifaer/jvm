package com.smj.testboot.service.impl;

import com.google.common.collect.Lists;
import com.smj.testboot.bean.LargeRecordPageQuery;
import com.smj.testboot.bean.ReportVo;
import com.smj.testboot.common.CommonUtils;
import com.smj.testboot.common.StatisReportRecordPageQuery;
import com.smj.testboot.mapper.AppointMapper;
import com.smj.testboot.service.AppointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AppointServiceImpl implements AppointService {

    @Resource
    private AppointMapper appointMapper;

    @Override
    public void update(Date startDate, Date endDate) {

        List<ReportVo> insertDB = appointMapper.selectAppointChannelInfoByDate(startDate, endDate);

        List<String> collect = insertDB.stream().map(ReportVo::getUniqueKey).distinct().collect(Collectors.toList());

        Map<String, ReportVo> reportOrderMap = insertDB.stream().collect(Collectors.toMap(ReportVo::getUniqueKey, Function.identity(), (k1, k2) -> k1));




        System.out.println(reportOrderMap);



    }

}
