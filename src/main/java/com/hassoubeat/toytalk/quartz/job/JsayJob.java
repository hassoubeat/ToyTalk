/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import java.io.IOException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author hassoubeat
 */
public class JsayJob implements Job{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Runtime r = Runtime.getRuntime();
        try {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            Process process = r.exec("jsay " + jobDataMap.get("eventName") + " " + jobDataMap.get("eventContent"));
            // TODO ロガーに実行したコマンドを出力
            System.out.println("jsay " + jobDataMap.get("eventName") + " " + jobDataMap.get("eventContent"));
        } catch (IOException ex) {
            // TODO 実行失敗時の挙動定義
            System.out.println("失敗した失敗した失敗した失敗した失敗した失敗した失敗した失敗した失敗した失敗した");
        }

    }
}
