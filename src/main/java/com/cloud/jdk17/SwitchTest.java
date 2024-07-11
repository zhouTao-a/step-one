package com.cloud.jdk17;import static java.lang.System.*;/** * Switch模式匹配（预览） */public class SwitchTest {    /**     * JDK12简化了写法     */    public static void JDK12_switch(String day) {        switch (day) {            case "MONDAY" -> out.println(1);            case "TUESDAY" -> out.println(2);            case "WEDNESDAY" -> out.println(3);            case "THURSDAY" -> out.println(4);            case "FRIDAY" -> out.println(5);            case "SATURDAY" -> out.println(6);            case "SUNDAY" -> out.println(7);            default -> out.println(0);        }    }    /**     * JDK13在简化的基础上增加了返回值     */    public static int JDK13_switch(String day) {        return switch (day) {            case "MONDAY" -> 1;            case "TUESDAY" -> 2;            case "WEDNESDAY" -> 3;            case "THURSDAY" -> 4;            case "FRIDAY" -> 5;            case "SATURDAY" -> 6;            case "SUNDAY" -> 7;            default -> 0;        };    }    /**     * JDK17之后，switch开始支持instanceof(未正式上线)     * mvn test异常     *///    public static String JDK17_instanceof_switch(Object o) {//        return switch (o) {//            case Integer i -> "Integer:" + i;//            case Long l -> "Long:" + l;//            case Double d -> "Double:" + d;//            case String s -> "String:" + s;//            default -> "UNKNOWN";//        };//    }}