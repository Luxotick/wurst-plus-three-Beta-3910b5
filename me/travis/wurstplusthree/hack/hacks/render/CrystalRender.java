/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.RenderEntityModelEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.lwjgl.opengl.GL11;

@Hack.Registration(name="Crystal Render", description="changes how crystal renders", category=Hack.Category.RENDER, isListening=false)
public class CrystalRender
extends Hack {
    public static CrystalRender INSTANCE;
    public BooleanSetting chams = new BooleanSetting("Chams", (Boolean)false, (Hack)this);
    public BooleanSetting glint = new BooleanSetting("Glint", (Boolean)false, (Hack)this);
    public BooleanSetting effect = new BooleanSetting("Effect", (Boolean)false, (Hack)this);
    public BooleanSetting wireframe = new BooleanSetting("Wireframe", (Boolean)false, (Hack)this);
    public BooleanSetting throughwalls = new BooleanSetting("Walls", (Boolean)false, (Hack)this);
    public BooleanSetting xqz = new BooleanSetting("XQZ", (Boolean)false, (Hack)this);
    public ColourSetting colour = new ColourSetting("Colour", new Colour(255, 255, 255, 150), (Hack)this);
    public ColourSetting wireColour = new ColourSetting("WireframeColour", new Colour(0, 0, 0, 150), (Hack)this);
    public ColourSetting hiddenColour = new ColourSetting("HiddenColour", new Colour(255, 255, 255, 150), (Hack)this);
    public DoubleSetting width = new DoubleSetting("Width", (Double)3.0, (Double)0.1, (Double)5.0, this);
    public DoubleSetting scale = new DoubleSetting("Scale", (Double)1.0, (Double)0.1, (Double)3.0, this);
    public Map<EntityEnderCrystal, Float> scaleMap = new ConcurrentHashMap<EntityEnderCrystal, Float>();

    public CrystalRender() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        for (Entity crystal : CrystalRender.mc.world.loadedEntityList) {
            if (!(crystal instanceof EntityEnderCrystal)) continue;
            if (!this.scaleMap.containsKey((Object)crystal)) {
                this.scaleMap.put((EntityEnderCrystal)crystal, Float.valueOf(3.125E-4f));
            } else {
                try {
                    this.scaleMap.put((EntityEnderCrystal)crystal, Float.valueOf(this.scaleMap.get((Object)crystal).floatValue() + 3.125E-4f));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!((double)this.scaleMap.get((Object)crystal).floatValue() >= 0.0625 * this.scale.getValue())) continue;
            this.scaleMap.remove((Object)crystal);
        }
    }

    @CommitEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet = (SPacketDestroyEntities)event.getPacket();
            for (int id : packet.getEntityIDs()) {
                try {
                    Entity entity = CrystalRender.mc.world.getEntityByID(id);
                    if (!(entity instanceof EntityEnderCrystal)) continue;
                    this.scaleMap.remove((Object)entity);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue().booleanValue()) {
            return;
        }
        Color colour = EntityUtil.getColor(event.entity, this.wireColour.getValue().getRed(), this.wireColour.getValue().getGreen(), this.wireColour.getValue().getBlue(), this.wireColour.getValue().getAlpha(), false);
        CrystalRender.mc.gameSettings.fancyGraphics = false;
        CrystalRender.mc.gameSettings.gammaSetting = 10000.0f;
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6913);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        if (this.throughwalls.getValue().booleanValue()) {
            GL11.glDisable((int)2929);
        }
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.color((float)((float)colour.getRed() / 255.0f), (float)((float)colour.getGreen() / 255.0f), (float)((float)colour.getBlue() / 255.0f), (float)((float)colour.getAlpha() / 255.0f));
        GlStateManager.glLineWidth((float)this.width.getValue().floatValue());
        event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}

