/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.alt.defult.GuiAccountSelector;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.DonatorItem;
import me.travis.wurstplusthree.util.elements.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CustomSplashScreen
extends GuiScreen {
    private final ResourceLocation background = new ResourceLocation("textures/pitbull1.jpg");
    private final List<DonatorItem> donatorItems = new ArrayList<DonatorItem>();
    private int y;
    private int x;
    private float watermarkX;

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    private void playMusic() {
        if (!this.mc.soundHandler.isSoundPlaying(WurstplusThree.SONG_MANAGER.getMenuSong())) {
            this.mc.soundHandler.playSound(WurstplusThree.SONG_MANAGER.getMenuSong());
        }
    }

    private void initDonators() {
        this.donatorItems.clear();
        try {
            String inputLine;
            URL capesList = new URL("https://raw.githubusercontent.com/TrvsF/capes/main/boosts.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            while ((inputLine = in.readLine()) != null) {
                String colune = inputLine.trim();
                String name = colune.split(":")[0];
                String ammount = colune.split(":")[1];
                this.donatorItems.add(new DonatorItem(name, Integer.parseInt(ammount), WurstplusThree.GUI_FONT_MANAGER.getTextWidth(name), WurstplusThree.GUI_FONT_MANAGER.getTextHeight(), this.width, this.height));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGui() {
        this.mc.gameSettings.enableVsync = false;
        this.mc.gameSettings.limitFramerate = 200;
        this.initDonators();
        this.playMusic();
        this.x = this.width / 4;
        this.y = this.height / 4 + 48;
        this.watermarkX = this.width + 80;
        this.buttonList.add(new TextButton(0, this.x, this.y + 22, "singleplayer"));
        this.buttonList.add(new TextButton(1, this.x, this.y + 44, "the_fellas"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 66, "settings"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 88, "discord"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 110, "alts"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 132, "log"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (CustomSplashScreen.isHovered(this.x, this.y + 20, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("singleplayer"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 44, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("the_fellas"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 66, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("settings"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 88, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("discord"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://discord.gg/hvnZePKQHx"));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 110, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("alts"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiAccountSelector());
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 132, WurstplusThree.MENU_FONT_MANAGER.getTextWidth("log"), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float xOffset = -1.0f * (((float)mouseX - (float)this.width / 2.0f) / ((float)this.width / 32.0f));
        float yOffset = -1.0f * (((float)mouseY - (float)this.height / 2.0f) / ((float)this.height / 18.0f));
        this.x = this.width / 4;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.background);
        CustomSplashScreen.drawCompleteImage(-16.0f + xOffset, -9.0f + yOffset, this.width + 32, this.height + 18);
        String watermark = "Wurst+3 v" + "0.7.0+3910b5a53abaa7a012c8dd616a29ffd4d5397611" + " : made by travis#0001 | Madmeg#4882 - with help from BrownZombie, k3b, wallhacks#6969, and Austin :D";
        for (DonatorItem item : this.donatorItems) {
            item.updatePos();
            switch (item.getSize()) {
                case 1: {
                    WurstplusThree.DONATOR_FONT_MANAGER.drawSmallStringRainbow(item.getName(), (float)item.getX(), (float)item.getY(), item.getRgb());
                    break;
                }
                case 2: {
                    WurstplusThree.DONATOR_FONT_MANAGER.drawMediumStringRainbow(item.getName(), (float)item.getX(), (float)item.getY(), item.getRgb());
                    break;
                }
                case 3: {
                    WurstplusThree.DONATOR_FONT_MANAGER.drawLargeStringRainbow(item.getName(), (float)item.getX(), (float)item.getY(), item.getRgb());
                }
            }
        }
        WurstplusThree.GUI_FONT_MANAGER.drawStringRainbow(watermark, this.watermarkX, this.height - WurstplusThree.GUI_FONT_MANAGER.getTextHeight() - 2, true);
        this.watermarkX -= 0.05f;
        if (this.watermarkX < (float)(-WurstplusThree.GUI_FONT_MANAGER.getTextWidth(watermark) - 10)) {
            this.watermarkX = this.width + 40;
        }
        WurstplusThree.MENU_FONT_MANAGER.drawStringBig("WurstPlus 3", this.x, (float)this.y - 20.0f, Color.white.getRGB(), true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private static class TextButton
    extends GuiButton {
        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, WurstplusThree.MENU_FONT_MANAGER.getTextWidth(buttonText), WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float)mouseX >= (float)this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                WurstplusThree.MENU_FONT_MANAGER.drawStringWithShadow(this.displayString, (float)this.x + 1.0f, this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine((float)this.x - 5.0f, this.y + 2 + WurstplusThree.MENU_FONT_MANAGER.getTextHeight(), (float)this.x - 5.0f, this.y - 2, 2.0f, Rainbow.getColour().getRGB());
                }
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float)mouseX >= (float)this.x - (float)WurstplusThree.MENU_FONT_MANAGER.getTextWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }
}

