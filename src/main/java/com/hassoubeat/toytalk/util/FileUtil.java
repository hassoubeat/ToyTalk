/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;


import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.io.File;
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
public class FileUtil {

    private static final FileUtil instance = new FileUtil();
    
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class.getName());
    
    /**
     * 引数で受け取ったパスからファセットプログラムをダウンロードする
     * @param downloadPath ファイルのダウンロードパス
     * @param saveDirectory 保存先ディレクトリ (例: /usr/local/test/ 直下にファイルが保存される) 
     * @return 
     */
    public String download(String downloadPath, String saveDirectory) {
        String fileName = FilenameUtils.getName(downloadPath);
        String fileSavePath = saveDirectory + fileName;
        try {
            URL url = new URL(downloadPath);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(fileSavePath);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        } catch (MalformedURLException ex) {
            throw new ToyTalkException("", ex);
        } catch (IOException ex) {
            throw new ToyTalkException("", ex);
        }
        logger.info("{}.{} FILE_DOWNLOAD_PATH:{} SAVE_DIRECTORY:{}" , MessageConst.SUCCESS_FILE_DOWNLOAD.getId(), MessageConst.SUCCESS_FILE_DOWNLOAD.getMessage(), downloadPath, fileSavePath);
        return fileSavePath;
    }
    
    /**
     * 引数で受け取ったパスのファイルの読込権限を誰でも可にする
     * @param filePath 
     */
    public void chmodAnyReadable(String filePath) {
        File file = new File(filePath);
        file.setReadable(true, false);
    }
    
    /**
     * 引数で受け取ったパスのファイルの書き込み権限を誰でも可にする
     * @param filePath 
     */
    public void chmodAnyWritable(String filePath) {
        File file = new File(filePath);
        file.setWritable(true, false);
    }
    
    public static FileUtil getInstance() {
        return instance;
    }
}
