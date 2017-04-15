/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.FacetInterface;
import com.hassoubeat.Result;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class OriginalJob implements Job{
    
    // ロガー
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    
    // TODO 外部プロパティに切り出し
    private final String PROGRAM_PLACE_PATH = "/home/pi/share/toytalk/facet/lib/";
    private final String PACAKAGE = "com.hassoubeat.";
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println("OriginalJobだよぉぉぉぉ！");
        
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String programFilePath = PROGRAM_PLACE_PATH + jobDataMap.get("fileName");
        String className = PACAKAGE + jobDataMap.get("className");
        
        //jarファイルを引数に指定
        File file = new File(programFilePath);
        URLClassLoader load;
        try {
            load = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            // クラスをロード
            Class cl = load.loadClass(className);

            FacetInterface instance = (FacetInterface)cl.newInstance();
            Result result = instance.execute();
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            // TODO なんとかするのだ
            throw new ToyTalkException("", ex);
        } catch (Exception ex) {
            throw new ToyTalkException("", ex);
        }
        // TODO ログ出力
        
    }
}
