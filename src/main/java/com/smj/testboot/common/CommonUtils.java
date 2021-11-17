package com.smj.testboot.common;

import com.google.common.collect.Lists;
import com.smj.testboot.bean.LargeRecordPageQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public class CommonUtils {



    public static <T> List<T> queryLargeRecordPageWrapper(LargeRecordPageQuery largeRecordPageQuery, ToLongFunction<LargeRecordPageQuery> countFunction, Function<LargeRecordPageQuery, List<T>> function) {
        if (Objects.isNull(largeRecordPageQuery)) {
            return Lists.newArrayList();
        }
        Long total = countFunction.applyAsLong(largeRecordPageQuery);
        if (total < LargeRecordPageConsts.SQL_QUERY_LIMIT) {
            largeRecordPageQuery.setEnabled(Boolean.FALSE);
            return function.apply(largeRecordPageQuery);
        }
        largeRecordPageQuery.setEnabled(Boolean.TRUE);
        largeRecordPageQuery.setPageSize(LargeRecordPageConsts.SQL_QUERY_PAGESIZE);
        long totalPage = total / LargeRecordPageConsts.SQL_QUERY_PAGESIZE + ((total % LargeRecordPageConsts.SQL_QUERY_PAGESIZE == 0) ? 0 : 1);
        List<T> records = new ArrayList<>(10240);
        for (int i = 0; i < totalPage; i++) {
            largeRecordPageQuery.setStartRow(i*LargeRecordPageConsts.SQL_QUERY_PAGESIZE);
            records.addAll(function.apply(largeRecordPageQuery));
        }
        return records;
    }
}
