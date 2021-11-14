/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.travis.wurstplusthree.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.cosmetics.GlassesModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;

public class CosmeticManager
implements Globals {
    public Map<String, ModelBase> cosmeticMap = new HashMap<String, ModelBase>();
    public static GlassesModel gm = new GlassesModel();

    public CosmeticManager() {
        try {
            String inputLine;
            URL capesList = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/cosmetics.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            while ((inputLine = in.readLine()) != null) {
                String colune = inputLine.trim();
                String name = colune.split(":")[0];
                String type2 = colune.split(":")[1];
                if (!type2.equals("glasses")) continue;
                this.cosmeticMap.put(name, gm);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.cosmeticMap.clear();
        try {
            String inputLine;
            URL capesList = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/cosmetics.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            while ((inputLine = in.readLine()) != null) {
                String colune = inputLine.trim();
                String name = colune.split(":")[0];
                String type2 = colune.split(":")[1];
                if (!type2.equals("glasses")) continue;
                this.cosmeticMap.put(name, gm);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelBase getRenderModels(EntityPlayer player) {
        return this.cosmeticMap.get(player.getUniqueID().toString());
    }

    public boolean hasCosmetics(EntityPlayer player) {
        return this.cosmeticMap.containsKey(player.getUniqueID().toString());
    }
}

