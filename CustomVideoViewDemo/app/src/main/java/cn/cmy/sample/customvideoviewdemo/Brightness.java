package cn.cmy.sample.customvideoviewdemo;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Brightness {

    //判断是否自动调节亮度
    public static boolean isAutoBrightness(Context context) {

        int autoBrightness = 0;

        try {
            autoBrightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (autoBrightness == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            return true;
        }
        return false;
    }

    //关闭系统自动亮度调节
    public static void closeAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }


    /**
     * 获取系统亮度
     *
     * @return
     */
    public static int getSystemBrightness(Context context) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }


    /**
     * 加大系统亮度
     *
     * @param brightness
     */
    public static void turnUpSystemBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            //默认情况下，当我们直接修改了系统亮度值后，
            // 当前Window中是可以即时反应出来亮度效果的，
            // 这是因为默认情况下，
            // WindowManager.LayoutParams的screenBrightness的
            // 默认值为WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            //即表示Window没有自己的亮度参数，将依随系统亮度效果的变化而变化。
            // 这也就是我们最常见的：当调整系统亮度后，
            // 所有Window都即时反应出系统亮度设置效果
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

        } else {
            //屏幕最大亮度为255。
            //屏幕最低亮度为0。
            //屏幕亮度值范围必须位于：0～255。
            //注意的是此处的brightness是一个0.0-1.0之间的一个float类型数值
            lp.screenBrightness += (brightness <= 0 ? 0 : brightness / 255f);
        }
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1.0f;
        }
        window.setAttributes(lp);

    }

    /**
     * 减小系统亮度
     *
     * @param brightness
     */
    public static void turnDownSystemBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {

            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

        } else {
            lp.screenBrightness -= (brightness <= 0 ? 0 : brightness / 255f);
        }
        if (lp.screenBrightness < 0) {
            lp.screenBrightness = 0.0f;
        }
        window.setAttributes(lp);

    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static void setScreenMode(Context context, int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 设置当前屏幕亮度值  0--255
     */
    public static void saveScreenBrightness(Context context, int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


}
