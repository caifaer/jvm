package com.smj.testboot.jvm.four_reference;

import java.util.ArrayList;
import java.util.List;

public class SoftReference {


    private static final int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) {
        // list --> SoftReference --> byte[]  通过soft来间接使用
        List<java.lang.ref.SoftReference<byte[]>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            java.lang.ref.SoftReference<byte[]> ref = new java.lang.ref.SoftReference<>(new byte[_4MB]);
            System.out.println(ref.get());
            list.add(ref);
            System.out.println(list.size());

        }
        System.out.println("循环结束：" + list.size());
        for (java.lang.ref.SoftReference<byte[]> ref : list) {
            System.out.println(ref.get());  // 在这里就可以看到  放内存不足的时候  对象就会被回收
        }

    }
}
