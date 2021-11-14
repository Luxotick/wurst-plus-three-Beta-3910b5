/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.crash.CrashReport
 *  org.lwjgl.Sys
 *  org.lwjgl.input.Keyboard
 */
package me.travis.wurstplusthree.mixin.mixins;

import javax.annotation.Nullable;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.CustomSplashScreen;
import me.travis.wurstplusthree.hack.hacks.client.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    private long lastFrame = this.getTime();

    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen var1);

    @Inject(method={"runTickKeyboard"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", ordinal=0)}, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void onRunTickKeyboard(CallbackInfo ci, int i) {
        if (Keyboard.getEventKeyState() && WurstplusThree.HACKS != null) {
            WurstplusThree.HACKS.onKeyDown(i);
        }
    }

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void runGameLoop(CallbackInfo callbackInfo) {
        long currentTime = this.getTime();
        int deltaTime = (int)(currentTime - this.lastFrame);
        this.lastFrame = currentTime;
        WurstplusThree.RENDER_UTIL_2D.setDeltaTime(deltaTime);
    }

    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Inject(method={"runTick()V"}, at={@At(value="RETURN")})
    private void runTick(CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && Gui.INSTANCE.customScreen.getValue().booleanValue()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new CustomSplashScreen());
        }
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu && Gui.INSTANCE.customScreen.getValue().booleanValue()) {
            this.displayGuiScreen(new CustomSplashScreen());
        }
    }

    @Redirect(method={"run"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Redirect(method={"runTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
    public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    public void shutdownHook(CallbackInfo info) {
        this.unload();
    }

    private void unload() {
        System.out.println("unloading wurst+3");
        WurstplusThree.unLoad();
        System.out.println("wurst+3 has been unloaded");
    }
}

