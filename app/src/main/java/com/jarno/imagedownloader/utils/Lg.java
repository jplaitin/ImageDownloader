package com.jarno.imagedownloader.utils;

import android.util.Log;

/**
 * Created by jarnolaitinen on 05/11/15.
 */
public class Lg {
    public static String TAG = "debug";
    public static boolean DEBUG = true;
    public static boolean SHOW_LINK = true;

    public static void i(){ //cant override these since stack trace get messed up
        Lg.i(Lg.TAG, "", 5);
    }
    public static void i(String message){
        Lg.i(Lg.TAG, message, 5);
    }
    public static void i(String tag, String message){
        Lg.i(tag, message, 5);
    }
    private static void i(String tag, String message, int backInd){
        if(DEBUG) {
            if(SHOW_LINK){
                Log.i(tag, getStackTraceInfo(backInd) + message + getStackTraceLink(backInd));
            }
            else {
                Log.e(tag, getStackTraceInfo(backInd) + message);
            }
        }
    }

    public static void d(){ //cant override these since stack trace get messed up
        Lg.d(Lg.TAG, "", 5);
    }
    public static void d(String message){
        Lg.d(Lg.TAG, message, 5);
    }
    public static void d(String tag, String message){
        Lg.d(tag, message, 5);
    }
    private static void d(String tag, String message, int backInd){
        if(DEBUG) {
            if(SHOW_LINK){
                Log.d(tag, getStackTraceInfo(backInd) + message + getStackTraceLink(backInd));
            }
            else {
                Log.e(tag, getStackTraceInfo(backInd) + message);
            }
        }
    }

    public static void w(){ //cant override these since stack trace get messed up
        Lg.w(Lg.TAG, "", 5);
    }
    public static void w(String message){
        Lg.w(Lg.TAG, message, 5);
    }
    public static void w(String tag, String message){
        Lg.w(tag, message, 5);
    }
    private static void w(String tag, String message, int backInd){
        if(DEBUG) {
            if(SHOW_LINK){
                Log.w(tag, getStackTraceInfo(backInd) + message + getStackTraceLink(backInd));
            }
            else {
                Log.e(tag, getStackTraceInfo(backInd) + message);
            }
        }
    }


    public static void e(){ //cant override these since stack trace get messed up
        Lg.e(Lg.TAG, "", 5);
    }
    public static void e(String message){
        Lg.e(Lg.TAG, message, 5);
    }
    public static void e(String tag, String message){
        Lg.e(tag, message, 5);
    }
    private static void e(String tag, String message, int backInd){
        if(DEBUG) {
            if(SHOW_LINK){
                Log.e(tag, getStackTraceInfo(backInd) + message + getStackTraceLink(backInd));
            }
            else {
                Log.e(tag, getStackTraceInfo(backInd) + message);
            }
        }
    }


    public static String getStackTraceInfo(int backIndex) {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        String fullClassName = Thread.currentThread().getStackTrace()[backIndex].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[backIndex].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[backIndex].getLineNumber();

        return className + "." + methodName + ":" + lineNumber +": ";
    }

    public static String getStackTraceLink(int backIndex) {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();
        String fullClassName = Thread.currentThread().getStackTrace()[backIndex].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[backIndex].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[backIndex].getLineNumber();
        return "("+ className + ".java:" + lineNumber + ")";
    }


}
