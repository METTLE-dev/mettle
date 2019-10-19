package org.whu.mettle.evaluate;

import org.apache.commons.lang3.StringUtils;
import org.whu.mettle.constant.StringConstant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ScoreCal {

    public Map<Double, String> getResult(String path) throws Exception {
        String clusterName;
        List<Double> mrViolation = new ArrayList<>();
        List<Double> mrPreference = new ArrayList<>();
        List<Double> mrSeverity = new ArrayList<>();
        Map<Double, String> resultMap = new TreeMap<>();

        FileReader fr;
        BufferedReader br=null;
        try{
            fr=new FileReader(path);
            br=new BufferedReader(fr);
            br.readLine();
            String line;
            while((line=br.readLine()) != null){
                String[] eles = StringUtils.split(line, StringConstant.COMMA);
                clusterName = eles[0];
                mrViolation = format(eles[1], mrViolation);
                mrPreference = format(eles[2], mrPreference);
                mrSeverity = format(eles[3], mrSeverity);
                double r = calculate(mrViolation, mrPreference, mrSeverity);

                resultMap.put(r, clusterName);

            }
        }finally {
            if(br!=null){
                br.close();
            }
        }

        return resultMap;

    }


    public double calculate(List l1, List l2, List l3){
        double sum = 0;
        for(int i=0; i<l1.size(); i++){
            sum += (double)l1.get(i) * (double)l2.get(i) * (double)l3.get(i);
        }

        return sum;
    }

    public List<Double> format(String s, List<Double> l){
        String s1 = StringUtils.removeStart(s, StringConstant.LIST_LEFT);
        String s2 = StringUtils.removeEnd(s1, StringConstant.LIST_RIGHT);
        String[] strs = s2.split(StringConstant.SPACE);
        for(int i=0; i<strs.length; i++){
            l.add(Double.valueOf(strs[i]));
        }
        return l;
    }
}
