import core.FileUtilManager;

public class main {
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

        FileUtilManager fileUtilManager = new FileUtilManager(file, "win");
        fileUtilManager.WatchForChange();
    }
}
