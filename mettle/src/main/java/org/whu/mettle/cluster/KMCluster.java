package org.whu.mettle.cluster;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class KMCluster {
	
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	private float[][] centroids = new float[numCluster][2];

	
	public KMCluster(String sourceFile,int numCluster){
		this.sourceFile=sourceFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public String[] cluster() throws Exception {
		SimpleKMeans KM = new SimpleKMeans();

		//set options
		String[] options = new String[4];
		options[0] = "-N";
		options[1] = Integer.toString(numCluster);
		options[2] = "-S";
		options[3] = "10";

		KM.setOptions(options);
		KM.buildClusterer(insdata);

		assignment = new String[insdata.numInstances()];
		for (int j = 0; j < insdata.numInstances(); j++) {
			assignment[j] = String.valueOf(KM.clusterInstance(insdata.instance(j)));
		}

		return assignment;

	}

	public String[] process() throws Exception{
		loadData();
		return cluster();
	}


}
