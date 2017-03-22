/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.gpio.GpioManager;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import com.hassoubeat.toytalk.quartz.QuartzManager;
import com.hassoubeat.toytalk.rest.RestClient;
import com.hassoubeat.toytalk.util.UtilLogic;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class EventFetchJob implements Job{
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final GpioManager gpio = GpioManager.getInstance();
    private final Viewer viewer = ViewerFactory.getInstance();
    private final UtilLogic utilLogic = UtilLogic.getInstance();
    private final RestClient restClient = RestClient.getInstance();
    private final QuartzManager quartzManager = QuartzManager.getInstance();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        // TODO ToyTalkEventのstartupと同様のため、共通化を図る
        gpio.connectLedOn();
        boolean isNetworkConnection = utilLogic.isOnlineCheck();
        gpio.connectLedOff();
        
        if (!isNetworkConnection) {
            // ネットワークが未接続(Wifiなどが設定されていなかった場合)
            logger.warn("{}:{}", MessageConst.UN_CONNECTED_NETWORK.getId(), MessageConst.UN_CONNECTED_NETWORK.getMessage());
            viewer.displayNetworkUnconnectView();
            return;
        } else {
            // ネットワーク接続済
            
            // 登録済のイベントをクリアする
            quartzManager.eventClear();
            // イベントの取得
            List<RestEvent> restEventList = restClient.fetchAllEvent();
            
            if (restEventList != null) {
                // イベントが取得できた場合
                for (RestEvent restEvent: restEventList) {
                    restEvent.toString();
                    quartzManager.addEvent(restEvent);
                }
            }
            // イベントの定期取得イベントを追加する
            quartzManager.addFetchEvent();
        }
    }
    
}
