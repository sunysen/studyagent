package com.blueware.agent;

public class TestTime {

    public static void testTime() throws InterruptedException {
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(2000);
        TestTime.testTime();
    }

}
