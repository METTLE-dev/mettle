package org.whu.mettle.mr;

import java.io.BufferedWriter;    
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import org.whu.mettle.cluster.EMCluster;
import org.whu.mettle.cluster.FFCluster;
import org.whu.mettle.cluster.KMCluster;
import org.whu.mettle.cluster.XMCluster;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GetCentroid {
	private Instances insdata;
	private String sourceFile;
	private int numCluster;
	private String[] assignment;
	private float[][] m;
	private float[][] mNew;

	public GetCentroid(String sourceFile, int numCluster){
		this.sourceFile = sourceFile;
		this.numCluster = numCluster;
	}


	public void writeNew(String followFile) throws Exception{
		FileWriter fw;
		BufferedWriter bw = null;
		try{
			fw = new FileWriter(followFile);
			bw = new BufferedWriter(fw);
			bw.write("A,B\n");
			for(int k=0;k<insdata.numInstances();k++){
				bw.write(mNew[k][0]+","+mNew[k][1]+"\n");
			}
		}finally {
			if(bw!=null){
				bw.close();}
		}
	}


	public void writeExtra(String followFile) throws Exception{
		FileWriter fw;
		BufferedWriter bw=null;
		try{
			fw = new FileWriter(followFile);
			bw = new BufferedWriter(fw);
			bw.write("A,B\n");
			for(int k=0;k<insdata.numInstances();k++){
				bw.write(m[k][0]+","+m[k][1]+"\n");
			}
			for(int k=0;k<insdata.numInstances();k++){
				bw.write(mNew[k][0]+","+mNew[k][1]+"\n");
			}
		}finally{
			if(bw!=null){
				bw.close();
			}
		}
	}

	
	public void loadData(){
		try {
		DataSource dataSource = new DataSource(sourceFile);
			insdata = dataSource.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getMidPoints(float[][] centroids){
		m=new float[insdata.numInstances()][2];
		mNew = new float[insdata.numInstances()][2];
		for(int i=0;i<2;i++){
			for(int k=0;k<insdata.numInstances();k++){
				m[k][i]=(float)insdata.instance(k).value(i);
				int l = Integer.valueOf(assignment[k]);
				mNew[k][i] = (m[k][i] + centroids[l][i]) / 2;
			}
		}
	}
	
	
	public float[][] getCentroidsKM(){
		KMCluster KM = new KMCluster(sourceFile, numCluster);
		try {
			assignment = KM.cluster();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//get centroids for each cluster
		float[][] centroids = new float[numCluster][2];
		for(int i=0;i<2;i++){
			for(int k=0;k<numCluster;k++){
				try{
				    centroids[k][i]=(float)KM.getClusterCentroids().instance(k).value(i);
				}catch(Exception e){
					e.printStackTrace();
					centroids[k][i] = 0;
				}
			}
		}
		return centroids;
	}
	
	public float[][] getCentroidsXM(){
		XMCluster XM = new XMCluster(sourceFile, numCluster);
		try {
			assignment = XM.cluster();
		} catch (Exception e) {
			e.printStackTrace();
		}
		float[][] centroids = new float[numCluster][2];
		for(int i=0;i<2;i++){
			for(int k=0;k<numCluster;k++){
				try{
				    centroids[k][i]=(float)XM.getClusterCenters().instance(k).value(i);
				}catch(Exception e){
					e.printStackTrace();
					centroids[k][i] = 0;
				}
		    }
		}
		return centroids;
	}
	
	public float[][] getCentroidsEM(){
		EMCluster em = new EMCluster(sourceFile, numCluster);
		try {
			assignment = em.cluster();
		} catch (Exception e) {
			e.printStackTrace();
		}
		float[][] centroids=new float[numCluster][2];
		for(int i=0;i<2;i++){
			for(int k=0;k<numCluster;k++){
		     centroids[k][i]=(float)em.getClusterModelsNumericAtts()[k][i][0];
		    }
		}
		return centroids;
	}
	
	
	public float[][] getCentroidsFF(){
		FFCluster FF = new FFCluster(sourceFile, numCluster);
		try {
			assignment = FF.cluster();
		} catch (Exception e) {
			e.printStackTrace();
		}
		float[][] centroids=new float[numCluster][2];
		m=new float[insdata.numInstances()][2];
		for(int i=0;i<2;i++){
			for(int k=0;k<numCluster;k++){
			    try{
			    	centroids[k][i]=(float)FF.getClusterCentroids().instance(k).value(i);
			    }catch(Exception e){
			    	e.printStackTrace();
			    	centroids[k][i]=0;
			    }
		        	     
		     }			
		}
		return centroids;
	}

	
}
