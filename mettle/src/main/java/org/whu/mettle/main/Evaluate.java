package org.whu.mettle.main;

import org.whu.mettle.constant.Config;
import org.whu.mettle.constant.StringConstant;
import org.whu.mettle.evaluate.ScoreCal;
import org.whu.mettle.util.PropertiesUtil;

import java.util.Iterator;
import java.util.Map;

public class Evaluate {

    public static void main(String... args) throws Exception{

        final String CONFIG_FILE = "config.properties";
        PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE, false);
        Config config = new Config(prop);

        String evaluateFile = "src/main/java/org/whu/mettle/main/evaluate.csv";
        ScoreCal scoreCal = new ScoreCal();
        Map<Double, String> resultMap = scoreCal.getResult(evaluateFile);
        Iterator iterator = resultMap.values().iterator();
        System.out.println("Final Ranking:");
        while (iterator.hasNext()){
            String cluster = (String)iterator.next();
            System.out.print(cluster + StringConstant.COMPARE_SIGN);
        }


    }

}
