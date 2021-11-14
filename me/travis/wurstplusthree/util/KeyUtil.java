/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

public class KeyUtil
extends Thread {
    public static List<Integer> keys_;

    public static void clip(List<Integer> keys) {
        keys_ = keys;
        KeyUtil obj = new KeyUtil();
        Thread thread = new Thread(obj);
        thread.start();
    }

    @Override
    public void run() {
        Robot r = null;
        try {
            r = new Robot();
        }
        catch (AWTException e) {
            e.printStackTrace();
            return;
        }
        for (Integer integer : keys_) {
            r.keyPress(integer);
        }
        try {
            Thread.sleep(100L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Integer integer : keys_) {
            r.keyRelease(integer);
        }
    }
}

