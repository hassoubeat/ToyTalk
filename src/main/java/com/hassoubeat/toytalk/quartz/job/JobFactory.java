/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.constract.PropertyConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.util.FileUtil;
import com.hassoubeat.toytalk.util.FacetVersionPropertyUtil;
import java.util.Properties;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 動的にQuartzJobを生成するクラス
 * @author hassoubeat
 */
public class JobFactory {
    // ロガー
    private static Logger logger = LoggerFactory.getLogger(JobFactory.class);
    
    private static FacetVersionPropertyUtil fvpUtil = FacetVersionPropertyUtil.getInstance();
    private static FileUtil fileUtil = FileUtil.getInstance();
    
    private static final String JOB = "job";
    
    // ジョブが属するグループ名を定義する
    public static final String TOY_GROUP = "job_toy_group";
    public static final String ACCOUNT_GROUP = "job_account_group";
    public static final String FACET_GROUP = "job_facet_group";
    
    /**
     * イベントの内容によって適したJobを発行する
     * @param restEvent 
     * @return 動的に生成したJob
     */
    static public JobDetail getJob(RestEvent restEvent) {
        JobDetail job = null;
        
        int eventId = restEvent.getId();
        
        // ジョブ名の生成
        String jobName = JOB + Integer.toString(restEvent.getId());
        
        // ジョブが属するグループ名の取得
        String jobGroupName = "";
        if (restEvent.getToyId() != null) {
            // Toyグループに紐づくイベント
            jobGroupName = TOY_GROUP;
            job = newJob(JsayJob.class).withIdentity(jobName, jobGroupName).build();
            job.getJobDataMap().put("eventName", restEvent.getName());
            job.getJobDataMap().put("eventContent", restEvent.getContent());
        }
        if (restEvent.getAccountId() != null) {
            // アカウントに紐づくイベント
            jobGroupName = ACCOUNT_GROUP;
            job = newJob(JsayJob.class).withIdentity(jobName, jobGroupName).build();
            job.getJobDataMap().put("eventName", restEvent.getName());
            job.getJobDataMap().put("eventContent", restEvent.getContent());
        }
        if (restEvent.getToyFacetId() != null) {
            // ファセットに紐づくイベント
            jobGroupName = FACET_GROUP;
            if (StringUtils.isEmpty(restEvent.getFacetProgramPath())) {
                // ファセットプログラムが指定されていなかった場合
                job = newJob(JsayJob.class).withIdentity(jobName, jobGroupName).build();
                job.getJobDataMap().put("eventName", restEvent.getName());
                job.getJobDataMap().put("eventContent", restEvent.getContent());
                
            } else {
                // ファセットプログラムが指定されていた場合
                String facetVersionKey = FilenameUtils.getBaseName(restEvent.getFacetProgramPath());
                Double facetVersion = restEvent.getFacetVersion();
                if (fvpUtil.compareToFacetVersion(facetVersionKey, facetVersion) > 0) {
                    // ToyTalkで保存しているToyバージョンよりも新しい場合
                    
                    // ファセットプログラムをダウンロードする
                    fileUtil.download(restEvent.getFacetProgramPath(), PropertyConst.FACET_LIB_PATH);
                    // ファセットプログラム編集画面をダウンロードする
                    String downloadEditViewPath = fileUtil.download(restEvent.getFacetPropertiesEditViewPath(), PropertyConst.FACET_PROPERTIES_EDIT_VIEW_PATH);
                    fileUtil.chmodAnyReadable(downloadEditViewPath);
                    
                    if (fvpUtil.compareToFacetVersion(facetVersionKey, facetVersion) == 2) {
                        // 新たに追加するファセットプログラムだった場合

                        // ファセットプロパティファイルを取得する
                        String downloadFilePath = fileUtil.download(restEvent.getFacetPropertiesPath(), PropertyConst.FACET_PROPERTIES_PATH);
                        fileUtil.chmodAnyReadable(downloadFilePath);
                        fileUtil.chmodAnyWritable(downloadFilePath);
                    }
                    // 新たにプロパティファイルにバージョンを上書きする
                    fvpUtil.saveFacetVersion(facetVersionKey, facetVersion);
                }
                
                job = newJob(OriginalJob.class).withIdentity(jobName, jobGroupName).build();
                job.getJobDataMap().put("fileName", FilenameUtils.getName(restEvent.getFacetProgramPath()));
                job.getJobDataMap().put("className", facetVersionKey);
            }
        }
        
        logger.info("{}.{} JOB_NAME:{} JOB_GROUP:{}" , MessageConst.SUCCESS_CREATE_JOB.getId(), MessageConst.SUCCESS_CREATE_JOB.getMessage(), jobName, jobGroupName);
        return job;
    }
    
    static public JobDetail getEventFetchJob() {
        return newJob(EventFetchJob.class).withIdentity("eventFetchJob", "job_event_fetch_group").build();
    }
    
}
