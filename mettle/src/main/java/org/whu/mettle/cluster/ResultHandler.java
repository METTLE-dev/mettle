package org.whu.mettle.cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ResultHandler {

    public static void writeLabel(String sourceFile, String targetFile, String[] labels) throws Exception{
        FileReader fr;
        BufferedReader br=null;
        FileWriter fw;
        BufferedWriter bw=null;

        String line;
        int j=0;

        try{
            fr=new FileReader(sourceFile);
            br=new BufferedReader(fr);
            fw=new FileWriter(targetFile);
            bw=new BufferedWriter(fw);

            line=br.readLine();
            bw.write(line+",cluster\n");
            while((line=br.readLine())!=null){
                bw.write(line+","+labels[j++]+"\n");
            }
        }finally{
            if (br!=null){
                br.close();}
            if(bw!=null){
                bw.close();}
        }
    }
}
