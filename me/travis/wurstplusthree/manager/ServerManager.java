/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package me.travis.wurstplusthree.manager;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.util.math.MathHelper;

@Hack.Registration(name="ServerManager", description="Manages Server", category=Hack.Category.HIDDEN, isListening=true)
public class ServerManager
extends Hack {
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("##.00#");
    private final Timer timer = new Timer();
    private float TPS = 20.0f;
    private long lastUpdate = -1L;
    private String serverBrand = "";
    private final float[] tickRates = new float[20];
    private int nextIndex = 0;

    public ServerManager() {
        Arrays.fill(this.tickRates, 0.0f);
    }

    public void onPacketReceived() {
        this.timer.reset();
    }

    public boolean isServerNotResponding() {
        return this.timer.passedMs(1500L);
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }

    public void update() {
        double d;
        float f;
        long currentTime = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
            return;
        }
        float timeElapsed = (float)(currentTime - this.lastUpdate) / 1000.0f;
        this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp((float)(20.0f / timeElapsed), (float)0.0f, (float)20.0f);
        ++this.nextIndex;
        long timeDiff = currentTime - this.lastUpdate;
        float tickTime = (float)timeDiff / 20.0f;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        float tps = 1000.0f / tickTime;
        if (f > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f2 : this.tpsCounts) {
            total += (double)f2;
        }
        total /= (double)this.tpsCounts.length;
        if (d > 20.0) {
            total = 20.0;
        }
        this.TPS = Float.parseFloat(this.format.format(total));
        this.lastUpdate = currentTime;
    }

    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }

    public float getTpsFactor() {
        return 20.0f / this.TPS;
    }

    public float getTPS() {
        return this.TPS;
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    public int getPing() {
        if (this.nullCheck()) {
            return 0;
        }
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        }
        catch (Exception e) {
            return 0;
        }
    }

    public float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : this.tickRates) {
            if (!(tickRate > 0.0f)) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return MathHelper.clamp((float)(sumTickRates / numTicks), (float)0.0f, (float)20.0f);
    }
}

