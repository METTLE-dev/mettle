package org.whu.mettle.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RuntimeUtil {

    private static String line;

    public static void runtimeExec(String... args){
        Runtime r = Runtime.getRuntime();
        Process proc;
        try {
            proc = r.exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            line =in.readLine();
            in.close();
            proc.waitFor();
            proc.destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getRuntimeResult(){
        return line;
    }

}