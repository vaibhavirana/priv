package gifteconomy.dem.com.gifteconomy.utils;

import android.content.Context;

/**
 * Created by raghav on 18/5/16.
 */
public class PrefsUtil {

    private static String Login = "Login";
    private static String GuideRead = "GuideRead";
    private static String Silent = "Silent";
    private static String Vibrate = "Vibrate";
    private static String Shake = "Shake";
    private static String Popup = "Popup";

    public static void setLogin(Context context, boolean isLogin) {
        Prefs.with(context).save(Login, isLogin);
    }

    public static boolean getLogin(Context context) {
        return Prefs.with(context).getBoolean(Login, false);
    }


    public static boolean getIsGuideRead(Context context) {
        return Prefs.with(context).getBoolean(GuideRead, false);
    }

    public static void setIsGuideRead(Context context, boolean isGuideRead) {
        Prefs.with(context).save(GuideRead, isGuideRead);
    }

    public static void setSilent(Context context, boolean isSilent) {
        Prefs.with(context).save(Silent, isSilent);
    }

    public static boolean getSilent(Context context) {
        return Prefs.with(context).getBoolean(Silent, false);
    }

    public static boolean getVibrate(Context context) {
        return Prefs.with(context).getBoolean(Vibrate, false);
    }

    public static void setVibrate(Context context, Boolean isVibrate) {
        Prefs.with(context).save(Vibrate, isVibrate);
    }

    public static boolean getShake(Context context) {
        return Prefs.with(context).getBoolean(Shake, false);
    }

    public static void setShake(Context context, Boolean isShake) {
        Prefs.with(context).save(Shake, isShake);
    }

    public static int getPopup(Context context) {
        // 0 for No Pop-up
        // 1 for Only when screen is ON
        // 2 for Always show Pop-up
        return Prefs.with(context).getInt(Popup, 0);
    }

    public static void setPopup(Context context, int which) {
        Prefs.with(context).save(Popup, which);
    }
}
