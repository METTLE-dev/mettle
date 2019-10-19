package org.whu.mettle.cluster;

import weka.clusterers.EM;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class EMCluster {
	
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	private float[][] centroids=new float[numCluster][2];
	
	public EMCluster(String sourceFile,int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public String[] cluster() throws Exception{
		EM em=new EM();
		
		//set options
		String[] options=new String[4];
		options[0]="-N";
		options[1]=Integer.toString(numCluster);
		options[2]="-S";
		options[3]="10";
		
		em.setOptions(options);
		em.buildClusterer(insdata);

		assignment=new String[insdata.numInstances()];
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(em.clusterInstance(insdata.instance(j)));
		}

		return assignment;

	}

	
	public String[] process() throws Exception{
		loadData();
		return cluster();
	}


}
