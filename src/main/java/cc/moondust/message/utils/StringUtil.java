package cc.moondust.message.utils;

/**
 * Created by j0 on 2016/8/1.
 */
public class StringUtil {

    public static boolean empty(String str) {
        if (str == null || str.trim().length() <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
