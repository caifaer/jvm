package com.smj.testboot.jvm.oom;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MetaspaceOom {



    // 模拟 元空间 溢出  打印gc日志 以及  在溢出时生成快照。
    // -XX:+UseParNewGC
    // -XX:+UseConcMarkSweepGC
    // -XX:MetaspaceSize=10m
    // -XX:MaxMetaspaceSize=10m
    // -XX:+PrintGCDetails
    // -Xloggc:gc.log
    // -XX:+HeapDumpOnOutOfMemoryError
    // -XX:HeapDumpPath=./

    public static void main(String[] args){

        long counter = 0;

        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Car.class);

            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                                     public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                                         if (method.getName().equals("run")) {
                                             System.out.println("启动汽车之间，先进行自动的安全检查......");
                                             return methodProxy.invokeSuper(o, objects);
                                         } else {
                                             return methodProxy.invokeSuper(o, objects);
                                         }
                                     }
                                 }

            );
            Car car = (Car) enhancer.create();
            car.run();

            System.out.println("目前创建了"+(++counter)+"个Car类的子类了");

        }

    }

    static class Car {
        public void run() {
            System.out.println("汽车启动，开始行使......");
        }
    }

    static class safeCar extends Car {
        @Override
        public void run() {
            System.out.println("汽车启动,开始行使......");super.run();
        }
    }

}
