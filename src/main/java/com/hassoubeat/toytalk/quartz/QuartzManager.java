/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz;

import com.hassoubeat.toytalk.daemon.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.quartz.job.JobFactory;
import com.hassoubeat.toytalk.quartz.trigger.TriggerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class QuartzManager {
    
    // ロガー
    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private Scheduler scheduler;
    
    /**
     * コンストラクタ
     */
    public QuartzManager() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException ex) {
            // TODO Exception発生時の挙動
        }
        
    }
        
    /**
     * 差分イベントを取得して、スケジューラに登録するメソッド
     */
    public void fetchDiffEvent() {
        
    }
    
    /**
     * スケジュールの登録
     * @param restEvent
     */
    public void addEvent(RestEvent restEvent) {
        
        // JobとTriggerを取得する
        JobDetail job = JobFactory.getJob(restEvent);
        Trigger trigger = TriggerFactory.getTrigger(restEvent);
        
        try {
            scheduler.scheduleJob(job, trigger);
            logger.info("{}.{} EVENT_ID:{}, EVENT_NAME:{}, JOB_ID:{}, TRIGGER_ID:{}", MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getMessage(), restEvent.getId(), restEvent.getName(), "job" + String.valueOf(restEvent.getId()), "trigger" + String.valueOf(restEvent.getId()));
        } catch (SchedulerException ex) {
            // TODO Exception発生時の挙動
            // TODO 登録失敗したJobとトリガーをID出力
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * スケジュールの更新
     * @param restEvent
     */
    public void editEvent(RestEvent restEvent) {
        
        // TODO スケジュールからJobとTriggerを取得する
        // TODO スケジュールにAddする
    }
    
    /**
     * スケジュールの削除
     */
    public void removeEvent() {
        // TODO IDからJobを消す
    }
    
    /**
     * 登録されているイベントを取得する
     */
    public void getRegistedEvent() {
        try {
            // TODO IDからJobを消す
            for(String test:scheduler.getCalendarNames()) {
                System.out.println(test);
            }
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 現在Quartzに登録されているイベントを全て削除する
     */
    public void eventClear() {
        try {
            scheduler.clear();
            logger.info("{}:{}", MessageConst.SUCCESS_CLEAR_EVENT.getId(), MessageConst.SUCCESS_CLEAR_EVENT.getMessage() );
        } catch (SchedulerException ex) {
            // TODO Exception発生時の挙動
        }
    }
    
}
