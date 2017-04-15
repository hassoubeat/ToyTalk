/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import com.hassoubeat.toytalk.quartz.job.JobFactory;
import com.hassoubeat.toytalk.quartz.trigger.MultiFireControlTriggerLisner;
import com.hassoubeat.toytalk.quartz.trigger.TriggerFactory;
import java.util.List;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.impl.matchers.EverythingMatcher.allTriggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class QuartzManager {
    
    // シングルトンインスタンス
    private static final QuartzManager QUARTZ_MANAGER = new QuartzManager();
    
    // ロガー
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private Scheduler scheduler;
    
    /**
     * コンストラクタ
     */
    private QuartzManager() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            // トリガー同時実行を制御するトリガーリスナーの適応
            scheduler.getListenerManager().addTriggerListener(new MultiFireControlTriggerLisner(), allTriggers());
            scheduler.start();
        } catch (SchedulerException ex) {
            // スケジューラの開始に失敗した場合 (想定外エラーとして共通エラーにラッピングしてスローする)
            logger.error("{}:{}",MessageConst.SCHEDULER_GENERATE_ERROR.getId(), MessageConst.SCHEDULER_GENERATE_ERROR.getMessage(), ex);
            throw new ToyTalkException(MessageConst.SCHEDULER_GENERATE_ERROR.getMessage(), ex);
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
        
        System.out.println(restEvent.toString());
        
        // JobとTriggerを取得する
        JobDetail job = JobFactory.getJob(restEvent);
        List<Trigger> triggers = TriggerFactory.getTrigger(restEvent, job);
        for (int index = 0; index < triggers.size(); index++) {
            try {
                Trigger trigger = triggers.get(index);
                if (index == 0) {
                    // 初回のループ時のみ実行
                    scheduler.scheduleJob(job, trigger);
                    logger.info("{}.{} EVENT_ID:{}, EVENT_NAME:{}, JOB_ID:{}, TRIGGER_ID:{}", MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getMessage(), restEvent.getId(), restEvent.getName(), job.getKey().getName(), trigger.getKey().getName());
                } else {
                    // Triggerが複数ある場合に実行
                    scheduler.scheduleJob(trigger);
                    logger.info("{}.{} EVENT_ID:{}, EVENT_NAME:{}, JOB_ID:{}, TRIGGER_ID:{}", MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getMessage(), restEvent.getId(), restEvent.getName(), job.getKey().getName(), trigger.getKey().getName());
                }
            } catch (SchedulerException ex) {
                // トリガーをスケジューラに登録失敗
                logger.warn("{}.{} EVENT_ID:{}, EVENT_NAME:{}, JOB_ID:{}" , MessageConst.FAILED_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.FAILED_REGIST_EVENT_TO_SCHEDULE.getMessage(), restEvent.getId(), restEvent.getName(), job.getKey().getName());
            }
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
    
//    /**
//     * 登録されているイベントを取得する
//     */
//    public void getRegistedEvent() {
//        try {
//            // TODO IDからJobを消す
//            for(String test:scheduler.getCalendarNames()) {
//                System.out.println(test);
//            }
//        } catch (SchedulerException ex) {
//            Logger.getLogger(QuartzManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    /**
     * 現在Quartzに登録されているイベントを全て削除する
     */
    public void eventClear() {
        try {
            scheduler.clear();
            logger.info("{}:{}", MessageConst.SUCCESS_CLEAR_EVENT.getId(), MessageConst.SUCCESS_CLEAR_EVENT.getMessage() );
        } catch (SchedulerException ex) {
            // スケジューラのクリアに失敗した場合 (想定外エラーとして共通エラーにラッピングしてスローする)
            logger.error("{}:{}",MessageConst.SCHEDULER_CLEAR_ERROR.getId(), MessageConst.SCHEDULER_CLEAR_ERROR.getMessage(), ex);
            throw new ToyTalkException(MessageConst.SCHEDULER_CLEAR_ERROR.getMessage(), ex);
        }
    }
    
    /**
     * 定期的にイベントを取得するジョブをスケジュールに登録する
     */
    public void addFetchEvent() {
        // JobとTriggerを取得する
        JobDetail job = JobFactory.getEventFetchJob();
        Trigger trigger = TriggerFactory.getEventFetchTrigger();
        try {
            scheduler.scheduleJob(job, trigger);
            logger.info("{}.{} JOB_ID:{}, TRIGGER_ID:{}", MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.SUCCESS_REGIST_EVENT_TO_SCHEDULE.getMessage(), job.getKey().getName(), trigger.getKey().getName());
        } catch (SchedulerException ex) {
            // ジョブ/イベントをスケジューラに登録失敗
            logger.warn("{}.{} JOB_ID:{}, TRIGGER_ID:{}" , MessageConst.FAILED_REGIST_EVENT_TO_SCHEDULE.getId(), MessageConst.FAILED_REGIST_EVENT_TO_SCHEDULE.getMessage(), job.getKey().toString(), trigger.getKey().toString());
        }
    }
    
    /**
     * インスタンスを返却する
     * @return 
     */
    public static QuartzManager getInstance() {
        return QUARTZ_MANAGER;
    }
}
