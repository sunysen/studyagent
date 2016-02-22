package com.blueware.agent;


public class TimeUtil {
    private static long startTime;
    private static long endTime;

    private TimeUtil() {
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static long getEndTime() {
        return endTime;
    }

    public static void setEndTime() {
        endTime = System.currentTimeMillis();
    }

    public static long getExclusiveTime(String className, String methodName) {
        long exclusive = getEndTime() - getStartTime();
        System.out.println(className.replace("/", ".") + "." + methodName + " exclusive:" + exclusive);
        return exclusive;
    }
}
