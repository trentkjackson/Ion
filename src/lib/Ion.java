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
        List<String> DontMinifyKeywords = new ArrayList<>(Arrays.asList("box-shadow:"));
        String encode_wanted_whitespace_array[] = ionCode.trim().replaceAll("\n", " ").split(" ");
        for(int i = 0; i < encode_wanted_whitespace_array.length; i++) {
            for(int x = 0; x < DontMinifyKeywords.size(); x++) {
                if(encode_wanted_whitespace_array[i].contains(DontMinifyKeywords.get(x))) {
                    int u = 1;
                    while(!encode_wanted_whitespace_array[i + u].contains(";")) {
                        encode_wanted_whitespace_array[i + u] = "%20%" + encode_wanted_whitespace_array[i + u];
                        u++;
                    }
                    encode_wanted_whitespace_array[i + u] = "%20%" + encode_wanted_whitespace_array[i + u];
                }
            }
        }

        ionCode = String.join(" ", encode_wanted_whitespace_array).trim().replaceAll("\n", " ");

        /* Interpreter handles single character shortcuts here, e.g. * is turned into !important. */
        String replacements[][] = {
                {"*"},
                {"!important"}
        };
        Map<String, String> replacementsMap = new Hashtable<>();
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

        // MIXINS
        List<String> MixinNames = new ArrayList<>();
        List<String> Mixins = new ArrayList<>();

        for(int i = 0; i < ionCodeArray.length; i++) {
            if(ionCodeArray[i].contains("(") && ionCodeArray[i].contains(")") && ionCodeArray[i].contains("$")) {
                String mixinName = ionCodeArray[i].trim().replaceAll("[()\n]", "");
                String mixinCode;
                StringBuilder mixinCodeBuilder = new StringBuilder();
                int m = 2;


                /*
                FIXME: Doesn't include long strings of code such as 'box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);' and 'transform: translateX(-50%, -50%);'
                ^^ FIXED BUT THERE IS NEW ERROR
                FIXME: minimized code is changing 'box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);' into 'box-shadow:10px10px5px0pxrgba(0,0,0,0.75);'
                ^^ fixed i think!
                 */
                while(!ionCodeArray[i + m].trim().contains("}") && !ionCodeArray[i + m].trim().equals("\n")) {
                    System.out.println(mixinName + "-+->" + ionCodeArray[i + m].trim().replaceAll("\n", ""));
                    mixinCodeBuilder.append(ionCodeArray[i + m].trim().replaceAll("\n", ""));
                    m++;
                }
                mixinCode = mixinCodeBuilder.toString();
                MixinNames.add(mixinName);
                Mixins.add(mixinCode);
                System.out.println(mixinName + " ------ " + mixinCode);
            }
        }

        for(int r = 0; r < MixinNames.size(); r++) {
            interpreter.put(MixinNames.get(r), Mixins.get(r));
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
                if(formattedKey.contains("(") && formattedKey.contains(")") && formattedKey.endsWith(":")) {
                    ionCodeArray[l] = "";
                    ionCodeArray[l + 1] = ionCodeArray[l + 1].substring(ionCodeArray[l + 1].indexOf(";") + 1, ionCodeArray[l + 1].length());
                }
                if(formattedKey.contains("(") && formattedKey.contains(")") && formattedKey.contains("$")) {
                    ionCodeArray[l] = "";
                    int m = 0;
                    while(!ionCodeArray[l + m].contains("}")) {
                        ionCodeArray[l + m] = "";
                        m++;
                    }
                    ionCodeArray[l + m] = "";
                }
                if(formattedKey.contains(";") && !formattedKey.contains(".")) {
                    ionCodeArray[l] = ionCodeArray[l].substring(0, ionCodeArray[l].length()).trim();
                    if(ionCodeArray[l].trim().equals(key + ";")) {
                        if(ionCodeArray[l].trim().contains("$")) {
                            ionCodeArray[l] = interpreter.get(key);
                        } else {
                            ionCodeArray[l] = interpreter.get(key) + ";\n";
                        }
                    }
                } else {
                    if(formattedKey.trim().equals(key)) {
                        for(int i = 0; i < replacements[0].length; i++) {
                            if(ionCodeArray[l + 1].contains(replacements[0][i])) {
                                return ionCodeArray[l] = interpreter.get(key);
                            }
                        }
                        ionCodeArray[l] = interpreter.get(key);
                    }
                }
            }
        }

        ionCode = String.join("", ionCodeArray).replaceAll("%20%", " ");

        return ionCode;
    }
}