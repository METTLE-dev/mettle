package org.whu.mettle.cluster;

import weka.clusterers.*;
import weka.core.*;  
import weka.core.converters.ConverterUtils.DataSource;


public class DBScanCluster {
	
	private Instances insdata;
	private String sourceFile;
	private String[] assignment;
	
	public DBScanCluster(String sourceFile){
		this.sourceFile=sourceFile;
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	

	public String[] cluster() throws Exception{
		DBScan db=new DBScan();
		
		//set options
		String[] options=new String[4];
		options[0]="-E";
		options[1]="0.1";
		options[2]="-M";
		options[3]="8";
		
		
		db.setOptions(options);
		db.buildClusterer(insdata);
		
		assignment=new String[insdata.numInstances()];
		
		ClusterEvaluation eval=new ClusterEvaluation();
		eval.setClusterer(db);
		eval.evaluateClusterer(insdata);
		double[] num=eval.getClusterAssignments();

		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(num[j]);
		}

		return assignment;

	}

	
	public String[] process() throws Exception{
		loadData();
		return cluster();
	}
	
	
}
