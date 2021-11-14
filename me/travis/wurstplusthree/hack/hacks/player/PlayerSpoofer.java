/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package me.travis.wurstplusthree.hack.hacks.player;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.util.ClientMessage;

@Hack.Registration(name="Player Spoofer", description="spoofs you name and skin", category=Hack.Category.PLAYER)
public class PlayerSpoofer
extends Hack {
    public static PlayerSpoofer INSTANCE;
    public String name = "travis";
    public File tmp;

    public PlayerSpoofer() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        try {
            this.tmp = new File("Wurstplus3" + File.separator + "tmp");
            if (!this.tmp.exists()) {
                this.tmp.mkdirs();
            }
            Gson gson = new Gson();
            if (this.name == null) {
                ClientMessage.sendErrorMessage("Please set the player name!");
                this.disable();
            }
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + this.name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Map map = (Map)gson.fromJson((Reader)reader, Map.class);
            ConcurrentHashMap<String, String> valsMap = new ConcurrentHashMap<String, String>();
            for (Map.Entry entry : map.entrySet()) {
                String key = (String)entry.getKey();
                String val = (String)entry.getValue();
                valsMap.put(key, val);
            }
            ((Reader)reader).close();
            String uuid = (String)valsMap.get("id");
            URL url2 = new URL("https://mc-heads.net/skin/" + uuid);
            BufferedImage image = ImageIO.read(url2);
            ImageIO.write((RenderedImage)image, "png", new File("Wurstplus3/tmp/skin.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.deleteSkinChangerFiles();
    }

    public void deleteSkinChangerFiles() {
        for (File file : PlayerSpoofer.mc.gameDir.listFiles()) {
            if (file.isDirectory() || !file.getName().contains("-skinchanger")) continue;
            file.delete();
        }
    }

    public String getOldName() {
        return mc.getSession().getUsername();
    }

    @Override
    public String getDisplayInfo() {
        return this.name;
    }
}

