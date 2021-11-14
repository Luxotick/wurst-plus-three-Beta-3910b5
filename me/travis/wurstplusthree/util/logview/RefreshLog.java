/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.logview;

import me.travis.wurstplusthree.util.logview.Gui;

class RefreshLog
extends Thread {
    public Gui INSTANCE;
    public boolean running;

    public RefreshLog(Gui instance) {
        this.INSTANCE = instance;
        this.running = true;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while (this.running) {
            if (System.currentTimeMillis() - time != 500L) continue;
            time = System.currentTimeMillis();
            for (String l : this.INSTANCE.getLog()) {
                if (this.INSTANCE.log.contains(l)) continue;
                this.INSTANCE.log.add(l);
                this.INSTANCE.display.append(l + "\n");
            }
        }
    }

    public void stop(boolean stop) {
        this.running = stop;
    }
}

