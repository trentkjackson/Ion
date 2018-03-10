package lib;
import java.io.*;

public class FileUtilManager {
    private String path;
    public FileUtilManager(String _path) {
        path = _path;
    }
    public String GetContents() {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.path));
        } catch(FileNotFoundException err) {
            err.printStackTrace();
            return null;
        }
        String contents = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine = bufferedReader.readLine();
            while(currentLine != null) {
                stringBuilder.append(currentLine);
                stringBuilder.append(System.lineSeparator());
                currentLine = bufferedReader.readLine();
            }
            contents = stringBuilder.toString();
        } catch(IOException err) {
            err.printStackTrace();
        }
        return contents;
    }
    // Getters and Setters.
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
