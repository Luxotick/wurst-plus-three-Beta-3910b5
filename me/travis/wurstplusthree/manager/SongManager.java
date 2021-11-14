/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 */
package me.travis.wurstplusthree.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.elements.songs.DontStop;
import me.travis.wurstplusthree.util.elements.songs.FireBall;
import me.travis.wurstplusthree.util.elements.songs.HotelRoom;
import net.minecraft.client.audio.ISound;

public class SongManager
implements Globals {
    private final List<ISound> songs = Arrays.asList(new ISound[]{FireBall.sound, HotelRoom.sound, DontStop.sound});
    private final ISound menuSong = this.getRandomSong();
    private ISound currentSong = this.getRandomSong();

    public ISound getMenuSong() {
        return this.menuSong;
    }

    public void skip() {
        boolean flag = this.isCurrentSongPlaying();
        if (flag) {
            this.stop();
        }
        this.currentSong = this.songs.get((this.songs.indexOf((Object)this.currentSong) + 1) % this.songs.size());
        if (flag) {
            this.play();
        }
    }

    public void play() {
        if (!this.isCurrentSongPlaying()) {
            SongManager.mc.soundHandler.playSound(this.currentSong);
        }
    }

    public void stop() {
        if (this.isCurrentSongPlaying()) {
            SongManager.mc.soundHandler.stopSound(this.currentSong);
        }
    }

    private boolean isCurrentSongPlaying() {
        return SongManager.mc.soundHandler.isSoundPlaying(this.currentSong);
    }

    public void shuffle() {
        this.stop();
        Collections.shuffle(this.songs);
    }

    private ISound getRandomSong() {
        return this.songs.get(random.nextInt(this.songs.size()));
    }
}

