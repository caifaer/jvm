package com.smj.testboot.jvm.oom;

public class StackOom {



    // 模拟栈溢出
    // 参数
    // -XX:ThreadStackSize=1m
    //-XX:+PrintGCDetails
    //-Xloggc:gc.log
    //-XX:+HeapDumpOnOutOfMemoryError
    //-XX:HeapDumpPath=./
    //-XX:+UseParNewGC
    //-XX:+UseConcMarkSweepGC
    public static long counter = 0;
    public static void main(String[]args){
        work();
    }
    public static void work (){
              System.out.println("目前是第"+(++counter)+"次调用方法");
              work();
    }


}
