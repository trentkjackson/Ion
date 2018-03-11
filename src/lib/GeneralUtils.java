package lib;

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
}


