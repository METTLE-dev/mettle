package org.whu.mettle.constant;

import org.whu.mettle.util.PropertiesUtil;

public class Config {

    private PropertiesUtil props;
    private String testData;
    private String script;
    private String pyPath;
    private String output;

    public Config(PropertiesUtil props){
        this.props = props;
        this.testData = props.getStringProperty("testData");
        this.script = props.getStringProperty("script");
        this.pyPath = props.getStringProperty("pyPath");
        this.output = props.getStringProperty("output");
    }

    public String getTestData(){
        return testData;
    }

    public String getScript(){
        return script;
    }

    public String getPyPath(){
        return pyPath;
    }

    public String getOutput(){
        return output;
    }
}
