package com.smj.testboot.jvm.string_table;


// StringTable [ "a", "b" ,"ab" ]  hashtable 结构，不能扩容
public class StringTableTest {


    public static void main(String[] args) {
        String s1 = "a"; // 懒惰的  只有执行的时候 才会放到字符串常量池   没有的放到里面  有的话就不放了
        String s2 = "b";
        String s3 = "ab";
        String s4 = s1 + s2; // 翻译成字节码 --- new StringBuilder().append("a").append("b").toString() --查看toString源码  就是 new String("ab")
        String s5 = "a" + "b";  // javac 在编译期间的优化，因为编译的时候 都是常量  所以结果已经在编译期确定为ab  此时就直接去常量池中确定了  变量的话就是上面这种

        System.out.println(s3 == s5);

    }

      /*
    1. 常量池中的字符串仅是符号，第一次用到时才变为对象
    2. 利用串池的机制，来避免重复创建字符串对象
    3. 字符串 变量 拼接的原理是 StringBuilder （1.8）
    4. 字符串 常量 拼接的原理是编译期优化
    5. 可以使用 intern 方法，主动将串池中还没有的字符串对象放入串池
        5.1 1.8 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有则放入串池， 会把串池中的对象返回
        5.2 1.6 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有会把此对象复制一份，放入串池， 会把串池中的对象返回
    */
    //  ["ab", "a", "b"]
    public static void intern() {

        String x = "ab";
        String s = new String("a") + new String("b");
        // a  b 在池子中 也在堆中  但是ab 只能在堆中  因为是动态拼接的
        // 此时可以调用 intern方法 将对象 ab放到 池子中  有则不会放入  没有就放里面再返回串池中的对象

        // 堆  new String("a")   new String("b") new String("ab")
        String s2 = s.intern(); // 将这个字符串对象尝试放入串池，如果有则并不会放入，如果没有则放入串池， 会把串池中的对象返回

        System.out.println( s2 == x); // true
        System.out.println( s == x ); // false   s还是堆中的对象  x在常量池中
    }


}
