/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketEntityTeleport
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.event.events.PacketEvent;
import me.travis.wurstplusthree.event.events.Render3DEvent;
import me.travis.wurstplusthree.event.events.UpdateWalkingPlayerEvent;
import me.travis.wurstplusthree.event.processor.CommitEvent;
import me.travis.wurstplusthree.event.processor.EventPriority;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.hack.HackPriority;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import me.travis.wurstplusthree.hack.hacks.combat.Threads;
import me.travis.wurstplusthree.hack.hacks.misc.AutoClip;
import me.travis.wurstplusthree.setting.type.BooleanSetting;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.setting.type.DoubleSetting;
import me.travis.wurstplusthree.setting.type.EnumSetting;
import me.travis.wurstplusthree.setting.type.IntSetting;
import me.travis.wurstplusthree.setting.type.KeySetting;
import me.travis.wurstplusthree.setting.type.ParentSetting;
import me.travis.wurstplusthree.util.BlockUtil;
import me.travis.wurstplusthree.util.ClientMessage;
import me.travis.wurstplusthree.util.CrystalUtil;
import me.travis.wurstplusthree.util.EntityUtil;
import me.travis.wurstplusthree.util.InventoryUtil;
import me.travis.wurstplusthree.util.MathsUtil;
import me.travis.wurstplusthree.util.PlayerUtil;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Colour;
import me.travis.wurstplusthree.util.elements.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Hack.Registration(name="Crystal Aura", description="the goods", category=Hack.Category.COMBAT, priority=HackPriority.Highest)
public final class CrystalAura
extends Hack {
    public static CrystalAura INSTANCE;
    private final ParentSetting ranges = new ParentSetting("Ranges", this);
    private final DoubleSetting breakRange = new DoubleSetting("Break Range", (Double)5.0, (Double)0.0, (Double)6.0, this.ranges);
    private final DoubleSetting placeRange = new DoubleSetting("Place Range", (Double)5.0, (Double)0.0, (Double)6.0, this.ranges);
    private final DoubleSetting breakRangeWall = new DoubleSetting("Break Range Wall", (Double)3.0, (Double)0.0, (Double)6.0, this.ranges);
    private final DoubleSetting placeRangeWall = new DoubleSetting("Place Range Wall", (Double)3.0, (Double)0.0, (Double)6.0, this.ranges);
    private final DoubleSetting targetRange = new DoubleSetting("Target Range", (Double)15.0, (Double)0.0, (Double)20.0, this.ranges);
    private final ParentSetting delays = new ParentSetting("Delays", this);
    private final IntSetting placeDelay = new IntSetting("Place Delay", 0, 0, 10, this.delays);
    private final IntSetting breakDelay = new IntSetting("Break Delay", 0, 0, 10, this.delays);
    private final ParentSetting damages = new ParentSetting("Damages", this);
    private final BooleanSetting sortBlocks = new BooleanSetting("Sort Blocks", (Boolean)true, this.damages);
    private final BooleanSetting ignoreSelfDamage = new BooleanSetting("Ignore Self", (Boolean)false, this.damages);
    private final IntSetting minPlace = new IntSetting("MinPlace", 9, 0, 36, this.damages);
    private final IntSetting maxSelfPlace = new IntSetting("MaxSelfPlace", 5, 0, 36, this.damages, s -> this.ignoreSelfDamage.getValue() == false);
    private final IntSetting minBreak = new IntSetting("MinBreak", 9, 0, 36, this.damages);
    private final IntSetting maxSelfBreak = new IntSetting("MaxSelfBreak", 5, 0, 36, this.damages, s -> this.ignoreSelfDamage.getValue() == false);
    private final BooleanSetting antiSuicide = new BooleanSetting("Anti Suicide", (Boolean)true, this.damages);
    private final ParentSetting general = new ParentSetting("General", this);
    public final EnumSetting rotateMode = new EnumSetting("Rotate", "Off", Arrays.asList("Off", "Break", "Place", "Both"), this.general);
    public final IntSetting maxYaw = new IntSetting("MaxYaw", 180, 0, 180, this.general);
    private final BooleanSetting raytrace = new BooleanSetting("Raytrace", (Boolean)false, this.general);
    private final EnumSetting fastMode = new EnumSetting("Fast", "Ignore", Arrays.asList("Off", "Ignore", "Ghost", "Sound"), this.general);
    public final EnumSetting autoSwitch = new EnumSetting("Switch", "None", Arrays.asList("Allways", "NoGap", "None", "Silent"), this.general);
    private final BooleanSetting silentSwitchHand = new BooleanSetting("Hand Activation", (Boolean)true, this.general, s -> this.autoSwitch.is("Silent"));
    private final BooleanSetting antiWeakness = new BooleanSetting("Anti Weakness", (Boolean)true, this.general);
    private final IntSetting maxCrystals = new IntSetting("MaxCrystal", 1, 1, 4, this.general);
    private final BooleanSetting ignoreTerrain = new BooleanSetting("Terrain Trace", (Boolean)true, this.general);
    private final EnumSetting crystalLogic = new EnumSetting("Placements", "Damage", Arrays.asList("Damage", "Nearby", "Safe"), this.general);
    private final BooleanSetting thirteen = new BooleanSetting("1.13", (Boolean)false, this.general);
    private final BooleanSetting attackPacket = new BooleanSetting("AttackPacket", (Boolean)true, this.general);
    private final BooleanSetting packetSafe = new BooleanSetting("Packet Safe", (Boolean)true, this.general);
    private final BooleanSetting noBreakCalcs = new BooleanSetting("No Break Calcs", (Boolean)false, this.general);
    private final EnumSetting arrayListMode = new EnumSetting("Array List Mode", "Latency", Arrays.asList("Latency", "Player", "CPS"), this.general);
    private final BooleanSetting debug = new BooleanSetting("Debug", (Boolean)false, this.general);
    private final ParentSetting misc = new ParentSetting("Misc", this);
    private final BooleanSetting threaded = new BooleanSetting("Threaded", (Boolean)false, this.misc);
    private final BooleanSetting antiStuck = new BooleanSetting("Anti Stuck", (Boolean)false, this.misc);
    private final IntSetting maxAntiStuckDamage = new IntSetting("Stuck Self Damage", 8, 0, 36, this.misc, v -> this.antiStuck.getValue());
    private final ParentSetting predict = new ParentSetting("Predict", this);
    private final BooleanSetting predictCrystal = new BooleanSetting("Predict Crystal", (Boolean)true, this.predict);
    private final BooleanSetting predictBlock = new BooleanSetting("Predict Block", (Boolean)true, this.predict);
    private final EnumSetting predictTeleport = new EnumSetting("P Teleport", "Sound", Arrays.asList("Sound", "Packet", "None"), this.predict);
    private final BooleanSetting entityPredict = new BooleanSetting("Entity Predict", (Boolean)true, this.predict, v -> this.rotateMode.is("Off"));
    private final IntSetting predictedTicks = new IntSetting("Predict Ticks", 2, 0, 5, this.predict, s -> this.entityPredict.getValue() != false && this.rotateMode.is("Off"));
    private final ParentSetting FeetObi = new ParentSetting("ObifeetMode", this);
    private final BooleanSetting palceObiFeet = new BooleanSetting("Enabled", (Boolean)false, this.FeetObi);
    private final BooleanSetting ObiYCheck = new BooleanSetting("YCheck", (Boolean)false, this.FeetObi);
    private final BooleanSetting rotateObiFeet = new BooleanSetting("Rotate", (Boolean)false, this.FeetObi);
    private final IntSetting timeoutTicksObiFeet = new IntSetting("Timeout", 3, 0, 5, this.FeetObi);
    private final ParentSetting faceplace = new ParentSetting("Tabbott", this);
    private final BooleanSetting noMP = new BooleanSetting("NoMultiPlace", (Boolean)false, this.faceplace);
    private final IntSetting facePlaceHP = new IntSetting("TabbottHP", 0, 0, 36, this.faceplace);
    private final IntSetting facePlaceDelay = new IntSetting("TabbottDelay", 0, 0, 10, this.faceplace);
    private final KeySetting fpbind = new KeySetting("TabbottBind", -1, this.faceplace);
    private final IntSetting fuckArmourHP = new IntSetting("Armour%", 0, 0, 100, this.faceplace);
    private final ParentSetting render = new ParentSetting("Render", this);
    private final EnumSetting when = new EnumSetting("When", "Place", Arrays.asList("Place", "Break", "Both", "Never"), this.render);
    private final EnumSetting mode = new EnumSetting("Mode", "Pretty", Arrays.asList("Pretty", "Solid", "Outline"), this.render);
    private final BooleanSetting fade = new BooleanSetting("Fade", (Boolean)false, this.render);
    private final IntSetting fadeTime = new IntSetting("FadeTime", 200, 0, 1000, this.render, v -> this.fade.getValue());
    private final BooleanSetting flat = new BooleanSetting("Flat", (Boolean)false, this.render);
    private final DoubleSetting height = new DoubleSetting("FlatHeight", (Double)0.2, (Double)-2.0, (Double)2.0, this.render, s -> this.flat.getValue());
    private final IntSetting width = new IntSetting("Width", 1, 1, 10, this.render);
    private final ColourSetting renderFillColour = new ColourSetting("FillColour", new Colour(0, 0, 0, 255), this.render);
    private final ColourSetting renderBoxColour = new ColourSetting("BoxColour", new Colour(255, 255, 255, 255), this.render);
    private final BooleanSetting renderDamage = new BooleanSetting("RenderDamage", (Boolean)true, this.render);
    private final EnumSetting swing = new EnumSetting("Swing", "Mainhand", Arrays.asList("Mainhand", "Offhand", "None"), this.render);
    private final BooleanSetting placeSwing = new BooleanSetting("Place Swing", (Boolean)true, this.render);
    private final List<EntityEnderCrystal> attemptedCrystals = new ArrayList<EntityEnderCrystal>();
    private final ArrayList<RenderPos> renderMap = new ArrayList();
    private final ArrayList<BlockPos> currentTargets = new ArrayList();
    private final Timer crystalsPlacedTimer = new Timer();
    private EntityEnderCrystal stuckCrystal;
    private boolean alreadyAttacking;
    private boolean placeTimeoutFlag;
    private boolean hasPacketBroke;
    private boolean didAnything;
    private boolean facePlacing;
    private long start;
    private long crystalLatency;
    private int placeTimeout;
    private int breakTimeout;
    private int breakDelayCounter;
    private int placeDelayCounter;
    private int facePlaceDelayCounter;
    private int obiFeetCounter;
    private int crystalsPlaced;
    public EntityPlayer ezTarget;
    public ArrayList<BlockPos> staticPos;
    public EntityEnderCrystal staticEnderCrystal;

    public CrystalAura() {
        INSTANCE = this;
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onUpdateWalkingPlayerEvent(@NotNull UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && !this.rotateMode.is("Off")) {
            this.doCrystalAura();
        }
    }

    @Override
    public void onUpdate() {
        if (this.rotateMode.is("Off")) {
            this.doCrystalAura();
        }
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onPacketSend(@NotNull PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)CrystalAura.mc.world) instanceof EntityEnderCrystal && this.fastMode.is("Ghost")) {
            Objects.requireNonNull(packet.getEntityFromWorld((World)CrystalAura.mc.world)).setDead();
            CrystalAura.mc.world.removeEntityFromWorld(packet.entityId);
        }
    }

    @CommitEvent(priority=EventPriority.HIGH)
    public final void onPacketReceive(@NotNull PacketEvent.Receive event) {
        SPacketSpawnObject spawnObjectPacket;
        if (event.getPacket() instanceof SPacketSpawnObject && (spawnObjectPacket = (SPacketSpawnObject)event.getPacket()).getType() == 51 && this.predictCrystal.getValue().booleanValue()) {
            for (EntityPlayer target : new ArrayList(CrystalAura.mc.world.playerEntities)) {
                if (this.isCrystalGood(new EntityEnderCrystal((World)CrystalAura.mc.world, spawnObjectPacket.getX(), spawnObjectPacket.getY(), spawnObjectPacket.getZ()), target) == 0.0) continue;
                if (this.debug.getValue().booleanValue()) {
                    ClientMessage.sendMessage("predict break");
                }
                CPacketUseEntity predict = new CPacketUseEntity();
                predict.entityId = spawnObjectPacket.getEntityID();
                predict.action = CPacketUseEntity.Action.ATTACK;
                CrystalAura.mc.player.connection.sendPacket((Packet)predict);
                if (!this.swing.is("None")) {
                    BlockUtil.swingArm(this.swing);
                }
                if (!this.packetSafe.getValue().booleanValue()) break;
                this.hasPacketBroke = true;
                this.didAnything = true;
                break;
            }
        }
        if (event.getPacket() instanceof SPacketEntityTeleport) {
            SPacketEntityTeleport tpPacket = (SPacketEntityTeleport)event.getPacket();
            Entity e = CrystalAura.mc.world.getEntityByID(tpPacket.getEntityId());
            if (e == CrystalAura.mc.player) {
                return;
            }
            if (e instanceof EntityPlayer && this.predictTeleport.is("Packet")) {
                e.setEntityBoundingBox(e.getEntityBoundingBox().offset(tpPacket.getX(), tpPacket.getY(), tpPacket.getZ()));
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect soundPacket = (SPacketSoundEffect)event.getPacket();
            if (soundPacket.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT && this.predictTeleport.is("Sound")) {
                CrystalAura.mc.world.loadedEntityList.spliterator().forEachRemaining(player -> {
                    if (player instanceof EntityPlayer && player != CrystalAura.mc.player && player.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= this.targetRange.getValue()) {
                        player.setEntityBoundingBox(player.getEntityBoundingBox().offset(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()));
                    }
                });
            }
            try {
                if (soundPacket.getCategory() == SoundCategory.BLOCKS && soundPacket.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity crystal : new ArrayList(CrystalAura.mc.world.loadedEntityList)) {
                        if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= this.breakRange.getValue())) continue;
                        this.crystalLatency = System.currentTimeMillis() - this.start;
                        if (!this.fastMode.getValue().equals("Sound")) continue;
                        crystal.setDead();
                    }
                }
            }
            catch (NullPointerException e) {
                // empty catch block
            }
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion explosionPacket = (SPacketExplosion)event.getPacket();
            BlockPos pos = new BlockPos(Math.floor(explosionPacket.getX()), Math.floor(explosionPacket.getY()), Math.floor(explosionPacket.getZ())).down();
            if (this.predictBlock.getValue().booleanValue()) {
                for (EntityPlayer player2 : new ArrayList(CrystalAura.mc.world.playerEntities)) {
                    if (!(this.isBlockGood(pos, player2) > 0.0)) continue;
                    BlockUtil.placeCrystalOnBlock(pos, EnumHand.MAIN_HAND, true);
                }
            }
        }
    }

    public void doCrystalAura() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        this.didAnything = false;
        if (!(this.placeDelayCounter <= this.placeTimeout || this.facePlaceDelayCounter < this.facePlaceDelay.getValue() && this.facePlacing)) {
            this.start = System.currentTimeMillis();
            this.placeCrystal();
        }
        if (!(this.breakDelayCounter <= this.breakTimeout || this.hasPacketBroke && this.packetSafe.getValue().booleanValue())) {
            if (this.debug.getValue().booleanValue()) {
                ClientMessage.sendMessage("Attempting break");
            }
            if (this.noBreakCalcs.getValue().booleanValue()) {
                this.breakCrystalNoCalcs();
            } else if (this.antiStuck.getValue().booleanValue() && this.stuckCrystal != null) {
                this.breakCrystal(this.stuckCrystal);
                this.stuckCrystal = null;
            } else {
                this.breakCrystal(null);
            }
        }
        if (!this.didAnything) {
            this.hasPacketBroke = false;
        }
        ++this.breakDelayCounter;
        ++this.placeDelayCounter;
        ++this.facePlaceDelayCounter;
        ++this.obiFeetCounter;
    }

    private void clearMap(BlockPos checkBlock) {
        ArrayList<RenderPos> toRemove = new ArrayList<RenderPos>();
        if (checkBlock == null || this.renderMap.isEmpty()) {
            return;
        }
        for (RenderPos pos : this.renderMap) {
            if (pos.pos.getX() != checkBlock.getX() || pos.pos.getY() != checkBlock.getY() || pos.pos.getZ() != checkBlock.getZ()) continue;
            toRemove.add(pos);
        }
        this.renderMap.removeAll(toRemove);
    }

    private void placeCrystal() {
        ArrayList<BlockPos> placePositions = this.getBestBlocks();
        this.currentTargets.clear();
        this.currentTargets.addAll(placePositions);
        if (placePositions == null) {
            return;
        }
        if (placePositions.size() > 0) {
            boolean offhandCheck = false;
            int slot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
            int old = CrystalAura.mc.player.inventory.currentItem;
            EnumHand hand = null;
            int stackSize = this.getCrystalCount(false);
            this.alreadyAttacking = false;
            if (CrystalAura.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (CrystalAura.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && (this.autoSwitch.getValue().equals("Allways") || this.autoSwitch.is("NoGap"))) {
                    if (this.autoSwitch.is("NoGap") && CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
                        return;
                    }
                    if (this.findCrystalsHotbar() == -1) {
                        return;
                    }
                    CrystalAura.mc.player.inventory.currentItem = this.findCrystalsHotbar();
                    CrystalAura.mc.playerController.syncCurrentPlayItem();
                }
            } else {
                offhandCheck = true;
            }
            if (this.autoSwitch.is("Silent") && slot != -1) {
                if (CrystalAura.mc.player.isHandActive() && this.silentSwitchHand.getValue().booleanValue()) {
                    hand = CrystalAura.mc.player.getActiveHand();
                }
                CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            }
            this.placeDelayCounter = 0;
            this.facePlaceDelayCounter = 0;
            this.didAnything = true;
            for (BlockPos targetBlock : placePositions) {
                if (CrystalAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || CrystalAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal || this.autoSwitch.is("Silent")) {
                    if (!this.setYawPitch(targetBlock)) continue;
                    EntityEnderCrystal cCheck = CrystalUtil.isCrystalStuck(targetBlock.up());
                    if (cCheck != null && this.antiStuck.getValue().booleanValue()) {
                        this.stuckCrystal = cCheck;
                        if (this.debug.getValue().booleanValue()) {
                            ClientMessage.sendMessage("SHITS STUCK");
                        }
                    }
                    BlockUtil.placeCrystalOnBlock(targetBlock, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing.getValue());
                    if (this.debug.getValue().booleanValue()) {
                        ClientMessage.sendMessage("placing");
                    }
                    ++this.crystalsPlaced;
                    continue;
                }
                if (!this.debug.getValue().booleanValue()) continue;
                ClientMessage.sendMessage("doing yawstep on place");
            }
            int newSize = this.getCrystalCount(offhandCheck);
            if (this.autoSwitch.is("Silent") && slot != -1) {
                CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(old));
                if (this.silentSwitchHand.getValue().booleanValue() && hand != null) {
                    CrystalAura.mc.player.setActiveHand(hand);
                }
            }
            if (newSize == stackSize) {
                this.didAnything = false;
            }
        }
    }

    private int getCrystalCount(boolean offhand) {
        if (offhand) {
            return CrystalAura.mc.player.getHeldItemOffhand().stackSize;
        }
        return CrystalAura.mc.player.getHeldItemMainhand().stackSize;
    }

    private void breakCrystalNoCalcs() {
        for (Entity e : CrystalAura.mc.world.loadedEntityList) {
            EntityEnderCrystal crystal;
            if (!(e instanceof EntityEnderCrystal) || e.isDead || (double)CrystalAura.mc.player.getDistance(e) > this.breakRange.getValue() || !CrystalAura.mc.player.canEntityBeSeen(e) && (this.raytrace.getValue().booleanValue() || (double)CrystalAura.mc.player.getDistance(e) > this.breakRangeWall.getValue())) continue;
            if (this.antiWeakness.getValue().booleanValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                boolean shouldWeakness = true;
                if (CrystalAura.mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(CrystalAura.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                    shouldWeakness = false;
                }
                if (shouldWeakness) {
                    if (!this.alreadyAttacking) {
                        this.alreadyAttacking = true;
                    }
                    int newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = CrystalAura.mc.player.inventory.getStackInSlot(i);
                        if (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemTool)) continue;
                        newSlot = i;
                        CrystalAura.mc.playerController.updateController();
                        break;
                    }
                    if (newSlot != -1) {
                        CrystalAura.mc.player.inventory.currentItem = newSlot;
                    }
                }
            }
            if (this.setYawPitch(crystal = (EntityEnderCrystal)e)) {
                EntityUtil.attackEntity((Entity)crystal, this.attackPacket.getValue());
                if (!this.swing.is("None")) {
                    BlockUtil.swingArm(this.swing);
                }
                if (this.debug.getValue().booleanValue()) {
                    ClientMessage.sendMessage("breaking");
                }
                this.breakDelayCounter = 0;
                continue;
            }
            if (!this.debug.getValue().booleanValue()) continue;
            ClientMessage.sendMessage("doing yawstep on break");
        }
    }

    private void breakCrystal(EntityEnderCrystal overwriteCrystal) {
        EntityEnderCrystal crystal;
        if (this.threaded.getValue().booleanValue()) {
            Threads threads = new Threads();
            threads.start();
            crystal = this.staticEnderCrystal;
        } else {
            crystal = this.getBestCrystal();
        }
        if (overwriteCrystal != null) {
            if (this.debug.getValue().booleanValue()) {
                ClientMessage.sendMessage("Overwriting Crystal");
            }
            if (CrystalUtil.calculateDamage((Entity)overwriteCrystal, (Entity)CrystalAura.mc.player, (boolean)this.ignoreTerrain.getValue()) < (float)this.maxAntiStuckDamage.getValue().intValue()) {
                crystal = overwriteCrystal;
            }
        }
        if (crystal == null) {
            return;
        }
        if (this.antiWeakness.getValue().booleanValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean shouldWeakness = true;
            if (CrystalAura.mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(CrystalAura.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                shouldWeakness = false;
            }
            if (shouldWeakness) {
                if (!this.alreadyAttacking) {
                    this.alreadyAttacking = true;
                }
                int newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = CrystalAura.mc.player.inventory.getStackInSlot(i);
                    if (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemTool)) continue;
                    newSlot = i;
                    CrystalAura.mc.playerController.updateController();
                    break;
                }
                if (newSlot != -1) {
                    CrystalAura.mc.player.inventory.currentItem = newSlot;
                }
            }
        }
        this.didAnything = true;
        if (this.setYawPitch(crystal)) {
            EntityUtil.attackEntity((Entity)crystal, this.attackPacket.getValue());
            if (!this.swing.is("None")) {
                BlockUtil.swingArm(this.swing);
            }
            if (this.debug.getValue().booleanValue()) {
                ClientMessage.sendMessage("breaking");
            }
            this.breakDelayCounter = 0;
        } else if (this.debug.getValue().booleanValue()) {
            ClientMessage.sendMessage("doing yawstep on break");
        }
    }

    public EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0.0;
        EntityEnderCrystal bestCrystal = null;
        for (Entity e : CrystalAura.mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal crystal = (EntityEnderCrystal)e;
            for (EntityPlayer target : new ArrayList(CrystalAura.mc.world.playerEntities)) {
                double targetDamage;
                if (CrystalAura.mc.player.getDistanceSq((Entity)target) > MathsUtil.square(this.targetRange.getValue().floatValue())) continue;
                if (this.entityPredict.getValue().booleanValue() && this.rotateMode.is("Off")) {
                    float f = target.width / 2.0f;
                    float f1 = target.height;
                    target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double)f, target.posY, target.posZ - (double)f, target.posX + (double)f, target.posY + (double)f1, target.posZ + (double)f));
                    Entity y = CrystalUtil.getPredictedPosition((Entity)target, this.predictedTicks.getValue().intValue());
                    target.setEntityBoundingBox(y.getEntityBoundingBox());
                }
                if ((targetDamage = this.isCrystalGood(crystal, target)) <= 0.0 || !(targetDamage > bestDamage)) continue;
                bestDamage = targetDamage;
                this.ezTarget = target;
                bestCrystal = crystal;
            }
        }
        if (this.ezTarget != null) {
            WurstplusThree.KD_MANAGER.targets.put(this.ezTarget.getName(), 20);
            AutoClip.INSTANCE.targets.put(this.ezTarget.getName(), 20);
        }
        if (bestCrystal != null && (this.when.is("Both") || this.when.is("Break"))) {
            BlockPos renderPos = bestCrystal.getPosition().down();
            this.clearMap(renderPos);
            this.renderMap.add(new RenderPos(renderPos, bestDamage));
        }
        return bestCrystal;
    }

    private ArrayList<BlockPos> getBestBlocks() {
        ArrayList<RenderPos> posArrayList = new ArrayList<RenderPos>();
        if (this.getBestCrystal() != null && this.fastMode.is("Off")) {
            this.placeTimeoutFlag = true;
            return null;
        }
        if (this.placeTimeoutFlag) {
            this.placeTimeoutFlag = false;
            return null;
        }
        for (EntityPlayer target : new ArrayList(CrystalAura.mc.world.playerEntities)) {
            if (CrystalAura.mc.player.getDistanceSq((Entity)target) > MathsUtil.square(this.targetRange.getValue().floatValue())) continue;
            if (this.entityPredict.getValue().booleanValue()) {
                float f = target.width / 2.0f;
                float f1 = target.height;
                target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double)f, target.posY, target.posZ - (double)f, target.posX + (double)f, target.posY + (double)f1, target.posZ + (double)f));
                Entity y = CrystalUtil.getPredictedPosition((Entity)target, this.predictedTicks.getValue().intValue());
                target.setEntityBoundingBox(y.getEntityBoundingBox());
            }
            for (BlockPos blockPos : CrystalUtil.possiblePlacePositions(this.placeRange.getValue().floatValue(), true, this.thirteen.getValue())) {
                double targetDamage = this.isBlockGood(blockPos, target);
                if (targetDamage <= 0.0) continue;
                posArrayList.add(new RenderPos(blockPos, targetDamage));
            }
        }
        if (this.sortBlocks.getValue().booleanValue()) {
            posArrayList.sort(new DamageComparator());
        }
        if (this.maxCrystals.getValue() > 1) {
            ArrayList<BlockPos> blockedPosList = new ArrayList<BlockPos>();
            ArrayList<RenderPos> toRemove = new ArrayList<RenderPos>();
            for (RenderPos test : posArrayList) {
                boolean blocked = false;
                for (BlockPos blockPos : blockedPosList) {
                    if (blockPos.getX() != test.pos.getX() || blockPos.getY() != test.pos.getY() || blockPos.getZ() != test.pos.getZ()) continue;
                    blocked = true;
                }
                if (!blocked) {
                    blockedPosList.addAll(this.getBlockedPositions(test.pos));
                    continue;
                }
                toRemove.add(test);
            }
            posArrayList.removeAll(toRemove);
        }
        if (this.ezTarget != null) {
            WurstplusThree.KD_MANAGER.targets.put(this.ezTarget.getName(), 20);
            AutoClip.INSTANCE.targets.put(this.ezTarget.getName(), 20);
        }
        int maxCrystals = this.maxCrystals.getValue();
        if (this.facePlacing && this.noMP.getValue().booleanValue()) {
            maxCrystals = 1;
        }
        ArrayList<BlockPos> finalArrayList = new ArrayList<BlockPos>();
        IntStream.range(0, Math.min(maxCrystals, posArrayList.size())).forEachOrdered(n -> {
            RenderPos pos = (RenderPos)posArrayList.get(n);
            if (this.when.is("Both") || this.when.is("Place")) {
                this.clearMap(pos.pos);
                if (pos.pos != null) {
                    this.renderMap.add(pos);
                }
            }
            finalArrayList.add(pos.pos);
        });
        return finalArrayList;
    }

    private ArrayList<BlockPos> getBlockedPositions(BlockPos pos) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        list.add(pos.add(1, -1, 1));
        list.add(pos.add(1, -1, -1));
        list.add(pos.add(-1, -1, 1));
        list.add(pos.add(-1, -1, -1));
        list.add(pos.add(-1, -1, 0));
        list.add(pos.add(1, -1, 0));
        list.add(pos.add(0, -1, -1));
        list.add(pos.add(0, -1, 1));
        list.add(pos.add(1, 0, 1));
        list.add(pos.add(1, 0, -1));
        list.add(pos.add(-1, 0, 1));
        list.add(pos.add(-1, 0, -1));
        list.add(pos.add(-1, 0, 0));
        list.add(pos.add(1, 0, 0));
        list.add(pos.add(0, 0, -1));
        list.add(pos.add(0, 0, 1));
        list.add(pos.add(1, 1, 1));
        list.add(pos.add(1, 1, -1));
        list.add(pos.add(-1, 1, 1));
        list.add(pos.add(-1, 1, -1));
        list.add(pos.add(-1, 1, 0));
        list.add(pos.add(1, 1, 0));
        list.add(pos.add(0, 1, -1));
        list.add(pos.add(0, 1, 1));
        return list;
    }

    private double isCrystalGood(@NotNull EntityEnderCrystal crystal, @NotNull EntityPlayer target) {
        if (this.isPlayerValid(target)) {
            if (CrystalAura.mc.player.canEntityBeSeen((Entity)crystal) ? CrystalAura.mc.player.getDistanceSq((Entity)crystal) > MathsUtil.square(this.breakRange.getValue().floatValue()) : CrystalAura.mc.player.getDistanceSq((Entity)crystal) > MathsUtil.square(this.breakRangeWall.getValue().floatValue())) {
                return 0.0;
            }
            if (crystal.isDead) {
                return 0.0;
            }
            if (this.attemptedCrystals.contains((Object)crystal)) {
                return 0.0;
            }
            double targetDamage = CrystalUtil.calculateDamage((Entity)crystal, (Entity)target, (boolean)this.ignoreTerrain.getValue());
            this.facePlacing = false;
            double miniumDamage = this.minBreak.getValue().intValue();
            if ((EntityUtil.getHealth((Entity)target) <= (float)this.facePlaceHP.getValue().intValue() || CrystalUtil.getArmourFucker(target, this.fuckArmourHP.getValue().intValue()).booleanValue() || this.fpbind.isDown()) && targetDamage < (double)this.minBreak.getValue().intValue()) {
                miniumDamage = EntityUtil.isInHole((Entity)target) ? 1.0 : 2.0;
                this.facePlacing = true;
            }
            if (targetDamage < miniumDamage && (double)EntityUtil.getHealth((Entity)target) - targetDamage > 0.0) {
                return 0.0;
            }
            double selfDamage = 0.0;
            if (!this.ignoreSelfDamage.getValue().booleanValue()) {
                selfDamage = CrystalUtil.calculateDamage((Entity)crystal, (Entity)CrystalAura.mc.player, (boolean)this.ignoreTerrain.getValue());
            }
            if (selfDamage > (double)this.maxSelfBreak.getValue().intValue()) {
                return 0.0;
            }
            if ((double)EntityUtil.getHealth((Entity)CrystalAura.mc.player) - selfDamage <= 0.0 && this.antiSuicide.getValue().booleanValue()) {
                return 0.0;
            }
            switch (this.crystalLogic.getValue()) {
                case "Safe": {
                    return targetDamage - selfDamage;
                }
                case "Damage": {
                    return targetDamage;
                }
                case "Nearby": {
                    double distance = CrystalAura.mc.player.getDistanceSq((Entity)crystal);
                    return targetDamage - distance;
                }
            }
        }
        return 0.0;
    }

    private double isBlockGood(@NotNull BlockPos blockPos, @NotNull EntityPlayer target) {
        if (this.isPlayerValid(target)) {
            if (!CrystalUtil.canSeePos(blockPos) && this.raytrace.getValue().booleanValue()) {
                return 0.0;
            }
            if (!CrystalUtil.canSeePos(blockPos) ? CrystalAura.mc.player.getDistanceSq(blockPos) > MathsUtil.square(this.placeRangeWall.getValue().floatValue()) : CrystalAura.mc.player.getDistanceSq(blockPos) > MathsUtil.square(this.placeRange.getValue().floatValue())) {
                return 0.0;
            }
            double targetDamage = CrystalUtil.calculateDamage(blockPos, (Entity)target, (boolean)this.ignoreTerrain.getValue());
            this.facePlacing = false;
            double miniumDamage = this.minPlace.getValue().intValue();
            if ((EntityUtil.getHealth((Entity)target) <= (float)this.facePlaceHP.getValue().intValue() || CrystalUtil.getArmourFucker(target, this.fuckArmourHP.getValue().intValue()).booleanValue() || this.fpbind.isDown()) && targetDamage < (double)this.minPlace.getValue().intValue()) {
                miniumDamage = EntityUtil.isInHole((Entity)target) ? 1.0 : 2.0;
                this.facePlacing = true;
            }
            if (targetDamage < miniumDamage && (double)EntityUtil.getHealth((Entity)target) - targetDamage > 0.0) {
                return 0.0;
            }
            double selfDamage = 0.0;
            if (!this.ignoreSelfDamage.getValue().booleanValue()) {
                selfDamage = CrystalUtil.calculateDamage(blockPos, (Entity)CrystalAura.mc.player, (boolean)this.ignoreTerrain.getValue());
            }
            if (selfDamage > (double)this.maxSelfPlace.getValue().intValue()) {
                return 0.0;
            }
            if ((double)EntityUtil.getHealth((Entity)CrystalAura.mc.player) - selfDamage <= 0.0 && this.antiSuicide.getValue().booleanValue()) {
                return 0.0;
            }
            switch (this.crystalLogic.getValue()) {
                case "Safe": {
                    return targetDamage - selfDamage;
                }
                case "Damage": {
                    return targetDamage;
                }
                case "Nearby": {
                    double distance = CrystalAura.mc.player.getDistanceSq(blockPos);
                    return targetDamage - distance;
                }
            }
        }
        return 0.0;
    }

    private boolean isPlayerValid(@NotNull EntityPlayer player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 0.0f || player == CrystalAura.mc.player) {
            return false;
        }
        if (WurstplusThree.FRIEND_MANAGER.isFriend(player.getName())) {
            return false;
        }
        if (player.getName().equals(CrystalAura.mc.player.getName())) {
            return false;
        }
        if (player.getDistanceSq((Entity)CrystalAura.mc.player) > 169.0) {
            return false;
        }
        if (this.palceObiFeet.getValue().booleanValue() && this.obiFeetCounter >= this.timeoutTicksObiFeet.getValue() && CrystalAura.mc.player.getDistance((Entity)player) < 5.0f) {
            try {
                this.blockObiNextToPlayer(player);
            }
            catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void blockObiNextToPlayer(EntityPlayer player) {
        if (this.ObiYCheck.getValue().booleanValue() && Math.floor(player.posY) == Math.floor(CrystalAura.mc.player.posY)) {
            return;
        }
        this.obiFeetCounter = 0;
        BlockPos pos = EntityUtil.getFlooredPos((Entity)player).down();
        if (EntityUtil.isInHole((Entity)player) || CrystalAura.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            return;
        }
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                BlockPos checkPos;
                if (i == 0 && j == 0 || !CrystalAura.mc.world.getBlockState(checkPos = pos.add(i, 0, j)).getMaterial().isReplaceable()) continue;
                BlockUtil.placeBlock(checkPos, PlayerUtil.findObiInHotbar(), (boolean)this.rotateObiFeet.getValue(), (boolean)this.rotateObiFeet.getValue(), this.swing);
            }
        }
    }

    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (CrystalAura.mc.player.inventory.getStackInSlot(i).getItem() != Items.END_CRYSTAL) continue;
            return i;
        }
        return -1;
    }

    private boolean setYawPitch(@NotNull EntityEnderCrystal crystal) {
        if (this.rotateMode.is("Off") || this.rotateMode.is("Place")) {
            return true;
        }
        float[] angle = MathsUtil.calcAngle(CrystalAura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), crystal.getPositionEyes(mc.getRenderPartialTicks()));
        float yaw = angle[0];
        float pitch = angle[1];
        float spoofedYaw = WurstplusThree.ROTATION_MANAGER.getSpoofedYaw();
        if (Math.abs(spoofedYaw - yaw) > (float)this.maxYaw.getValue().intValue() && Math.abs(spoofedYaw - 360.0f - yaw) > (float)this.maxYaw.getValue().intValue() && Math.abs(spoofedYaw + 360.0f - yaw) > (float)this.maxYaw.getValue().intValue()) {
            WurstplusThree.ROTATION_MANAGER.setPlayerRotations(Math.abs(spoofedYaw - yaw) < 180.0f ? (spoofedYaw > yaw ? spoofedYaw - (float)this.maxYaw.getValue().intValue() : spoofedYaw + (float)this.maxYaw.getValue().intValue()) : (spoofedYaw > yaw ? spoofedYaw + (float)this.maxYaw.getValue().intValue() : spoofedYaw - (float)this.maxYaw.getValue().intValue()), pitch);
            return false;
        }
        WurstplusThree.ROTATION_MANAGER.setPlayerRotations(yaw, pitch);
        return true;
    }

    public boolean setYawPitch(@NotNull BlockPos pos) {
        if (this.rotateMode.is("Off") || this.rotateMode.is("Break")) {
            return true;
        }
        float[] angle = MathsUtil.calcAngle(CrystalAura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() + 0.5f), (double)((float)pos.getZ() + 0.5f)));
        float yaw = angle[0];
        float pitch = angle[1];
        float spoofedYaw = WurstplusThree.ROTATION_MANAGER.getSpoofedYaw();
        if (Math.abs(spoofedYaw - yaw) > (float)this.maxYaw.getValue().intValue() && Math.abs(spoofedYaw - 360.0f - yaw) > (float)this.maxYaw.getValue().intValue() && Math.abs(spoofedYaw + 360.0f - yaw) > (float)this.maxYaw.getValue().intValue()) {
            WurstplusThree.ROTATION_MANAGER.setPlayerRotations(Math.abs(spoofedYaw - yaw) < 180.0f ? (spoofedYaw > yaw ? spoofedYaw - (float)this.maxYaw.getValue().intValue() : spoofedYaw + (float)this.maxYaw.getValue().intValue()) : (spoofedYaw > yaw ? spoofedYaw + (float)this.maxYaw.getValue().intValue() : spoofedYaw - (float)this.maxYaw.getValue().intValue()), pitch);
            return false;
        }
        WurstplusThree.ROTATION_MANAGER.setPlayerRotations(yaw, pitch);
        return true;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.renderMap.isEmpty()) {
            return;
        }
        boolean outline = false;
        boolean solid = false;
        switch (this.mode.getValue()) {
            case "Pretty": {
                outline = true;
                solid = true;
                break;
            }
            case "Solid": {
                outline = false;
                solid = true;
                break;
            }
            case "Outline": {
                outline = true;
                solid = false;
            }
        }
        ArrayList<RenderPos> toRemove = new ArrayList<RenderPos>();
        for (RenderPos renderPos : this.renderMap) {
            int fillAlpha = this.renderFillColour.getValue().getAlpha();
            int boxAlpha = this.renderBoxColour.getValue().getAlpha();
            if (this.currentTargets.contains((Object)renderPos.pos)) {
                renderPos.fadeTimer = 0.0;
            } else if (!this.fade.getValue().booleanValue()) {
                toRemove.add(renderPos);
            } else {
                renderPos.fadeTimer += 1.0;
                fillAlpha = (int)((double)fillAlpha - (double)fillAlpha * (renderPos.fadeTimer / (double)this.fadeTime.getValue().intValue()));
                boxAlpha = (int)((double)boxAlpha - (double)boxAlpha * (renderPos.fadeTimer / (double)this.fadeTime.getValue().intValue()));
            }
            if (renderPos.fadeTimer > (double)this.fadeTime.getValue().intValue()) {
                toRemove.add(renderPos);
            }
            if (toRemove.contains(renderPos)) continue;
            RenderUtil.drawBoxESP(this.flat.getValue() != false ? new BlockPos(renderPos.pos.getX(), renderPos.pos.getY() + 1, renderPos.pos.getZ()) : renderPos.pos, new Colour(this.renderFillColour.getValue().getRed(), this.renderFillColour.getValue().getGreen(), this.renderFillColour.getValue().getBlue(), Math.max(fillAlpha, 0)), new Colour(this.renderBoxColour.getValue().getRed(), this.renderBoxColour.getValue().getGreen(), this.renderBoxColour.getValue().getBlue(), Math.max(boxAlpha, 0)), this.width.getValue().intValue(), outline, solid, true, this.flat.getValue() != false ? this.height.getValue() : 0.0, false, false, false, false, 0);
            if (!this.renderDamage.getValue().booleanValue()) continue;
            RenderUtil.drawText(renderPos.pos, String.valueOf(MathsUtil.roundAvoid(renderPos.damage, 1)), Gui.INSTANCE.customFont.getValue());
        }
        this.renderMap.removeAll(toRemove);
    }

    @Override
    public void onEnable() {
        this.placeTimeout = this.placeDelay.getValue();
        this.breakTimeout = this.breakDelay.getValue();
        this.placeTimeoutFlag = false;
        this.ezTarget = null;
        this.facePlacing = false;
        this.attemptedCrystals.clear();
        this.hasPacketBroke = false;
        this.alreadyAttacking = false;
        this.obiFeetCounter = 0;
        this.crystalLatency = 0L;
        this.start = 0L;
        this.staticEnderCrystal = null;
        this.staticPos = null;
        this.crystalsPlaced = 0;
        this.crystalsPlacedTimer.reset();
    }

    public float getCPS() {
        return (float)this.crystalsPlaced / ((float)this.crystalsPlacedTimer.getPassedTimeMs() / 1000.0f);
    }

    @Override
    public String getDisplayInfo() {
        switch (this.arrayListMode.getValue()) {
            case "Latency": {
                return this.crystalLatency + "ms";
            }
            case "CPS": {
                return "" + MathsUtil.round(this.getCPS(), 2);
            }
            case "Player": {
                return this.ezTarget != null ? this.ezTarget.getName() : null;
            }
        }
        return "";
    }

    static class DamageComparator
    implements Comparator<RenderPos> {
        DamageComparator() {
        }

        @Override
        public int compare(RenderPos a, RenderPos b) {
            return b.damage.compareTo(a.damage);
        }
    }

    static class RenderPos {
        Double damage;
        double fadeTimer;
        BlockPos pos;

        public RenderPos(BlockPos pos, Double damage) {
            this.pos = pos;
            this.damage = damage;
        }
    }
}

