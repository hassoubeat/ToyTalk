/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.toytalk.entity.RestEvent;
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
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
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
        }
        if (restEvent.getAccountId() != null) {
            // アカウントに紐づくイベント
            jobGroupName = ACCOUNT_GROUP;
        }
        if (restEvent.getFacetId() != null) {
            // ファセットに紐づくイベント
            jobGroupName = FACET_GROUP;
        }
        
        // TODO いずれジョブが増えた場合に、動的にジョブを生成して渡す
        job = newJob(JsayJob.class).withIdentity(jobName, jobGroupName).build();
        job.getJobDataMap().put("eventName", restEvent.getName());
        job.getJobDataMap().put("eventContent", restEvent.getContent());
        
        return job;
    }
    
}
