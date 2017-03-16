/*
 * To change bitLogic license header, choose License Headers in Project Properties.
 * To change bitLogic template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.trigger;

import com.hassoubeat.toytalk.daemon.constract.EventRoopParamConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.util.BitLogic;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import org.quartz.SimpleTrigger;
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
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static EventRoopParamConst erpConst = new EventRoopParamConst();
    private static BitLogic bitLogic = BitLogic.getInstace();
    private static final String TRIGGER = "trigger";
    
    // Triggerが属するグループ名を定義する
    public static final String TOY_GROUP = "trigger_toy_group";
    public static final String ACCOUNT_GROUP = "trigger_account_group";
    public static final String FACET_GROUP = "trigger_facet_group";
    
            
    /**
     * イベントの内容によって適したTriggerを発行する
     * @param restEvent 
     * @return 動的生成したトリガー
     */
    static public Trigger getTrigger(RestEvent restEvent) {
        Trigger trigger = null;
        
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
            trigger = (SimpleTrigger) newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).build();
            return trigger;
        } 
        
        // 以下、イベントの繰り返しが有効の場合
        
        // 繰り返し間隔値の取得
        int roopInterval = bitLogic.bitAnd(restEvent.getRoop(), bitLogic.bitNot(erpConst.IS_ROOP_INTERVAL_BIT));
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_DAY_ROOP)) {
            // 日時繰り返しの場合
            
            if (restEvent.getEndDate() == null) {
                // 繰り返しの終わりが設定されていなかった場合
                trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval)).build();
            } else {
                // 繰り返しの終わりが設定されていた場合
                trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInDays(roopInterval)).build();
            }
            
            return trigger;
        }

        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_WEEK_ROOP)) {
            // 週次繰り返しの場合
            // TODO 曜日に合わせてCalenderIntervalTriggerを複数返却するハメになるので、こいつだけ独自実装にする必要ありかも
        }
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_MOUTH_ROOP)) {
            // 月次繰り返しの場合
            
            if (bitLogic.bitCheck(roop, erpConst.IS_ROOP_STANDARD_DAY)) {
                // 日付基準の繰り返しの場合(毎月 N 日)
                
                if (restEvent.getEndDate() == null) {
                    // 繰り返しの終わりが設定されていなかった場合
                    trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInMonths(roopInterval)).build();
                } else {
                    // 繰り返しの終わりが設定されていた場合
                    trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInMonths(roopInterval)).build();
                }
                
                return trigger;
                
            } else {
                // TODO 曜日基準の繰り返しの場合(毎月 第 N 曜日)
                
            }
        }
        
        if (bitLogic.bitCheck(roop, erpConst.IS_EVERY_YEAR_ROOP)) {
            // 年次繰り返しの場合
            
            if (restEvent.getEndDate() == null) {
                // 繰り返しの終わりが設定されていなかった場合
                trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).withSchedule(calendarIntervalSchedule().withIntervalInYears(roopInterval)).build();
            } else {
                // 繰り返しの終わりが設定されていた場合
                trigger = newTrigger().withIdentity(triggerName, triggerGroupName).startAt(restEvent.getStartDate()).endAt(restEvent.getEndDate()).withSchedule(calendarIntervalSchedule().withIntervalInYears(roopInterval)).build();
            }
                
            return trigger;
        }
        
        return trigger;
    }
    

    /**
     * 複数のトリガーを必要とするイベントであるかをチェックする
     * @param restEvent 
     * @return 複数のトリガーが必要とするイベントだった場合(true)
     */
    public static boolean isEventReturnMultiTrigger(RestEvent restEvent) {
        boolean isExist = false;
        
        
        return isExist;
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
