package org.whu.mettle.cluster;


public class Clusters {
	private String sourceFile;
	private int numCluster;
	
	public Clusters(String sourceFile, int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;
	}
	
	public String[] km() throws Exception{
		KMCluster kmCluster = new KMCluster(sourceFile, numCluster);
	    return kmCluster.process();
	}
	
	public String[] xm() throws Exception{
		XMCluster xmCluster = new XMCluster(sourceFile, numCluster);
		return  xmCluster.process();
	}
	
	public String[] em() throws Exception{
		EMCluster emCluster = new EMCluster(sourceFile, numCluster);
		return emCluster.process();
	}
	
	public String[] ff() throws Exception{
		FFCluster ffCluster = new FFCluster(sourceFile, numCluster);
		return ffCluster.process();
	}
	
	public String[] an() throws Exception{
		HierarchicalCluster agCluster = new HierarchicalCluster(sourceFile, numCluster);
		return agCluster.process();
	}
	
	public String[] ds() throws Exception{
		DBScanCluster dbCluster = new DBScanCluster(sourceFile);
		return dbCluster.process();
	}
	

}
