package com.cloud.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) throws Exception {
        //指定初始容量15来创建一个HashMap
        HashMap m = new HashMap(15);
        //获取HashMap整个类
        Class<?> mapType = m.getClass();
        //获取指定属性，也可以调用getDeclaredFields()方法获取属性数组
        Field threshold =  mapType.getDeclaredField("threshold");
        //将目标属性设置为可以访问
        threshold.setAccessible(true);
        //获取指定方法，因为HashMap没有容量这个属性，但是capacity方法会返回容量值
        Method capacity = mapType.getDeclaredMethod("capacity");
        //设置目标方法为可访问
        capacity.setAccessible(true);
        //打印刚初始化的HashMap的容量、阈值和元素数量
        System.out.println("容量："+capacity.invoke(m)+"    阈值："+threshold.get(m)+"    元素数量："+m.size());
        for (int i = 0;i<17;i++){
            m.put(i,i);
            //动态监测HashMap的容量、阈值和元素数量
            System.out.println("容量："+capacity.invoke(m)+"    阈值："+threshold.get(m)+"    元素数量："+m.size());
        }
    }
}
