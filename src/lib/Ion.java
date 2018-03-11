package lib;

import lib.FileUtilManager;

import java.util.*;

public class Ion {
    private FileUtilManager fileManager;
    public Ion(FileUtilManager manager) {
        this.fileManager = manager;
    }

    // Minify code, saving space.
    public String Minify(String text) {
        return text.replaceAll("[\\r\\n]", "");
    }

    public String Parse() {
        String ionCode = this.fileManager.GetContents();

        /* Interpreter handles single character shortcuts here, e.g. * is turned into !important. */
        String replacements[][] = {
                {"*"},
                {"!important"}
        };
        Map<String, String> replacementsMap = new Hashtable<String, String>();
        for(int i = 0; i < replacements[0].length; i++) {
            replacementsMap.put(replacements[0][i], replacements[1][i]);
        }
        String[] ionCodeArray = ionCode.split("");
        for(int x = 0; x < ionCodeArray.length; x++) {
            for(String key : replacementsMap.keySet()) {
                if(ionCodeArray[x].equals(key)) {
                    ionCodeArray[x] = replacementsMap.get(key);
                }
            }
        }
        ionCode = String.join("", ionCodeArray);

        /* Interpreter handles words etc here. */
        String[][] INTERNAL = {
                {"blue450"},
                {"#EF3540"}
        };
        Map<String, String> interpreter = new Hashtable<String, String>();

        ionCodeArray = ionCode.split(" ");
        for(int r = 0; r < INTERNAL[0].length; r++) {
            interpreter.put(INTERNAL[0][r], INTERNAL[1][r]);
        }
        for(int l = 0; l < ionCodeArray.length; l++) {
            for(String key : interpreter.keySet()) {
                if(ionCodeArray[l].trim().contains(";")) {
                    if(ionCodeArray[l].trim().equals(key + ";")) {
                        ionCodeArray[l] = interpreter.get(key) + ";\n";
                    }
                } else {
                    if(ionCodeArray[l].trim().equals(key)) {
                        ionCodeArray[l] = interpreter.get(key);
                    }
                }
            }
        }
        ionCode = String.join("", ionCodeArray);
        return ionCode;
    }
}
