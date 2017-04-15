/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファセットバージョンを管理するプロパティファイルであれこれするクラス
 * @author hassoubeat
 */
public class FacetVersionPropertyUtil {

    private static final FacetVersionPropertyUtil instance = new FacetVersionPropertyUtil();
    
    private static final Logger logger = LoggerFactory.getLogger(FacetVersionPropertyUtil.class.getName());
    
    private final PropertyUtil propertyUtil = PropertyUtil.getInstace();
    
    private final String PROPERTY_FILE_NAME = "facetVersion.properties";
    
    /**
     * 引数で渡したファセットバージョンとプロパティファイルに保持しているファセットバージョンを比較する
     * @param propertyKey
     * @param targetFacetVersion
     * @return 比較結果 引数のファセットバージョンがパラメータが存在しなかった or 大の場合 1, =の場合 0, 小の場合-1
     */
    public int compareToFacetVersion(String propertyKey, Double targetFacetVersion){
        String saveFacetVersionStr = propertyUtil.load(PROPERTY_FILE_NAME, propertyKey);
        if (StringUtils.isEmpty(saveFacetVersionStr)) {
            // keyに対応するパラメータが存在しなかった場合
            return 1;
        }
        Double saveFacetVersion = Double.parseDouble(saveFacetVersionStr);
        
        return targetFacetVersion.compareTo(saveFacetVersion);
    }
    
    /**
     * 引数で渡したキーとパラメーターでプロパティファイルに上書き保存する
     * @param propertyKey
     * @param saveFacetVersion
     */
    public void saveFacetVersion(String propertyKey, Double saveFacetVersion){
        propertyUtil.save(PROPERTY_FILE_NAME, propertyKey, saveFacetVersion.toString());
    }
    
    /**
     * FacetVersionPropertyUtilのシングルトンインスタンスを返却する
     * @return 
     */
    public static FacetVersionPropertyUtil getInstance() {
        return instance;
    }
}
