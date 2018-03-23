package core;

import java.util.*;

public class Variables {
    private static Map<String, String> replacementsMap = new Hashtable<>();
    private static String ionSource;
    private static String[] ionSourceArray;
    private static List<String> INTERNAL_VARIABLE;
    private static List<String> INTERNAL_REPLACEMENT;

    public static void IntegrateVariables(String src, String[][] replacements) {
        Map<String, String> _replacementsMap = new Hashtable<>();
        for(int i = 0; i < replacements[0].length; i++) {
            _replacementsMap.put(replacements[0][i], replacements[1][i]);
        }
        String[] ionCodeArray = src.split("");
        for(int x = 0; x < ionCodeArray.length; x++) {
            for(String key : _replacementsMap.keySet()) {
                if(ionCodeArray[x].equals(key)) {
                    ionCodeArray[x] = _replacementsMap.get(key);
                }
            }
        }
        src = String.join("", ionCodeArray);

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

        List<String> _INTERNAL_VARIABLE = new ArrayList<>(Arrays.asList(INTERNAL[0]));
        List<String> _INTERNAL_REPLACEMENT = new ArrayList<>(Arrays.asList(INTERNAL[1]));

        ionCodeArray = src.split(" ");

        for(int j = 0; j < ionCodeArray.length; j++) {
            if(ionCodeArray[j].trim().contains("(") && ionCodeArray[j].contains(")") && ionCodeArray[j].trim().contains(":")) {
                String variableName = ionCodeArray[j].substring(0, ionCodeArray[j].indexOf(":")).replaceAll(":", "").trim();
                String variable = ionCodeArray[j + 1].substring(0, ionCodeArray[j + 1].indexOf(";")).replaceAll(";", "").trim();
                _INTERNAL_VARIABLE.add(variableName.trim());
                _INTERNAL_REPLACEMENT.add(variable.trim());
            }
        }

        replacementsMap = _replacementsMap;
        ionSource = src;
        ionSourceArray = ionCodeArray;
        INTERNAL_VARIABLE = _INTERNAL_VARIABLE;
        INTERNAL_REPLACEMENT = _INTERNAL_REPLACEMENT;
    }

    public static Map<String, String> getReplacementsMap() {
        return replacementsMap;
    }

    public static String getIonSource() {
        return ionSource;
    }

    public static String[] getIonSourceArray() {
        return ionSourceArray;
    }

    public static List<String> getInternalVariable() {
        return INTERNAL_VARIABLE;
    }

    public static List<String> getInternalReplacement() {
        return INTERNAL_REPLACEMENT;
    }
}
