package org.whu.mettle.mr;

import org.whu.mettle.constant.Config;
import org.whu.mettle.util.RuntimeUtil;

import java.lang.reflect.Method;

public class MRImpl {

    private String sourceFile;
    private String followFile;
    private int numCluster;
    private String clusterName;
    private Config config;

    public MRImpl(String sourceFile, String followFile, int numCluster, String clusterName, Config config){
        this.sourceFile = sourceFile;
        this.followFile = followFile;
        this.numCluster = numCluster;
        this.clusterName = clusterName;
        this.config = config;
    }

    public void MR1_1(){
        exec("MR1_1");

    }


    public void MR1_2(){
        exec("MR1_2");
    }


    public void MR2_1() throws Exception{
        GetCentroid c = new GetCentroid(sourceFile, numCluster);
        c.loadData();
        Method m = c.getClass().getMethod("getCentroids" + clusterName.toUpperCase());
        c.getMidPoints((float[][])m.invoke(c));
        c.writeNew(followFile);

    }

    public void MR3_1() throws Exception {
        GetCentroid c = new GetCentroid(sourceFile, numCluster);
        c.loadData();
        Method m = c.getClass().getMethod("getCentroids" + clusterName.toUpperCase());
        c.getMidPoints((float[][])m.invoke(c));
        c.writeExtra(followFile);
    }


    public void MR3_2(){
        exec("MR3_2");
    }

    public void MR4_1(){
        GetInformative g = new GetInformative(sourceFile, followFile, numCluster, clusterName);
        g.writeInformative();
    }

    public void MR4_2(){
        exec("MR4_2");
    }

    public void MR5_1(){
        exec("MR5_1");
    }

    public void MR5_2(){
        exec("MR5_2");
    }

    public void MR_6(){
        exec("MR_6");
    }


    public void exec(String mrName){
        String pyPath = config.getPyPath();
        String canonicalPath = System.getProperty("user.dir");
        String scriptPath = canonicalPath.replace("\\", "/") + config.getScript();
        String[] args = new String[]{pyPath, scriptPath, sourceFile, followFile, mrName};
        RuntimeUtil.runtimeExec(args);
    }


}
