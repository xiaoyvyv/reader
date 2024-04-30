package com.xiaoyv.comic.datasource.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class TempHolder {
    public static final ReentrantLock lock = new ReentrantLock();

    public static TempHolder inst = new TempHolder();
    public static volatile int listHash = 0;

    public static volatile int listHashTemp;
    public static String loadingTime;

    public static boolean isListHashChange() {
        if (listHash == listHashTemp) {
            return false;
        }
        listHashTemp = listHash;
        return true;
    }

    public static volatile boolean isSeaching = false;
    public static volatile boolean isConverting = false;
    public static volatile boolean isRecordTTS = false;

    public static int isRecordFrom = 1;
    public static int isRecordTo = 1;

    public static volatile AtomicBoolean isActiveSpeedRead = new AtomicBoolean(false);
    public String login = "", password = "";
    public int linkPage = -1;

    public long timerFinishTime = 0;
    public int pageDelta = 0;

    public volatile boolean loadingCancelled = false;
    public boolean forseAppLang = false;

    public volatile long lastRecycledDocument = 0;

    public int textFromPage = 0;
    public String copyFromPath = null;

    public int documentTitleBarHeight;

    public static TempHolder get() {
        return inst;
    }


}
