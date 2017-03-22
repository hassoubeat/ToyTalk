/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.trigger;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * トリガーの発報を制御し、同時に処理を実行するを防止するトリガーリスナー(キュー的にFIFOで実行される)
 * @author hassoubeat
 */
public class MultiFireControlTriggerLisner implements TriggerListener{
    
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // トリガーの実行順序を制御するキューリスト
    static private List<String> queueList = new ArrayList();
    

    public MultiFireControlTriggerLisner() {
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void triggerFired(Trigger trgr, JobExecutionContext jec) {
        logger.debug(trgr.toString() + "triggerFired");
        
        // トリガー情報をリストに格納し、最前列になるまで待機する
        queueList.add(trgr.toString());
        while (!(trgr.toString().equals(queueList.get(0)))) {
            // 最初の要素(実行順)になるまでループを繰り返す
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }   
    }

    @Override
    public boolean vetoJobExecution(Trigger trgr, JobExecutionContext jec) {
        logger.debug(trgr.toString() + "vetoJobExecution");
        return false;
    }
    

    @Override
    public void triggerMisfired(Trigger trgr) {
        logger.debug(trgr.toString() + "triggerMisfired");
        
    }

    @Override
    public void triggerComplete(Trigger trgr, JobExecutionContext jec, Trigger.CompletedExecutionInstruction cei) {
        logger.debug(trgr.toString() + "triggerComplete");
        queueList.remove(0);
        
        
    }
    
}
