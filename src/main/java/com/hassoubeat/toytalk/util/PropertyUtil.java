/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;


import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.nio.file.NoSuchFileException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * プロパティファイルの読み書きを行うメソッド
 * @author hassoubeat
 */
public class PropertyUtil {

    private static final PropertyUtil instance = new PropertyUtil();
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class.getName());
    
    /**
     * 指定したプロパティファイルから指定したkeyのパラメーターを取得する
     * @param filePath
     * @param key
     * @return 
     */
    public String load(String filePath, String key) {
        Configurations configs = new Configurations();
        Configuration config;
        try {
            config = configs.properties(filePath);
        } catch (ConfigurationException ex) {
            logger.info("{}.{}", MessageConst.FAILED_GET_PROPERTY_FILE.getId(), MessageConst.FAILED_GET_PROPERTY_FILE.getMessage(), ex);
//            NoSuchFileException noSuchFileException = new NoSuchFileException(MessageConst.FAILED_GET_PROPERTY_FILE.getMessage());
//            noSuchFileException.initCause(ex);
//            throw noSuchFileException;
            throw new ToyTalkException(MessageConst.FAILED_GET_PROPERTY_FILE.getMessage(), ex);
        }
        String param = config.getString(key);
        return param;
    }
    
    /**
     * 指定したプロパティファイルに指定したkeyでパラメーターを保存する
     * @param filePath
     * @param key
     * @param param
     */
    public void save(String filePath, String key, String param) {
        Configurations configs = new Configurations();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = 
            configs.propertiesBuilder(filePath);
        PropertiesConfiguration config;
        try {
            config = builder.getConfiguration();
            config.setProperty(key, param);
        } catch (ConfigurationException ex) {
            logger.info("{}.{}", MessageConst.FAILED_GET_PROPERTY_FILE.getId(), MessageConst.FAILED_GET_PROPERTY_FILE.getMessage(), ex);
//            NoSuchFileException noSuchFileException = new NoSuchFileException(MessageConst.FAILED_GET_PROPERTY_FILE.getMessage());
//            noSuchFileException.initCause(ex);
//            throw noSuchFileException;
            throw new ToyTalkException(MessageConst.FAILED_GET_PROPERTY_FILE.getMessage(), ex);
        }
        try {
            builder.save();
        } catch (ConfigurationException ex) {
            logger.info("{}.{}", MessageConst.FAILED_SAVE_PROPERTY_FILE.getId(), MessageConst.FAILED_SAVE_PROPERTY_FILE.getMessage());
            throw new ToyTalkException(MessageConst.FAILED_SAVE_PROPERTY_FILE.getMessage(), ex);
        }
    }
    
    /**
     * PropertyUtilのシングルトンインスタンスを取得する
     * @return 
     */
    public static PropertyUtil getInstace() {
        return instance;
    }
    
    
    
}
