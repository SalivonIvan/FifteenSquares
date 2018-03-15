package com.fifteen.squares;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.VideoService;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

    private Setting() {
    }

    public static void settingFoneMusic() {

//        if (mp == null) {
//            System.out.println("PLAY_FONE1");
//            Media foneMusic = new Media(Setting.class.getResource("fone.mp3").toString());
//            mp = new MediaPlayer(foneMusic);
//            mp.setCycleCount(MediaPlayer.INDEFINITE);
//            mp.setVolume(DEFAULT_SOUND_VOLUME);
//            System.out.println("PLAY_FONE2");
//        }
        if (mp == null) {
            System.out.println("PLAY_FONE1");
            Services.get(VideoService.class).ifPresent(service -> {
                service.setControlsVisible(true);
                service.getPlaylist().add(Setting.class.getClassLoader().getResource("/com/fifteen/squares/fone.mp3").toString());
                service.play();
                System.out.println("PLAY_FONE2");
            });
//            System.out.println("PLAY_FONE1");
//            Media foneMusic = new Media(Setting.class.getResource("fone.mp3").toString());
//            mp = new MediaPlayer(foneMusic);
//            mp.setCycleCount(MediaPlayer.INDEFINITE);
//            mp.setVolume(DEFAULT_SOUND_VOLUME);
//            System.out.println("PLAY_FONE2");
        }

//        if (SOUND)
//            mp.setAutoPlay(true);
        System.out.println("PLAY_FONE3");
    }
}
