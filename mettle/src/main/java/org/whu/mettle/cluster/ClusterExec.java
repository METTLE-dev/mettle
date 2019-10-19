package org.whu.mettle.cluster;

import java.lang.reflect.Method;

public class ClusterExec{

    private String target;
    private String clusterName;
    private int numCluster;
    private String output;

    public ClusterExec(String target, int numcluster, String clusterName, String output){
        this.target = target;
        this.numCluster = numcluster;
        this.clusterName = clusterName;
        this.output = output;
    }

    public void exec() throws Exception{
        Clusters c = new Clusters(target, numCluster);
        Method m = c.getClass().getMethod(clusterName.toLowerCase());
        String[] assignment = (String[])m.invoke(c);

        ResultHandler.writeLabel(target, output, assignment);
    }
}
