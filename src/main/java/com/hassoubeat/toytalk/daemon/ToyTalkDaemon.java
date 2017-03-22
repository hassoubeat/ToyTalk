/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.daemon;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import com.hassoubeat.toytalk.gpio.ToyTalkEvent;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import java.util.Timer;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class ToyTalkDaemon implements Daemon{
    
    // ロガー
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    Viewer viewer = ViewerFactory.getInstance();
    
    ToyTalkEvent toyTalkEvent;
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        logger.debug(this.getClass().getName() + ":init()");
        
        try {
            // GPIOインスタンスの取得
            toyTalkEvent = new ToyTalkEvent();
            toyTalkEvent.startup();
        } catch (ToyTalkException ex) {
            logger.error("{}.{}", MessageConst.SYSTEM_ERROR.getId(), MessageConst.SYSTEM_ERROR.getMessage(), ex);
            viewer.displaySystemErrorView();
        }
        
    }

    @Override
    public void start() throws Exception {
        logger.debug(this.getClass().getName() + ":start()");
        try {
            toyTalkEvent.run();
        } catch (ToyTalkException ex) {
            logger.error("{}.{}", MessageConst.SYSTEM_ERROR.getId(), MessageConst.SYSTEM_ERROR.getMessage(), ex);
            viewer.displaySystemErrorView();
        }
        
    }

    @Override
    public void stop() throws Exception {
        logger.debug(this.getClass().getName() + ":stop()");
        // GPIOインスタンスのシャットダウン
        toyTalkEvent.shutdown();
    }

    @Override
    public void destroy() {
        logger.debug(this.getClass().getName() + ":destory()");
    }
    
}
