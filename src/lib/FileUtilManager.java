package lib;
import java.io.*;

public class FileUtilManager {
    private String path;
    public FileUtilManager(String _path) {
        path = _path;
    }

    // Checks if file exists at the given path.
    public boolean Exists() {
        return new File(this.path).isFile();
    }

    // Returns content of a file at the given path.
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
            return null;
        }
        return contents;
    }

    public float GetSize(GeneralUtils.BYTE_SIZES format) {
        float size = new File(this.path).length();
        switch(format) {
            case BYTES:
                return size;
            case KILOBYTES:
                return size * (float) Math.pow(10, -3);
            case MEGABYTES:
                return size * (float) Math.pow(10, -6);
            default:
                return size;
        }
    }
    // Getters and Setters.
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
