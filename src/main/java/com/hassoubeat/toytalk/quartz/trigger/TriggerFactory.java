/*
 * To change bitLogic license header, choose License Headers in Project Properties.
 * To change bitLogic template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.trigger;

import com.hassoubeat.toytalk.constract.EventRoopParamConst;
import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.util.BitLogic;
import com.hassoubeat.toytalk.util.UtilLogic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 動的にQuartzTriggerを生成するクラス
 * @author hassoubeat
 */
public class TriggerFactory {
    
    // ロガー
    private static Logger logger = LoggerFactory.getLogger(TriggerFactory.class);
    
    private static EventRoopParamConst erpConst = new EventRoopParamConst();
    private static BitLogic bitLogic = BitLogic.getInstace();
    private static UtilLogic utilLogic = UtilLogic.getInstance();
    private static final String TRIGGER = "trigger";
    
    // Triggerが属するグループ名を定義する
    public static final String TOY_GROUP = "trigger_toy_group";
    public static final String ACCOUNT_GROUP = "trigger_account_group";
    public static final String FACET_GROUP = "trigger_facet_group";
    
            
    /**
     * イベントの内容によって適したTriggerを発行する
     * @param restEvent 
     * @param tyingJob トリガーに紐付けるJob 
     * @return 動的生成したトリガー
     */
    static public List<Trigger> getTrigger(RestEvent restEvent, JobDetail tyingJob) {
        List<Trigger> triggers = new ArrayList();
        
        int roop = restEvent.getRoop();
        
        // トリガー名の生成
        String triggerName = TRIGGER + Integer.toString(restEvent.getId());
        
        // トリガーが属するグループ名の取得
        String triggerGroupName = "";
        if (restEvent.getToyId() != null) {
            // Toyグループに紐づくイベント
            triggerGroupName = TOY_GROUP;
        }
        if (restEvent.getAccountId() != null) {
            //　アカウントに紐づくイベント
            triggerGroupName = ACCOUNT_GROUP;
        }
        if (restEvent.getFacetId() != null) {
            //　ファセットに紐づくイベント
            triggerGroupName = FACET_GROUP;
        }
        
        if (!bitLogic.bitCheck(roop, erpConst.IS_ROOP)) {
            // イベントの繰り返しが無効の場合
            triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).build());
            logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "ループなし");
            return triggers;
        } 
        
        // 以下、イベントの繰り返しが有効の場合
        
        // 繰り返し間隔値の取得
        int roopInterval = bitLogic.bitAnd(restEvent.getRoop(), bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_DAY_ROOP)) {
            // 日時繰り返しの場合
            
            if (restEvent.getRoopEndDate() == null) {
                // ループの終わりが設定されていなかった場合
                triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "日時ループ");
            } else {
                // ループの終わりが設定されていた場合
                triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "日時ループ");
            }
        }

        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_WEEK_ROOP)) {
            // 週次繰り返しの場合
            
            // イベント開始日時の曜日を取得する
            int eventStartDoW = utilLogic.getDayOfWeek(restEvent.getStartDate());
            
            // 曜日指定の確認
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_SUNDAY)) {
                // 日曜日が指定されていた場合
                Date sundayDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.SUNDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.SUNDAY, triggerGroupName).startAt(sundayDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.SUNDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.SUNDAY, triggerGroupName).startAt(sundayDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.SUNDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_MONDAY)) {
                // 月曜日が指定されていた場合
                Date mondayDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.MONDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.MONDAY, triggerGroupName).startAt(mondayDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.MONDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.MONDAY, triggerGroupName).startAt(mondayDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.MONDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_TUESDAY)) {
                // 火曜日が指定されていた場合
                Date tuesdayDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.TUESDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.TUESDAY, triggerGroupName).startAt(tuesdayDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.TUESDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.TUESDAY, triggerGroupName).startAt(tuesdayDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.TUESDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_WEDNESDAY)) {
                // 水曜日が指定されていた場合
                Date webnesDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.WEDNESDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.WEDNESDAY, triggerGroupName).startAt(webnesDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.WEDNESDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.WEDNESDAY, triggerGroupName).startAt(webnesDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.WEDNESDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_THURSDAY)) {
                // 木曜日が指定されていた場合
                Date thursDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.THURSDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.THURSDAY, triggerGroupName).startAt(thursDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.THURSDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.THURSDAY, triggerGroupName).startAt(thursDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.THURSDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_FRIDAY)) {
                // 金曜日が指定されていた場合
                Date fridayDate = utilLogic.calDate(restEvent.getStartDate(), Calendar.FRIDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.FRIDAY, triggerGroupName).startAt(fridayDate).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.FRIDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.FRIDAY, triggerGroupName).startAt(fridayDate).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.FRIDAY, triggerGroupName, "週ループ");
                }
            }
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_SATURDAY)) {
                // 土曜日が指定されていた場合
                Date saturday = utilLogic.calDate(restEvent.getStartDate(), Calendar.SATURDAY - eventStartDoW, Calendar.DAY_OF_MONTH);
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.SATURDAY, triggerGroupName).startAt(saturday).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).inTimeZone(TimeZone.getTimeZone("Asia/Tokyo")).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.SATURDAY, triggerGroupName, "週ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName  + "-" + Calendar.SATURDAY, triggerGroupName).startAt(saturday).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInWeeks(roopInterval).inTimeZone(TimeZone.getTimeZone("Asia/Tokyo")).withMisfireHandlingInstructionDoNothing()).forJob(tyingJob).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName  + "-" + Calendar.SATURDAY, triggerGroupName, "週ループ");
                }
            }
            
            return triggers;
        }
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_MOUTH_ROOP)) {
            // 月次繰り返しの場合
            
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_STANDARD_DAY)) {
                // 日付基準の繰り返しの場合(毎月 N 日)
                
                if (restEvent.getRoopEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInMonths(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "月ループ");
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInMonths(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                    logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "月ループ");
                }
                
                return triggers;
                
            } else {
                // TODO 曜日基準の繰り返しの場合(毎月 第 N 曜日)
                
            }
        }
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_YEAR_ROOP)) {
            // 年次繰り返しの場合
            
            if (restEvent.getRoopEndDate() == null) {
                // 繰り返しの終わりが設定されていなかった場合
                triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInYears(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "年ループ");
            } else {
                // 繰り返しの終わりが設定されていた場合
                triggers.add(newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getRoopEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInYears(roopInterval).withMisfireHandlingInstructionDoNothing()).build());
                logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{}, ROOP_TYPE:{}" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), triggerName, triggerGroupName, "年ループ");
            }
                
            return triggers;
        }
        
        return triggers;
    }
    
    
    public static Trigger getEventFetchTrigger() {
        ResourceBundle properties = ResourceBundle.getBundle("toytalk");
        String eventFetchInteval = properties.getString("EventFetchInterval");
        
        if (eventFetchInteval == null) {
            // nullだった場合、デフォルトの15分をセット
            eventFetchInteval = "15";
        }
        // 優先度最低(1 ※ デフォルトは5)でトリガーを設定する(実行が他のトリガーと被ると、先にイベントが全削除されて実行されない恐れがあるため)
        Trigger trigger = newTrigger().withIdentity("eventFetchTrigger", "trigger_event_fetch_group").startNow().withSchedule(cronSchedule("0 */" + eventFetchInteval + " * * * ?").withMisfireHandlingInstructionDoNothing()).withPriority(1).build();
        logger.info("{}.{} TRIGGER_NAME:{}, TRIGGER_GROUP_NAME:{} EVENT_FETCH_INTERVAL:{}min" , MessageConst.SUCCESS_CREATE_TRIGGER.getId(), MessageConst.SUCCESS_CREATE_TRIGGER.getMessage(), "eventFetchTrigger", "trigger_event_fetch_group", eventFetchInteval);
        return trigger;
        
    }
    
    /**
     * 引数として渡されたパラメータで繰り返し曜日フラグが一つでも立っていた場合、trueを返す
     * @param param
     * @return 
     */
    private boolean dayOfTheWeekCheck(int param) {
        boolean isSundayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_SUNDAY);
        boolean isMondayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_MONDAY);
        boolean isTuesdayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_TUESDAY);
        boolean isWednesdayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_WEDNESDAY);
        boolean isThursdayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_THURSDAY);
        boolean isFridayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_FRIDAY);
        boolean isSaturdayCheck = bitLogic.bitCheck(param, erpConst.IS_ROOP_SATURDAY);
        
        return isSundayCheck | isMondayCheck | isTuesdayCheck | isWednesdayCheck | isThursdayCheck | isFridayCheck | isSaturdayCheck;
    }
    
}
