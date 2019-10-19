package org.whu.mettle.cluster;

import weka.clusterers.XMeans;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class XMCluster {
	
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	private float[][] centroids = new float[numCluster][2];
	
	public XMCluster(String sourceFile, int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public String[] cluster() throws Exception{
		XMeans XM=new XMeans();
		
		//set options
		String[] options=new String[6];
		options[0]="-L";
		options[1]=Integer.toString(numCluster);
		options[2]="-H";
		options[3]=Integer.toString(numCluster);
		options[4]="-S";
		options[5]="10";
		
		XM.setOptions(options);
		XM.buildClusterer(insdata);

		assignment=new String[insdata.numInstances()];
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(XM.clusterInstance(insdata.instance(j)));
		}

		return assignment;
		
	}

	
	public String[] process() throws Exception{
		loadData();
		return cluster();
	}



}
