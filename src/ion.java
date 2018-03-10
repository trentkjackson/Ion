import lib.FileUtilManager;
import lib.GeneralUtils;

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
            file = "C:\\Users\\trent\\Desktop\\IonGit\\test\\test.txt";

        FileUtilManager fileUtilManager = new FileUtilManager(file);
        System.out.println(fileUtilManager.GetContents());
        System.out.println(fileUtilManager.GetSize(GeneralUtils.BYTE_SIZES.MEGABYTES));
    }
}
