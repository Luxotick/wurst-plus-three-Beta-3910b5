/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.event.events.RenderItemEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;

@Hack.Registration(name="View Model", description="makes you hand look cool", category=Hack.Category.RENDER)
public class ViewModel
extends Hack {
    public static ViewModel INSTANCE;
    public BooleanSetting fixEating = new BooleanSetting("Fix Eating", (Boolean)true, (Hack)this);
    DoubleSetting mainX = new DoubleSetting("mainX", (Double)1.2, (Double)0.0, (Double)6.0, this);
    DoubleSetting mainY = new DoubleSetting("mainY", (Double)-0.95, (Double)-3.0, (Double)3.0, this);
    DoubleSetting mainZ = new DoubleSetting("mainZ", (Double)-1.45, (Double)-5.0, (Double)5.0, this);
    DoubleSetting offX = new DoubleSetting("offX", (Double)-1.2, (Double)-6.0, (Double)0.0, this);
    DoubleSetting offY = new DoubleSetting("offY", (Double)-0.95, (Double)-3.0, (Double)3.0, this);
    DoubleSetting offZ = new DoubleSetting("offZ", (Double)-1.45, (Double)-5.0, (Double)5.0, this);
    DoubleSetting mainAngel = new DoubleSetting("mainAngle", (Double)0.0, (Double)0.0, (Double)360.0, this);
    DoubleSetting mainRx = new DoubleSetting("mainRotationPointX", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting mainRy = new DoubleSetting("mainRotationPointY", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting mainRz = new DoubleSetting("mainRotationPointZ", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting offAngel = new DoubleSetting("offAngle", (Double)0.0, (Double)0.0, (Double)360.0, this);
    DoubleSetting offRx = new DoubleSetting("offRotationPointX", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting offRy = new DoubleSetting("offRotationPointY", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting offRz = new DoubleSetting("offRotationPointZ", (Double)0.0, (Double)-1.0, (Double)1.0, this);
    DoubleSetting mainScaleX = new DoubleSetting("mainScaleX", (Double)1.0, (Double)-5.0, (Double)10.0, this);
    DoubleSetting mainScaleY = new DoubleSetting("mainScaleY", (Double)1.0, (Double)-5.0, (Double)10.0, this);
    DoubleSetting mainScaleZ = new DoubleSetting("mainScaleZ", (Double)1.0, (Double)-5.0, (Double)10.0, this);
    DoubleSetting offScaleX = new DoubleSetting("offScaleX", (Double)1.0, (Double)-5.0, (Double)10.0, this);
    DoubleSetting offScaleY = new DoubleSetting("offScaleY", (Double)1.0, (Double)-5.0, (Double)10.0, this);
    DoubleSetting offScaleZ = new DoubleSetting("offScaleZ", (Double)1.0, (Double)-5.0, (Double)10.0, this);

    public ViewModel() {
        INSTANCE = this;
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onItemRender(RenderItemEvent event) {
        if (this.nullCheck()) {
            return;
        }
        event.setMainX(this.mainX.getValue());
        event.setMainY(this.mainY.getValue());
        event.setMainZ(this.mainZ.getValue());
        event.setOffX(this.offX.getValue());
        event.setOffY(this.offY.getValue());
        event.setOffZ(this.offZ.getValue());
        event.setMainRAngel(this.mainAngel.getValue());
        event.setMainRx(this.mainRx.getValue());
        event.setMainRy(this.mainRy.getValue());
        event.setMainRz(this.mainRz.getValue());
        event.setOffRAngel(this.offAngel.getValue());
        event.setOffRx(this.offRx.getValue());
        event.setOffRy(this.offRy.getValue());
        event.setOffRz(this.offRz.getValue());
        event.setMainHandScaleX(this.mainScaleX.getValue());
        event.setMainHandScaleY(this.mainScaleY.getValue());
        event.setMainHandScaleZ(this.mainScaleZ.getValue());
        event.setOffHandScaleX(this.offScaleX.getValue());
        event.setOffHandScaleY(this.offScaleY.getValue());
        event.setOffHandScaleZ(this.offScaleZ.getValue());
    }
}

