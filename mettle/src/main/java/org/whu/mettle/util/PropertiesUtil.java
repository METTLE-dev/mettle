package org.whu.mettle.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

    private final static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    private Properties p;
    private String fileName;

    public PropertiesUtil(String fileName, boolean outside){
        this.p = new Properties();
        this.fileName = fileName;

        InputStream inputStream = null;
        try{
            if(outside){
                inputStream = getInputStreamByFile(fileName);
            }else{
                inputStream = getInputStream(Thread.currentThread().getContextClassLoader(), fileName);
                if(inputStream == null){
                    inputStream = getInputStream(PropertiesUtil.class.getClassLoader(), fileName);
                }
            }
            p.load(inputStream);
        }catch(Exception e){
            throw new RuntimeException("can not find config file:" + fileName, e);
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException ioe){
                    log.error("error closing fileStream", ioe);
                }
            }
        }
    }

    /**
     * load inner properties
     */
    public static InputStream getInputStream(ClassLoader classLoader, String fileName){
        if(classLoader == null || StringUtils.isBlank(fileName)){
            log.info("classLoader is null or fileName is null");
            return null;
        }
        fileName = fileName.trim();
        InputStream stream = null;
        try{
            stream = classLoader.getResourceAsStream(fileName);
        }catch (Exception e){
            log.error("error read /" + fileName, e);
        }
        return stream;
    }

    /**
     * load outer properties
     */
    public static InputStream getInputStreamByFile(String path){
        File file = new File(path);
        if(!file.isFile() || !file.exists()){
            throw new IllegalArgumentException("File" + path + "does not exists");
        }

        InputStream stream = null;
        try{
            stream = new FileInputStream(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * get properties
     */
    public String getStringProperty(String propertyName){
        return p.getProperty(propertyName);
    }

    public String getStringProperty(String propertyName, String dft){
        String value = p.getProperty(propertyName);
        if(StringUtils.isBlank(value)){
            return dft;
        }
        return value;
    }

    public Integer getIntProperty(String propertyName, Integer dft){
        String value = p.getProperty(propertyName);
        return getInt(value, dft);
    }

    public Long getLongProperty(String propertyName, Long dft){
        String value = p.getProperty(propertyName);
        return getLong(value, dft);
    }

    public Double getDoubleProperty(String propertyName, Double dft){
        String value = p.getProperty(propertyName);
        return getDouble(value, dft);
    }

    public Boolean getBooleanProperty(String propertyName, Boolean dft){
        String value = p.getProperty(propertyName);
        return getBoolean(value, dft);
    }

    private Integer getInt(String str, Integer dft){
        try{
            return Integer.parseInt(str.trim());
        }catch (Exception e){
            log.error("error parsing" + str + "to Int, use default value: " + dft);
            return dft;
        }
    }

    private Long getLong(String str, Long dft){
        try{
            return Long.parseLong(str.trim());
        }catch (Exception e){
            log.error("error parsing" + str + "to Long, use default value: " + dft);
            return dft;
        }
    }

    public Double getDouble(String str, Double dft){
        try{
            return Double.parseDouble(str.trim());
        }catch(Exception e){
            log.error("error parsing" + str + "to double, use default value: " + dft);
            return dft;
        }
    }

    private Boolean getBoolean(String str, Boolean dft){
        try{
            return Boolean.parseBoolean(str.trim());
        }catch (Exception e){
            log.error("error parsing" + str + "to bool, use default value: " + dft);
            return dft;
        }
    }


    public Properties getProps(){
        return p;
    }

    public String getFileName(){
        return fileName;
    }

    public void setProperty(String propertyName, String propertyValue){
        p.setProperty(propertyName, propertyValue);
    }


}
