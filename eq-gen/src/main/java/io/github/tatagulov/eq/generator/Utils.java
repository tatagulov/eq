package io.github.tatagulov.eq.generator;


import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toFirstUpperCase(part);
        }
        return camelCaseString;
    }

    public static String toCamelCaseFirstLower(String s){
        String camelCaseString = toCamelCase(s);
        return Keywords.getCheckedName(camelCaseString.substring(0,1).toLowerCase() + camelCaseString.substring(1));
    }

    private static String toFirstUpperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    static String replacePackageToPath(String packageName) {
        String separator = Matcher.quoteReplacement(File.separator);
        String searchString = Pattern.quote(".");
        return packageName.replaceAll(searchString,separator);
    }

    public static void checkDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) throw new RuntimeException(String.format("directory %s not create",path));
        }
    }
}
