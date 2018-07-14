package cn.cmy.sample.customvideoviewdemo;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class AudioUtil {

    public static void turnUp(Context context, float percent) {

        AudioManager audioManager = getAudioManager(context);
        //current audio
        int streamVolume = getStreamVolume(context);
        //max audio
        int streamMaxVolume = getStreamMaxVolume(context);
        int index = (int) (streamVolume + percent * streamMaxVolume);
        if (index > streamMaxVolume) {
            return;
        }
        setStreamVolume(audioManager, AudioManager.STREAM_MUSIC, index, 0);

    }

    private static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.
                getSystemService(Context.AUDIO_SERVICE);
    }

    public static void turnDown(Context context, float percent) {
        AudioManager audioManager = getAudioManager(context);
        int streamVolume = getStreamVolume(context);
        int streamMaxVolume = getStreamMaxVolume(context);
        int index = (int) (streamVolume - percent * streamMaxVolume);
        if (index < 0) {
            return;
        }
        setStreamVolume(audioManager, AudioManager.STREAM_MUSIC, index, 0);

    }

    public static void setStreamVolume(AudioManager audioManager,
                                       int streamTypes, int index, int flag) {
        audioManager.setStreamVolume(streamTypes, index, flag);
    }


    public static int getStreamMaxVolume(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).
                getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    public static int getStreamVolume(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).
                getStreamVolume(AudioManager.STREAM_MUSIC);

    }

}
