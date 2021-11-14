/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.logview;

import me.travis.wurstplusthree.util.logview.Gui;
import me.travis.wurstplusthree.util.logview.RefreshLog;

public class Threads
extends Thread {
    @Override
    public void run() {
        Gui gui = new Gui();
        RefreshLog refreshLog = new RefreshLog(gui);
        refreshLog.start();
        gui.setThead(refreshLog);
    }
}

