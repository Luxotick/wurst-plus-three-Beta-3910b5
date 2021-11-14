/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.ISound$AttenuationType
 *  net.minecraft.client.audio.Sound
 *  net.minecraft.client.audio.Sound$Type
 *  net.minecraft.client.audio.SoundEventAccessor
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 */
package me.travis.wurstplusthree.util.elements.songs;

import javax.annotation.Nullable;
import me.travis.wurstplusthree.util.Globals;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class HotelRoom
implements Globals {
    public static final ISound sound;
    private static final String song = "hotelroom";
    private static final ResourceLocation loc;

    static {
        loc = new ResourceLocation("sounds/hotelroom.ogg");
        sound = new ISound(){
            private final int pitch = 1;
            private final int volume = 1;

            public ResourceLocation getSoundLocation() {
                return loc;
            }

            @Nullable
            public SoundEventAccessor createAccessor(SoundHandler soundHandler) {
                return new SoundEventAccessor(loc, "Pitbull");
            }

            public Sound getSound() {
                return new Sound(HotelRoom.song, 1.0f, 1.0f, 1, Sound.Type.SOUND_EVENT, false);
            }

            public SoundCategory getCategory() {
                return SoundCategory.VOICE;
            }

            public boolean canRepeat() {
                return true;
            }

            public int getRepeatDelay() {
                return 2;
            }

            public float getVolume() {
                return 1.0f;
            }

            public float getPitch() {
                return 1.0f;
            }

            public float getXPosF() {
                return 1.0f;
            }

            public float getYPosF() {
                return 0.0f;
            }

            public float getZPosF() {
                return 0.0f;
            }

            public ISound.AttenuationType getAttenuationType() {
                return ISound.AttenuationType.LINEAR;
            }
        };
    }
}

