package core;

import java.util.ArrayList;
import java.util.List;

public class Mixins {
    private static List<String> MixinNames;
    private static List<String> Mixins;

    public static void IntegrateMixins(String[] srcArr) {

        List<String> _MixinNames = new ArrayList<>();
        List<String> _Mixins = new ArrayList<>();

        for(int i = 0; i < srcArr.length; i++) {
            if(srcArr[i].contains("(") && srcArr[i].contains(")") && srcArr[i].contains("$")) {
                String mixinName = srcArr[i].trim().replaceAll("[()\n]", "");
                String mixinCode;
                StringBuilder mixinCodeBuilder = new StringBuilder();
                int m = 2;

                while(!srcArr[i + m].trim().contains("}") && !srcArr[i + m].trim().equals("\n")) {
                    mixinCodeBuilder.append(srcArr[i + m].trim().replaceAll("\n", ""));
                    m++;
                }
                mixinCode = mixinCodeBuilder.toString();
                _MixinNames.add(mixinName);
                _Mixins.add(mixinCode);
            }
        }

        MixinNames = _MixinNames;
        Mixins =_Mixins;
    }

    public static List<String> getMixinNames() {
        return MixinNames;
    }

    public static List<String> getMixins() {
        return Mixins;
    }
}
