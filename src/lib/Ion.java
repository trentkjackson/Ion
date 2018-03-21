package lib;

import lib.FileUtilManager;

import java.lang.reflect.Array;
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
        ionCode = ionCode.trim().replaceAll("\n", " ");

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
                // Entry[0][7] will be replaced with Entry[1][7] etc etc.
                {"red50", "red100", "red150", "red200", "red300", "red400", "red500", "red600", "red700", "red800",
                "red900", "red100A", "red200A", "red400A", "red700A", "pink50", "pink100", "pink200", "pink300",
                "pink400", "pink500", "pink500", "pink600", "pink700", "pink800", "pink900", "pink100A", "pink200A"},
                {"#FFEBEEE", "#FFCDD2", "#EF9A9A", "#E57373", "#EF5350", "#F44336", "#E53935", "#D32F2F", "#C62828",
                "#B71C1C", "#FF8A80", "#FF5252", "#FF1744", "#D50000", "#FCE4EC", "#F8BBD0", "#F48FB1", "#F06292",
                "#EC407A", "#E91E63", "#D81B60", "#C2185B", "#AD1457", "#880E4F", "#FF80AB", "#FF4081", "#F50057",
                "#C51162"}
        };

        List<String> INTERNAL_VARIABLE = new ArrayList<>(Arrays.asList(INTERNAL[0]));
        List<String> INTERNAL_REPLACEMENT = new ArrayList<>(Arrays.asList(INTERNAL[1]));


        Map<String, String> interpreter = new Hashtable<>();

        ionCodeArray = ionCode.split(" ");

        for(int j = 0; j < ionCodeArray.length; j++) {
            if(ionCodeArray[j].trim().contains("(") && ionCodeArray[j].contains(")") && ionCodeArray[j].trim().contains(":")) {
                String variableName = ionCodeArray[j].substring(0, ionCodeArray[j].indexOf(":")).replaceAll(":", "").trim();
                String variable = ionCodeArray[j + 1].substring(0, ionCodeArray[j + 1].indexOf(";")).replaceAll(";", "").trim();
                INTERNAL_VARIABLE.add(variableName.trim());
                INTERNAL_REPLACEMENT.add(variable.trim());
            }
        }

        for(int r = 0; r < INTERNAL_VARIABLE.size(); r++) {
            interpreter.put(INTERNAL_VARIABLE.get(r), INTERNAL_REPLACEMENT.get(r));
        }

        interpreter.forEach((k, v) -> {
           System.out.println(k + "-->" + v);
        });

        for(int l = 0; l < ionCodeArray.length; l++) {
            for(String key : interpreter.keySet()) {
                String formattedKey = ionCodeArray[l].trim().replaceAll("\n", "");
                if(formattedKey.contains("(") && formattedKey.contains(")") && formattedKey.contains(":")) {
                    ionCodeArray[l] = "";
                    ionCodeArray[l + 1] = ionCodeArray[l + 1].substring(ionCodeArray[l + 1].indexOf(";") + 1, ionCodeArray[l + 1].length());
                }
                if(formattedKey.contains(";") && !formattedKey.contains(".")) {
                    ionCodeArray[l] = ionCodeArray[l].substring(0, ionCodeArray[l].length()).trim();
                    if(ionCodeArray[l].trim().equals(key + ";")) {
                        ionCodeArray[l] = interpreter.get(key) + ";\n";
                    }
                } else {
                    if(ionCodeArray[l].trim().equals(key)) {
                        ionCodeArray[l] = interpreter.get(key) + ";";
                    }
                }
            }
        }

        ionCode = String.join("", ionCodeArray);
        return ionCode;
    }
}


        /*
        for(int j = 0; j < ionCodeArray.length; j++) {
            if(ionCodeArray[j].startsWith("(") && ionCodeArray[j].contains(")") && ionCodeArray[j].endsWith(":")) {
                System.out.println(ionCodeArray[j].replace(":", "").trim());
                String variableName = ionCodeArray[j].replace(":", "").trim();
                String variable = ionCodeArray[j + 1].replace(";", "").trim();
                String[][] INTERNAL_NEW = new String[2][INTERNAL[0].length + 1];
                for(int i = 0; i < INTERNAL[1].length; i++) {
                    INTERNAL_NEW[0][i] = INTERNAL[0][i];
                    INTERNAL_NEW[1][i] = INTERNAL[1][i];
                }
                INTERNAL_NEW[0][INTERNAL_NEW[0].length - 1] = variableName.trim();
                INTERNAL_NEW[1][INTERNAL_NEW[0].length - 1] = variable.trim();
                Arrays.stream(INTERNAL_NEW[0]).forEach(i -> System.out.println(i));
                INTERNAL = INTERNAL_NEW;
            }
        }
        */