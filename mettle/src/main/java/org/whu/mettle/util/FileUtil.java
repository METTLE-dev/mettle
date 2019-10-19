package org.whu.mettle.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;


public class FileUtil {

    public static void ifPath(String targetPath){
        File filePath = new File(targetPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
    }

    public static String getAbsolutePath(String packName, String fileName) throws IOException {
        File root = new File(".");
        return root.getCanonicalPath() + packName.replaceAll("\\.",
                Matcher.quoteReplacement("/")) + fileName;
    }

    public static String getAbsolutePath(String packName) throws IOException{
        File root = new File(".");
        return root.getCanonicalPath() + packName.replaceAll("\\.",
                Matcher.quoteReplacement("/"));
    }

}
