/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.DoubleSetting;

@Hack.Registration(name="Camera Clip", description="f5 mode", category=Hack.Category.RENDER, isListening=false)
public class CameraClip
extends Hack {
    public static CameraClip INSTANCE;
    public DoubleSetting distance = new DoubleSetting("Distance", (Double)10.0, (Double)-10.0, (Double)50.0, this);

    public CameraClip() {
        INSTANCE = this;
    }
}

