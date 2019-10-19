package org.whu.mettle.evaluate;

import org.whu.mettle.constant.Config;
import org.whu.mettle.util.RuntimeUtil;

public class CheckViolation {

    private String sourceFile;
    private String followFile;
    private int numCluster;
    private Config config;

    public CheckViolation(String sourceFile, String followFile, int numCluster, Config config){
        this.sourceFile = sourceFile;
        this.followFile = followFile;
        this.numCluster = numCluster;
        this.config = config;
    }

    public int exec(){
        String pyPath = config.getPyPath();
        String canonicalPath = System.getProperty("user.dir");
        String scriptPath = canonicalPath.replace("\\", "/") + "/lib/check.py";
        String[] args = new String[]{pyPath, scriptPath, sourceFile, followFile, String.valueOf(numCluster)};
        RuntimeUtil.runtimeExec(args);
        String re = RuntimeUtil.getRuntimeResult();
        if(Float.parseFloat(re) > 0){
            return 1;
        }else{
            return 0;
        }
    }
}
