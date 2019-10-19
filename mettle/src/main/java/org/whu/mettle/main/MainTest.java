package org.whu.mettle.main;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import org.whu.mettle.evaluate.CheckViolation;
import org.whu.mettle.cluster.ClusterExec;
import org.whu.mettle.constant.StringConstant;
import org.whu.mettle.mr.MREntity;
import org.whu.mettle.mr.MRImpl;
import org.whu.mettle.util.CSVUtil;
import org.whu.mettle.util.FileUtil;
import org.whu.mettle.util.PropertiesUtil;
import org.whu.mettle.constant.Config;


public class MainTest {

    private Map<String, MREntity> resultMap = new HashMap<>();
    final String CONFIG_FILE = "config.properties";
    PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE, false);
    Config config = new Config(prop);

    @DataProvider(name = "TestData")
    public static Iterator<Object[]> getTestData() throws IOException {
        return new CSVUtil("MainTest.csv");

    }

    @Test(dataProvider = "TestData")
    public void mettleTest(Map<String, String> testData) throws Exception {

        List<String> mrs = Arrays.asList("MR1_1", "MR4_2", "MR5_1");
        List<Double> mrPreference = Arrays.asList(0.2, 0.2, 0.6);

        List<Integer> mrViolation = new ArrayList<>();
        String clusterName = testData.get("ClusterName");
        String datasetName = testData.get("DatasetName");
        int numCluster = Integer.valueOf(testData.get("NumCluster"));

        String sourceInput = FileUtil.getAbsolutePath(config.getTestData(), datasetName);
        String sourceOutput = FileUtil.getAbsolutePath(config.getOutput(), datasetName);
        FileUtil.ifPath(FileUtil.getAbsolutePath(config.getOutput()));
        String sourceFile = sourceInput + StringConstant.POSTFIX;
        String sourceOutputFile = sourceOutput + StringConstant.POSTFIX;

        // source clustering
        ClusterExec c1 = new ClusterExec(sourceFile, numCluster, clusterName, sourceOutputFile);
        c1.exec();

        for (String mr : mrs) {
            String followFile = sourceInput + StringConstant.CONNEC_SIGN + mr + StringConstant.POSTFIX;
            String followOutputFile = sourceOutput + StringConstant.CONNEC_SIGN + mr + StringConstant.POSTFIX;

            // follow-up data generation
            MRImpl mrImpl = new MRImpl(sourceFile, followFile, numCluster, clusterName, config);
            Method m = mrImpl.getClass().getMethod(mr);
            m.invoke(mrImpl);
            // follow-up clustering
            ClusterExec c2 = new ClusterExec(followFile, numCluster, clusterName, followOutputFile);
            c2.exec();

            //violation checking
            CheckViolation check = new CheckViolation(sourceOutputFile, followOutputFile, numCluster, config);
            mrViolation.add(check.exec());
        }

        MREntity mrEntity = new MREntity();
        mrEntity.setMrViolation(mrViolation);
        mrEntity.setMrPreference(mrPreference);

        resultMap.put(clusterName, mrEntity);

    }

    @AfterTest
    public void mettleEval() throws Exception {

        String clusterName;
        MREntity mrEntity;
        List<Integer> mrViolation;
        List<Double> mrPreference;

        FileWriter fw;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("src/main/java/org/whu/mettle/main/evaluate.csv");
            bw = new BufferedWriter(fw);
            String header = "clusterName" + StringConstant.COMMA + "mrViolation" + StringConstant.COMMA
                    + "mrPreference" + StringConstant.COMMA + "mrSeverity\n";
            bw.write(header);
            for (Map.Entry<String, MREntity> entry : resultMap.entrySet()) {
                clusterName = entry.getKey();
                mrEntity = entry.getValue();
                mrViolation = mrEntity.getMrViolation();
                mrPreference = mrEntity.getMrPreference();
                String line = clusterName + StringConstant.COMMA + StringUtils.replace(mrViolation.toString(),",", " ")
                        + StringConstant.COMMA + StringUtils.replace(mrPreference.toString(),","," ") + "\n";
                bw.write(line);
            }

        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

}
