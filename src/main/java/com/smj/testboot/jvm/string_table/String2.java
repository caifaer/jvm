package com.smj.testboot.jvm.string_table;

public class String2 {

    public static void main(String[] args) {
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
    }

    // 常量池中的信息，都会被加载到运行时常量池中， 这时 a b ab 都是常量池中的符号，还没有变为 java 字符串对象
    // 是一种懒加载的思想  加载的时候才放到串池中
    // ldc #2 会把 a 符号变为 "a" 字符串对象   然后放到 stringTable中
    // ldc #3 会把 b 符号变为 "b" 字符串对象
    // ldc #4 会把 ab 符号变为 "ab" 字符串对象

}
