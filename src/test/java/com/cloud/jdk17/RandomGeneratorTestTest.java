package com.cloud.jdk17;import org.junit.jupiter.api.Test;import java.util.List;import java.util.random.RandomGenerator;import java.util.random.RandomGeneratorFactory;import static org.junit.jupiter.api.Assertions.*;class RandomGeneratorTestTest {    @Test    void test() {        RandomGeneratorTest.test();    }    @Test    void all() {        List<RandomGeneratorFactory<RandomGenerator>> all = RandomGeneratorTest.all();        assertEquals(13, all.size());        all.forEach(e -> System.out.println(e.group() + "-" + e.name()));    }}