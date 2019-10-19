package org.whu.mettle.mr;

import org.whu.mettle.cluster.Clusters;
import org.whu.mettle.cluster.ResultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetInformative {

    private String sourceFile;
    private String followFile;
    private int numCluster;
    private String clusterName;

    public GetInformative(String sourceFile, String followFile, int numCluster, String clusterName){
        this.sourceFile = sourceFile;
        this.followFile = followFile;
        this.numCluster = numCluster;
        this.clusterName = clusterName;
    }

    public void writeInformative(){
        String[] assignment = null;
        Clusters clusters = new Clusters(sourceFile, numCluster);
        Method m;
        try {
            m = clusters.getClass().getMethod(clusterName.toLowerCase());
            assignment = (String[])m.invoke(clusters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            ResultHandler.writeLabel(sourceFile, followFile, assignment);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
