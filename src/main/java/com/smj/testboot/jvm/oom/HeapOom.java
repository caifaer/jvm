package com.smj.testboot.jvm.oom;

import java.util.ArrayList;
import java.util.List;

public class HeapOom {



    // 模拟 OOM -- 即堆溢出
    // -Xms10m
    //-Xmx10m
    //-XX:+PrintGCDetails
    //-Xloggc:gc.log
    //-XX:+HeapDumpOnOutOfMemoryError
    //-XX:HeapDumpPath=./
    //-XX:+UseParNewGC
    //-XX:+UseConcMarkSweepGC
    public static void main(String[] args){
        long counter = 0;
        List<Object> list = new ArrayList<Object>();
        while (true) {
            list.add(new Object());
            System.out.println("当前创建了第" + (++counter) + "个对象");
        }
        // 。。。。。
        // 当前创建了第360143个对象
        //当前创建了第360144个对象
        //当前创建了第360145个对象
        // Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
    }
}
