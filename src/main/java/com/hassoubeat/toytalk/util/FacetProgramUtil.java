/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;


import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファセットプログラムを編集するクラス
 * @author hassoubeat
 */
public class FacetProgramUtil {

    private static final FacetProgramUtil instance = new FacetProgramUtil();
    
    private static final Logger logger = LoggerFactory.getLogger(FacetProgramUtil.class.getName());
    
    // TODO 外部プロパティから読込
    private final String PROGRAM_PLACE_PATH = "/home/pi/share/toytalk/facet/lib/";
    
    /**
     * 引数で受け取ったパスからファセットプログラムをダウンロードする
     * @param facetProgramPath
     * @return 
     */
    public String download(String facetProgramPath) {
        String fileName = FilenameUtils.getName(facetProgramPath);
        String fileDownloadPath = PROGRAM_PLACE_PATH + fileName;
        try {
            URL url = new URL(facetProgramPath);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(fileDownloadPath);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        } catch (MalformedURLException ex) {
            throw new ToyTalkException("", ex);
        } catch (IOException ex) {
            throw new ToyTalkException("", ex);
        }
        logger.info("{}.{} FILE_DOWNLOAD_PATH:{}" , MessageConst.SUCCESS_FACET_PROGRAM_DOWNLOAD.getId(), MessageConst.SUCCESS_FACET_PROGRAM_DOWNLOAD.getMessage(), fileDownloadPath);
        return fileDownloadPath;
    }
    
    public static FacetProgramUtil getInstance() {
        return instance;
    }
}
