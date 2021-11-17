package com.smj.testboot.common;

import com.smj.testboot.bean.LargeRecordPageQuery;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Data
public class StatisReportRecordPageQuery extends LargeRecordPageQuery {

    private List<String> resultIds;

    private Date startDate;

    private Date endDate;

}
