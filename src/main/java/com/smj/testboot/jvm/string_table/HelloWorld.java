package com.smj.testboot.jvm.string_table;

public class HelloWorld {

    // 使用 javap -v HelloWorld.class  反编译成二进制字节码文件
    // 包括了类的基本信息 常量池 类的方法定义(虚拟机指令)
    public static void main(String[] args) {
        System.out.println("hello world");
    }


    /**
     * 常量池，就是一张表，虚拟机指令根据这张常量表找到要执行的类名、方法名、参数类型、字面量等信息
     *
     * 运行时常量池，常量池是 *.class 文件中的，当该类被加载，它的常量池信息就会放入运行时常量池，并把里面的 符号地址(比如#2这些符号地址)变为真实地址
     */

    /**
     *   Last modified 2021-11-21; size 631 bytes
     *   MD5 checksum 9c14908754f06ea3e2ca879877e805bd
     *   Compiled from "HelloWorld.java"
     *
     *
     * public class com.smj.testboot.jvm.string_table.HelloWorld
     *   minor version: 0
     *   major version: 52
     *   flags: ACC_PUBLIC, ACC_SUPER
     *
     *
     *
     * Constant pool:
     *    #1 = Methodref          #6.#21         // java/lang/Object."<init>":()V
     *    #2 = Fieldref           #22.#23        // java/lang/System.out:Ljava/io/PrintStream;
     *    #3 = String             #24            // hello world
     *    #4 = Methodref          #25.#26        // java/io/PrintStream.println:(Ljava/lang/String;)V
     *    #5 = Class              #27            // com/smj/testboot/jvm/string_table/HelloWorld
     *    #6 = Class              #28            // java/lang/Object
     *    #7 = Utf8               <init>
     *    #8 = Utf8               ()V
     *    #9 = Utf8               Code
     *   #10 = Utf8               LineNumberTable
     *   #11 = Utf8               LocalVariableTable
     *   #12 = Utf8               this
     *   #13 = Utf8               Lcom/smj/testboot/jvm/string_table/HelloWorld;
     *   #14 = Utf8               main
     *   #15 = Utf8               ([Ljava/lang/String;)V
     *   #16 = Utf8               args
     *   #17 = Utf8               [Ljava/lang/String;
     *   #18 = Utf8               MethodParameters
     *   #19 = Utf8               SourceFile
     *   #20 = Utf8               HelloWorld.java
     *   #21 = NameAndType        #7:#8          // "<init>":()V
     *   #22 = Class              #29            // java/lang/System
     *   #23 = NameAndType        #30:#31        // out:Ljava/io/PrintStream;
     *   #24 = Utf8               hello world
     *   #25 = Class              #32            // java/io/PrintStream
     *   #26 = NameAndType        #33:#34        // println:(Ljava/lang/String;)V
     *   #27 = Utf8               com/smj/testboot/jvm/string_table/HelloWorld
     *   #28 = Utf8               java/lang/Object
     *   #29 = Utf8               java/lang/System
     *   #30 = Utf8               out
     *   #31 = Utf8               Ljava/io/PrintStream;
     *   #32 = Utf8               java/io/PrintStream
     *   #33 = Utf8               println
     *   #34 = Utf8               (Ljava/lang/String;)V
     * {
     *   public com.smj.testboot.jvm.string_table.HelloWorld();    构造方法
     *     descriptor: ()V
     *     flags: ACC_PUBLIC
     *     Code:
     *       stack=1, locals=1, args_size=1
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     *       LineNumberTable:
     *         line 3: 0
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       5     0  this   Lcom/smj/testboot/jvm/string_table/HelloWorld;
     *
     *
     * 类的方法定义  包含了虚拟机指令    0  3 5 8 就是虚拟机指令
     *   public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=2, locals=1, args_size=1
     *          0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;   #2 其实就是去常量池中 查表
     *          3: ldc           #3                  // String hello world
     *          5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *          8: return
     *       LineNumberTable:
     *         line 6: 0
     *         line 7: 8
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       9     0  args   [Ljava/lang/String;
     *     MethodParameters:
     *       Name                           Flags
     *       args
     * }
     * SourceFile: "HelloWorld.java"
     *
     *
     *
     *
     *
     *
     */
}
