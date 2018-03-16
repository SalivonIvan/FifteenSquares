package com.fifteen.squares;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.VideoService;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Optional;

/**
 * Created by ivan salivon on 15.03.18.
 */
public class Setting {

    public static final String LEVEL_COMPLEXITY = "level.complexity";
    public static final String DEFAULT_LEVEL_COMPLEXITY = "10";
    public static final boolean SOUND = true;
    public static final double DEFAULT_SOUND_VOLUME = 0.3;
    public static final boolean VIDEO = false;
    public static final boolean VIBRATION = false;

    private static MediaPlayer mp;
    private static Optional<VideoService> vs;

    private Setting() {
    }

    public static void settingFoneMusic() {

        if (Platform.isDesktop()) {
            if (mp == null) {
                Media foneMusic = new Media(Setting.class.getResource("fone.mp3").toString());
                mp = new MediaPlayer(foneMusic);
                mp.setCycleCount(MediaPlayer.INDEFINITE);
                mp.setVolume(DEFAULT_SOUND_VOLUME);
            }
            if (SOUND)
                mp.setAutoPlay(true);

        }
        if (Platform.isAndroid()) {
            if (vs == null)
                vs = Services.get(VideoService.class);

            vs.ifPresent(service -> {

                if (SOUND) {
                    service.getPlaylist().add("com/fifteen/squares/fone.mp3");
                    service.setLooping(true);
                    service.play();
                }

            });

        }


    }
}
