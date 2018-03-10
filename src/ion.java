import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import lib.FileUtilManager;
public class ion {
    public static void main(String[] args) {
        String file;
        /*
        try {
            file = args[1];
        } catch(ArrayIndexOutOfBoundsException err) {
            System.out.println("Please supply the file's location in the argv.");
            err.printStackTrace();
            return;
        }
        */

        // production only
            file = "/Users/macair/Desktop/ion/test/test.txt";

        FileUtilManager fileUtilManager = new FileUtilManager(file);
        System.out.println(fileUtilManager.GetContents());
    }
}
