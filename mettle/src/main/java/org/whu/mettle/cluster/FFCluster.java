package org.whu.mettle.cluster;

import weka.clusterers.FarthestFirst;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class FFCluster {
	
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	private float[][] centroids = new float[numCluster][2];
	
	public FFCluster(String sourceFile,int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public String[] cluster() throws Exception{
		FarthestFirst FF=new FarthestFirst();
		
		//set options
		String[] options=new String[4];
		options[0]="-N";
		options[1]=Integer.toString(numCluster);
		options[2]="-S";
		options[3]="10";
		
		FF.setOptions(options);
		FF.buildClusterer(insdata);

		assignment=new String[insdata.numInstances()];
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(FF.clusterInstance(insdata.instance(j)));
		}

		return assignment;

	}

	public String[] process() throws Exception{
		loadData();
		return cluster();
	}



}
