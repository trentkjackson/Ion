package core;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeneralUtils {
    // Identifiers for the file size function in "FileUtilManager"
    public enum BYTE_SIZES {
        BYTES, KILOBYTES, MEGABYTES
    }

    // Returns the nth index of x in the given string.
    public static int nthIndexOf(String str, String indexed, int n) {
        int detected = 0;
        try {
            for(int i = 0; i < str.length(); i++) {
                if(str.split("")[i].equals(indexed)) {
                    if(detected == n) {
                        return i;
                    } else {
                        detected++;
                    }
                }
            }
        } catch(Exception err) {
            err.printStackTrace();
        }
        return -1;
    }

    public static float RoundToNthDecimal(float number, int place) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(place, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}


