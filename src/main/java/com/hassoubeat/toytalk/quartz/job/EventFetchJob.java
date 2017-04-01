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
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
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
        
        try {
            // TODO 一秒ストップすることで、フェッチの再取得が発生しないようにする
            // どうやらCronの仕様で、1秒以内に再度同じイベントが登録されてしまうともう一度走ってしまうため
            sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("GPIOインスタンス：" + gpio.toString());
        System.out.println("Viewerインスタンス：" + viewer.toString());
        System.out.println("UtilLogicインスタンス：" + utilLogic.toString());
        System.out.println("RestClientインスタンス：" + restClient.toString());
        System.out.println("QuartzManagerインスタンス：" + quartzManager.toString());
        
        
        System.out.println("aaaaaaaaa");
        
        // TODO ToyTalkEventのstartupと同様のため、共通化を図る
        gpio.connectLedOn();
        boolean isNetworkConnection = utilLogic.isOnlineCheck();
        gpio.connectLedOff();
        
        System.out.println("bbbbbbbb");
        
        if (!isNetworkConnection) {
            System.out.println("ccccccccc");
            // ネットワークが未接続(Wifiなどが設定されていなかった場合)
            logger.warn("{}:{}", MessageConst.UN_CONNECTED_NETWORK.getId(), MessageConst.UN_CONNECTED_NETWORK.getMessage());
            viewer.displayNetworkUnconnectView();
            return;
        } else {
            System.out.println("dddddddd");
            // ネットワーク接続済
            
            // 登録済のイベントをクリアする
            quartzManager.eventClear();
            System.out.println("eeeeeeee");
            // イベントの取得
            List<RestEvent> restEventList = restClient.fetchAllEvent();
            
            System.out.println("ffffffff");
            
            if (restEventList != null) {
                System.out.println("gggggggg");
                // イベントが取得できた場合
                for (RestEvent restEvent: restEventList) {
                    restEvent.toString();
                    quartzManager.addEvent(restEvent);
                }
            }
            
            System.out.println("hhhhhhhhh");
        }
    }
    
}
