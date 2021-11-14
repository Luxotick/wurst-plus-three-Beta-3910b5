/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 */
package me.travis.wurstplusthree.hack.hacks.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplusthree.event.events.TotemPopEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.elements.Colour;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

@Hack.Registration(name="Chams", description="draws people as colours/through walls", category=Hack.Category.RENDER, isListening=false)
public class Chams
extends Hack {
    public static Chams INSTANCE;
    public EnumSetting mode = new EnumSetting("Mode", "Wire", Arrays.asList("Model", "Wire", "Shine", "WireModel"), this);
    public DoubleSetting width = new DoubleSetting("Width", (Double)2.0, (Double)0.0, (Double)5.0, this, v -> this.mode.is("Wire"));
    public BooleanSetting texture = new BooleanSetting("Texture", (Boolean)false, (Hack)this);
    public BooleanSetting lighting = new BooleanSetting("Lighting", (Boolean)false, (Hack)this);
    public BooleanSetting blend = new BooleanSetting("Blend", (Boolean)false, (Hack)this);
    public BooleanSetting transparent = new BooleanSetting("Transparent", (Boolean)false, (Hack)this);
    public BooleanSetting depth = new BooleanSetting("Depth", (Boolean)false, (Hack)this);
    public BooleanSetting xqz = new BooleanSetting("xqz", (Boolean)false, (Hack)this);
    public ParentSetting playerParent = new ParentSetting("Players", this);
    public BooleanSetting players = new BooleanSetting("RenderPlayers", (Boolean)false, this.playerParent);
    public BooleanSetting local = new BooleanSetting("Self", (Boolean)false, this.playerParent);
    public ColourSetting highlightColorPlayer = new ColourSetting("PlayerColor", new Colour(250, 0, 250, 50), this.playerParent);
    public ColourSetting xqzColorPlayer = new ColourSetting("PlayerXqz", new Colour(0, 70, 250, 50), this.playerParent, v -> this.xqz.getValue());
    public BooleanSetting popChams = new BooleanSetting("PopChams", (Boolean)false, this.playerParent);
    public ColourSetting popChamsColors = new ColourSetting("PlayerColor", new Colour(255, 255, 255, 200), this.playerParent);
    public ParentSetting mobsParent = new ParentSetting("PassiveMobs", this);
    public BooleanSetting mobs = new BooleanSetting("RenderMobs", (Boolean)false, this.mobsParent);
    public ColourSetting highlightColorMobs = new ColourSetting("MobColor", new Colour(80, 200, 0, 50), this.mobsParent);
    public ColourSetting xqzColorPlayerMobs = new ColourSetting("MobXqz", new Colour(0, 59, 200, 50), this.mobsParent, v -> this.xqz.getValue());
    public ParentSetting monstersParent = new ParentSetting("Monsters", this);
    public BooleanSetting monsters = new BooleanSetting("RenderMonsters", (Boolean)false, this.monstersParent);
    public ColourSetting highlightColorMonster = new ColourSetting("MonsterColor", new Colour(140, 200, 250, 50), this.monstersParent);
    public ColourSetting xqzColorMonster = new ColourSetting("MonsterXqz", new Colour(190, 0, 90, 50), this.monstersParent, v -> this.xqz.getValue());
    public ConcurrentHashMap<Integer, Integer> pops = new ConcurrentHashMap();

    public Chams() {
        INSTANCE = this;
    }

    @CommitEvent(priority=EventPriority.LOW)
    public void onPopLol(TotemPopEvent event) {
        if (this.popChams.getValue().booleanValue() && event.getEntity() != Chams.mc.player) {
            EntityPlayer ee = event.getEntity();
            ClientMessage.sendMessage("PopEventLol");
            ArrayList<Integer> idList = new ArrayList<Integer>();
            for (Entity e : Chams.mc.world.loadedEntityList) {
                idList.add(e.entityId);
            }
            EntityOtherPlayerMP popCham = new EntityOtherPlayerMP((World)Chams.mc.world, event.getEntity().getGameProfile());
            popCham.copyLocationAndAnglesFrom((Entity)ee);
            popCham.rotationYawHead = ee.getRotationYawHead();
            popCham.rotationYaw = ee.rotationYaw;
            popCham.rotationPitch = ee.rotationPitch;
            popCham.setGameType(GameType.CREATIVE);
            popCham.setHealth(20.0f);
            for (int i = 0; i > -10000; --i) {
                if (idList.contains(i)) continue;
                Chams.mc.world.addEntityToWorld(i, (Entity)popCham);
                this.pops.put(i, this.popChamsColors.getValue().getAlpha());
                break;
            }
        }
    }
}

