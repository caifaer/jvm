package com.smj.testboot.service.impl;


import java.util.Date;


public interface StatisReportService {

    void generateStatisReport(String logId, Date startDate, Date endDate);

}
