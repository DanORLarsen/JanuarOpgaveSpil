package com.dan.GameApp1D;

public class MyTimerDan {
    private static long startTime;

    public static void start()
    {
        startTime = System.currentTimeMillis();
        System.out.println("Timer start");
    }

    public static void stop()
    {
        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime-startTime);
        System.out.println("Time elapsed: " + (elapsedTime/1000) + "S, and " + elapsedTime%1000 + "MS");

    }

    public static String getStringScore()
    {long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime-startTime);
         String d = "It took: " + (elapsedTime/1000) + " Seconds and  " + elapsedTime%1000 + " Milliseconds  to win";
         return d;
    }
}