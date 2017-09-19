package com.edgardrake.flameseeker.lib.utilities;

import android.content.res.Resources;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Edgar Drake on 31-Jul-17.
 */

public class NumberUtils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getCurrency(double value){
        Locale id = new Locale("in","ID");
        return NumberFormat.getInstance(id).format(value);
    }

    public static boolean isNumber(CharSequence number) {
        return number.toString().matches("[0-9]+");
    }
}
