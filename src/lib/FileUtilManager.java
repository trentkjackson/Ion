package lib;
import java.io.*;
import static lib.GeneralUtils.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.file.*;
import lib.Ion;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileUtilManager {
    private String path;
    private String outputPath;
    public FileUtilManager(String _path) {
        path = _path;
    }

    // Checks if file exists at the given path.
    public boolean ExistsAtPath() {
        return new File(this.path).isFile();
    }

    // Event which is triggered when file of the class has been altered.
    private void OnFileChange() {
        Ion interpreter = new Ion(this);
        this.UpdateCSSOutput(interpreter.Minify(interpreter.Parse()));
        System.out.println("[Detected] Change has been written to " + this.outputPath + "(" + RoundToNthDecimal(this.GetSize(BYTE_SIZES.KILOBYTES, this.outputPath), 3) + "kb)");
    }

    private void OnStartWatching() {
        this.InstantiateCSSOutput();
        System.out.println("Now watching \"" + this.path + "\"");
    }

    // Event which is triggered on the watch key for the classes given file has be unregistered.
    private void OnWatchKeyUnregistered() {}

    // Removes extension off a string of a file name.
    private String TruncateExtension(String absFile) {
        return absFile.substring(0, absFile.indexOf("."));
    }

    // Beings watching the files directory and only triggered events for the given file's change in state.
    public void WatchForChange() {
        this.OnStartWatching();
        Path dir = FileSystems.getDefault().getPath(System.getProperty("user.home"), this.GetDirectoryFromUser().replaceAll("\\\\", "/"));
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey watchKey = dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while(true) {
                final WatchKey key = watchService.take();
                for (WatchEvent<?> e : key.pollEvents()) {
                    Path changed = (Path) e.context();
                    if(changed.endsWith(this.GetFileName())) { this.OnFileChange(); }
                } if (!key.reset()) { this.OnWatchKeyUnregistered(); }
            }
        } catch(IOException|InterruptedException|InvalidPathException err) {
            err.printStackTrace();
        }
    }

    // Returns the files absolute directory.
    public String GetPathDirectory() {
        String[] reversedPath = new StringBuilder(this.path).reverse().toString().split("");
        int detected = 0;
        for(int i = 0; i < reversedPath.length; i++) {
            if(reversedPath[i].equals("\\") || reversedPath[i].equals("/")) {
                reversedPath[i] = "\0";
                break;
            } else {
                reversedPath[i] = "\0";
            }
        }
        return new StringBuilder(String.join("", reversedPath)).reverse().toString().trim();
    }

    // Returns the absolute path of the file but only begins after their root drive and user folder, e.g.
    // "C:/Users/trent" will not be included so it will look something like this... "Desktop/lib.Ion"
    public String GetDirectoryFromUser() {
        return this.GetPathDirectory().substring(nthIndexOf(this.GetPathDirectory(), "\\", 2) + 1, this.GetPathDirectory().length()).trim();
    }

    // Returns the file name with it's extension included.
    public String GetFileName() {
        return this.path.substring(this.path.lastIndexOf("\\") + 1, this.path.length()).trim();
    }

    // Check if the path has the extension given.
    public boolean HasExtension(String extension) {
        String fileExtension = this.path.substring(this.path.indexOf("."), this.path.length());
        return fileExtension.equals(extension);
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

    private void InstantiateCSSOutput() {
        this.outputPath = this.GetPathDirectory() + "\\" + this.TruncateExtension(this.GetFileName()) + ".css";
        if(new File(this.outputPath).isFile()) { return; }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputPath))) {
            writer.write("/* Generated by lib.Ion */");
            writer.flush();
        } catch(Exception err) {
            err.printStackTrace();
        }
    }

    public void UpdateCSSOutput(String content) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.outputPath))) {
            bufferedWriter.write("");
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch(Exception err) {
            err.printStackTrace();
        }
    }

    /* Returns the size of a file in either of the following formats
     - BYTES
     - KILOBYTES
     - MEGABYTES (Most likely will be a absurdly small size if there is not much written in the file)
     */
    public float GetSize(BYTE_SIZES format, String path) {
        float size = new File(path).length();
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
