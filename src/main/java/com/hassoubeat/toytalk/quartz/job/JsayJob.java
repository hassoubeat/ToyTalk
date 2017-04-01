/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.quartz.job;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.io.IOException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class JsayJob implements Job{
    
    // ロガー
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    
    private final String COMMAND = "jsay";
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Runtime runTime = Runtime.getRuntime();
        String jsayCommand = "";
        try {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            jsayCommand = COMMAND + " " + jobDataMap.get("eventName") + "・" + jobDataMap.get("eventContent");
            Process process = runTime.exec(jsayCommand);
            // プロセスの実行が完了するまで、待機する
            process.waitFor();
            
            // ロガーに実行したコマンドを出力
            logger.info("{}:{} COMMAND:{}", MessageConst.SUCCESS_RUN_COMMAND.getId(), MessageConst.SUCCESS_RUN_COMMAND.getMessage(), jsayCommand);
        } catch (IOException | InterruptedException ex) {
            // 実行失敗時の挙動定義
            logger.error("{}:{} COMMAND:{}", MessageConst.FAILED_RUN_COMMAND.getId(), MessageConst.FAILED_RUN_COMMAND.getMessage(), jsayCommand);
            throw new ToyTalkException();
        }
    }
}
