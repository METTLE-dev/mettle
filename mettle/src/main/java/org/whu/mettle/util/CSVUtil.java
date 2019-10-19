package org.whu.mettle.util;

import org.whu.mettle.constant.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class CSVUtil implements Iterator<Object[]> {

    private BufferedReader bufferedReader;
    private ArrayList<String> arrayList = new ArrayList<>();
    private int rowNum = 0;
    private int colNum = 0;
    private int currRowNum = 0;
    private String colName[];


    public CSVUtil(String fileName) throws IOException{

        String pack = ".src.main.java.org.whu.mettle.main.";
        String absolutePath = FileUtil.getAbsolutePath(pack, fileName);
        File csvFile = new File(absolutePath);
        bufferedReader = new BufferedReader(new FileReader(csvFile));
        while(bufferedReader.ready()){
            arrayList.add(bufferedReader.readLine());
            this.rowNum++;
        }

        String[] header = arrayList.get(0).split(",");
        this.colNum = header.length;
        colName = new String[colNum];
        for(int i=0; i<colNum; i++){
            colName[i] = header[i];
        }

        this.currRowNum++;
    }

    @Override
    public boolean hasNext(){
        if(rowNum == 0 || currRowNum >= rowNum){
            try{
                bufferedReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Object[] next(){
        Map<String, String> map = new TreeMap<>();
        String csvCell[] = arrayList.get(currRowNum).split(",");
        for(int i=0; i<this.colNum; i++){
            map.put(colName[i], csvCell[i]);
        }

        Object[] obj = new Object[1];
        obj[0] = map;

        this.currRowNum++;

        return obj;
    }

    @Override
    public void remove(){
        throw new UnsupportedOperationException("remove unsupported");
    }

}
