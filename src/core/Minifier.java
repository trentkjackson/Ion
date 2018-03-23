package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Minifier {
    static String[] minified(String src, String[][] replacements) {
        List<String> dontMinifyKeywords = new ArrayList<>(
                // For some reason the keyword "margin:" breaks this.
                Arrays.asList("box-shadow:", "font:", "border:", "transform:", "flex-flow:", "grid:")
        );
        String encode_wanted_whitespace_array[] = src.trim().replaceAll("\n", " ").split(" ");
        mainLoop:
        for(int i = 0; i < encode_wanted_whitespace_array.length; i++) {
            for(int x = 0; x < dontMinifyKeywords.size(); x++) {
                if(encode_wanted_whitespace_array[i].contains(dontMinifyKeywords.get(x))) {
                    int u = 2;
                    while(!encode_wanted_whitespace_array[i + u].contains(";")) {
                        if(!encode_wanted_whitespace_array[i + u].contains("!important")) {
                            encode_wanted_whitespace_array[i + u] = "%20%" + encode_wanted_whitespace_array[i + u];
                        }
                        u++;
                    }
                    for(int q = 0; q < replacements[0].length; q++) {
                        if(encode_wanted_whitespace_array[i + u].contains(replacements[0][q])) {
                            break mainLoop;
                        }
                    }
                    encode_wanted_whitespace_array[i + u] = "%20%" + encode_wanted_whitespace_array[i + u];
                }
            }
        }
        return encode_wanted_whitespace_array;
    }
}
