/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package me.travis.wurstplusthree.manager;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javax.imageio.ImageIO;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.Pair;
import net.minecraft.util.ResourceLocation;

public class CapeManager
implements Globals {
    private final List<UUID> ogCapes = new ArrayList<UUID>();
    private final List<Pair<UUID, BufferedImage>> donatorCapes = new ArrayList<Pair<UUID, BufferedImage>>();
    private final List<UUID> cooldudeCapes = new ArrayList<UUID>();
    private final List<UUID> contributorCapes = new ArrayList<UUID>();
    private final List<ResourceLocation> ogCapeFrames = new ArrayList<ResourceLocation>();
    private final List<ResourceLocation> coolCapeFrames = new ArrayList<ResourceLocation>();
    public static int capeFrameCount = 0;

    public ResourceLocation getOgCape() {
        return this.ogCapeFrames.get(capeFrameCount % 35);
    }

    public ResourceLocation getCoolCape() {
        return this.coolCapeFrames.get(capeFrameCount % 35);
    }

    public CapeManager() {
        String inputLine;
        BufferedReader in;
        URL capesList2;
        int i;
        Timer timer = new Timer();
        timer.schedule((TimerTask)new gifCapeCounter(), 0L, 41L);
        for (i = 0; i < 35; ++i) {
            this.ogCapeFrames.add(new ResourceLocation("textures/gifcape/cape-" + i + ".png"));
        }
        for (i = 0; i < 37; ++i) {
            this.coolCapeFrames.add(new ResourceLocation("textures/gifcape2/w3templateblackborder-" + i + ".png"));
        }
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/ogs.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.ogCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception capesList2) {
            // empty catch block
        }
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/dev.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.contributorCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/cooldude.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.cooldudeCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception capesList3) {
            // empty catch block
        }
        try {
            String inputLine2;
            File tmp = new File("Wurstplus3" + File.separator + "capes");
            if (!tmp.exists()) {
                tmp.mkdirs();
            }
            URL capesList4 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/donator.txt");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(capesList4.openStream()));
            while ((inputLine2 = in2.readLine()) != null) {
                String colune = inputLine2.trim();
                String uuid = colune.split(":")[0];
                String cape = colune.split(":")[1];
                URL capeUrl = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/capes/" + cape + ".png");
                BufferedImage capeImage = ImageIO.read(capeUrl);
                ImageIO.write((RenderedImage)capeImage, "png", new File("Wurstplus3/capes/" + uuid + ".png"));
                this.donatorCapes.add(new Pair<UUID, BufferedImage>(UUID.fromString(uuid), capeImage));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void reload() {
        String inputLine;
        BufferedReader in;
        URL capesList2;
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/ogs.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.ogCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception capesList2) {
            // empty catch block
        }
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/dev.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.contributorCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception capesList3) {
            // empty catch block
        }
        try {
            capesList2 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/cooldude.txt");
            in = new BufferedReader(new InputStreamReader(capesList2.openStream()));
            while ((inputLine = in.readLine()) != null) {
                this.cooldudeCapes.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception capesList4) {
            // empty catch block
        }
        try {
            String inputLine2;
            File tmp = new File("Wurstplus3" + File.separator + "capes");
            if (!tmp.exists()) {
                tmp.mkdirs();
            }
            URL capesList5 = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/donator.txt");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(capesList5.openStream()));
            while ((inputLine2 = in2.readLine()) != null) {
                String colune = inputLine2.trim();
                String uuid = colune.split(":")[0];
                String cape = colune.split(":")[1];
                URL capeUrl = new URL("https://raw.githubusercontent.com/WurstPlus/capes/main/capes/" + cape + ".png");
                BufferedImage capeImage = ImageIO.read(capeUrl);
                ImageIO.write((RenderedImage)capeImage, "png", new File("Wurstplus3/capes/" + uuid + ".png"));
                this.donatorCapes.add(new Pair<UUID, BufferedImage>(UUID.fromString(uuid), capeImage));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean isOg(UUID uuid) {
        return this.ogCapes.contains(uuid);
    }

    public boolean isDonator(UUID uuid) {
        for (Pair<UUID, BufferedImage> donator : this.donatorCapes) {
            if (!donator.getKey().toString().equalsIgnoreCase(uuid.toString())) continue;
            return true;
        }
        return false;
    }

    public BufferedImage getCapeFromDonor(UUID uuid) {
        for (Pair<UUID, BufferedImage> donator : this.donatorCapes) {
            if (!donator.getKey().toString().equalsIgnoreCase(uuid.toString())) continue;
            return donator.getValue();
        }
        return null;
    }

    public boolean isCool(UUID uuid) {
        return this.cooldudeCapes.contains(uuid);
    }

    public boolean isContributor(UUID uuid) {
        return this.contributorCapes.contains(uuid);
    }

    static class gifCapeCounter
    extends TimerTask {
        gifCapeCounter() {
        }

        @Override
        public void run() {
            ++capeFrameCount;
        }
    }
}

