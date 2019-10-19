package org.whu.mettle.cluster;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class HierarchicalCluster {
	
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	
	public HierarchicalCluster(String sourceFile,int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public String[] cluster() throws Exception{
		HierarchicalClusterer HC=new HierarchicalClusterer();
		
		//set options
		String[] options=new String[4];
		options[0]="-N";
		options[1]=Integer.toString(numCluster);
		options[2]="-L";
		options[3]="AVERAGE";
		
		HC.setOptions(options);
		HC.buildClusterer(insdata);

		assignment=new String[insdata.numInstances()];
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(HC.clusterInstance(insdata.instance(j)));
		}

		return assignment;
	}

	
	public String[] process() throws Exception{
		loadData();
		return cluster();
	}



}
