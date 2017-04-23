/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.FacetInterface;
import com.hassoubeat.Result;
import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.constract.PropertyConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import com.hassoubeat.toytalk.gpio.GpioManager;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import com.hassoubeat.toytalk.quartz.QuartzManager;
import com.hassoubeat.toytalk.rest.RestClient;
import com.hassoubeat.toytalk.util.UtilLogic;
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
    private final GpioManager gpio = GpioManager.getInstance();
    private final Viewer viewer = ViewerFactory.getInstance();
    private final UtilLogic utilLogic = UtilLogic.getInstance();
    private final RestClient restClient = RestClient.getInstance();
    private final QuartzManager quartzManager = QuartzManager.getInstance();
    
    private final String PACAKAGE = PropertyConst.FACET_PACKAGE;
    private final String FACET_PROGRAM_PATH = PropertyConst.FACET_LIB_PATH;
    
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println("OriginalJobだよぉぉぉぉ！");
        
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String programFilePath = FACET_PROGRAM_PATH + jobDataMap.get("fileName");
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
        // ログ出力
        logger.info("{}.{} RUN_PROGRAM_FILE_PATH:{} RUN_CLASS:{}" , MessageConst.SUCCESS_RUN_ORIGINAL_JOB.getId(), MessageConst.SUCCESS_RUN_ORIGINAL_JOB.getMessage(), programFilePath, className);
        
    }
}
