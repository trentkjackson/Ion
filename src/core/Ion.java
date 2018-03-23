package core;
import java.util.*;

public class Ion {
    private FileUtilManager fileManager;
    public Ion(FileUtilManager manager) {
        this.fileManager = manager;
    }

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
        // Minify properly
        String[] encode_wanted_whitespace_array = Minifier.minified(ionCode, replacements);
        ionCode = String.join(" ", encode_wanted_whitespace_array).trim().replaceAll("\n", " ");

        // Add variables
        Variables.IntegrateVariables(ionCode, replacements);
        String[] ionCodeArray = Variables.getIonSourceArray();
        List<String> INTERNAL_VARIABLE = Variables.getInternalVariable();
        List<String> INTERNAL_REPLACEMENT = Variables.getInternalReplacement();

        // Add mixins
        Mixins.IntegrateMixins(ionCodeArray);
        List<String> MixinNames = core.Mixins.getMixinNames();
        List<String> Mixins = core.Mixins.getMixins();

        Map<String, String> interpreter = new Hashtable<>();

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