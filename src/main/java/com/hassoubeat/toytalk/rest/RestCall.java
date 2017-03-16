/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.rest;

import com.hassoubeat.toytalk.daemon.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.gpio.GPIO;
import com.hassoubeat.toytalk.gpio.ToyTalkEvent;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import com.hassoubeat.toytalk.util.UtilLogic;
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RestAPIのコールクラス
 * @author hassoubeat
 */
public class RestCall {
    
    // ロガー
    private static final Logger logger = LoggerFactory.getLogger(ToyTalkEvent.class);
    // GPIO処理の管理クラス(シングルトン)
    private GPIO gpio = gpio = GPIO.getInstance();
    // 表示処理の管理クラス
    private Viewer viewer = ViewerFactory.getInstace();
    // Utilロジック
    private UtilLogic utilLogic = UtilLogic.getInstace();
    
    final private String HEADER_NAME_ROT_NUMBER = "rotNum";
    final private String HEADER_NAME_AUTHORICATION = "authorication";
    final private String HEADER_NAME_MAC_ADDRESS = "macAddress";
    final private String HEADER_NAME_TOY_TALK_VERSION = "toyTalkVersion";
    
    /**
     * イベントの全取得を行うメソッド
     * @return 
     * @throws java.lang.InterruptedException
     */
    public List<RestEvent> fetchAllEvent () {
        String rotNum = utilLogic.getRotNumber();
        String accessToken = utilLogic.getAccessToken();
        String macAddress = "11:11:11:11:11:13";
        String toyTalkVersion = utilLogic.getToyTalkVersion();

        Client restClient = ClientBuilder.newClient();
        // サーバ接続中LEDをONにする
        gpio.connectLedOn();
        
        List<RestEvent> eventList = null;
        try {
            WebTarget webTarget = restClient.target("http://52.196.234.137:8080/ToyManager/webapi/events/0.1/events");
            eventList = webTarget.request(MediaType.APPLICATION_XML).header(HEADER_NAME_ROT_NUMBER, rotNum).header(HEADER_NAME_AUTHORICATION, accessToken).header(HEADER_NAME_MAC_ADDRESS, macAddress).header(HEADER_NAME_TOY_TALK_VERSION, "").get(new GenericType<List<RestEvent>>(){});
        } catch (NotAuthorizedException ex) {
            // TODO 認証失敗と期限切れを出し分ける
            System.out.println("ExceptionMessage" + ex.getMessage());

        } catch (ForbiddenException ex) {
            // アクセスフィルターが未認証の場合
            viewer.displayAccessFilterUnApprovalView();
            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.ACCESS_FILTER_UN_APPROVAL.getId(), MessageConst.ACCESS_FILTER_UN_APPROVAL.getMessage(), rotNum, accessToken, macAddress, ex);
            System.out.println("ExceptionMessage" + ex.getMessage());
            try {
                sleep(4000);
            } catch (InterruptedException ex1) {
                // TODO 
            }
        } catch (BadRequestException ex) {
            // リクエストパラメータの値に不備がある時
            viewer.displayRequestParamInvalidView();
            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.REQUEST_PARAM_INVALID.getId(), MessageConst.REQUEST_PARAM_INVALID.getMessage(), rotNum, accessToken, macAddress, ex);
            try {
                sleep(4000);
            } catch (InterruptedException ex1) {
                // TODO
            }
        } finally {
            // サーバ接続中LEDをOFFにする
            gpio.connectLedOff();
            // RESTクライアントを閉じる
            restClient.close();
        } 
        
        return eventList;
    }
    
}
