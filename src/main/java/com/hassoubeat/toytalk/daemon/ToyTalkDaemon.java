/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.daemon;

import com.hassoubeat.toytalk.gpio.ToyTalkEvent;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

/**
 *
 * @author hassoubeat
 */
public class ToyTalkDaemon implements Daemon{
    
    ToyTalkEvent toyTalkEvent;
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println(this.getClass().getName() + ":init()");
        
        // GPIOインスタンスの取得
        toyTalkEvent = new ToyTalkEvent();
        toyTalkEvent.startup();
        
    }

    @Override
    public void start() throws Exception {
        System.out.println(this.getClass().getName() + ":start()");
        toyTalkEvent.run();
        
    }

    @Override
    public void stop() throws Exception {
        System.out.println(this.getClass().getName() + ":stop()");
        
        // GPIOインスタンスのシャットダウン
        toyTalkEvent.shutdown();
    }

    @Override
    public void destroy() {
        System.out.println(this.getClass().getName() + ":destroy()");
    }
    
}
