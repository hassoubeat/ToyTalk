/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.rest;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.constract.PropertyConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.gpio.GpioManager;
import com.hassoubeat.toytalk.gpio.ToyTalkEvent;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import com.hassoubeat.toytalk.quartz.QuartzManager;
import com.hassoubeat.toytalk.util.UtilLogic;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class RestClient {
    
    // シングルトンインスタンス
    private static final RestClient REST_CLIENT = new RestClient();
    
    // ロガー
    private static final Logger logger = LoggerFactory.getLogger(ToyTalkEvent.class);
    private GpioManager gpio = gpio = GpioManager.getInstance();
    private Viewer viewer = ViewerFactory.getInstance();
    private UtilLogic utilLogic = UtilLogic.getInstance();
    private final QuartzManager quartzManager = QuartzManager.getInstance();
    
    final private String HEADER_NAME_ROT_NUMBER = "rotNum";
    final private String HEADER_NAME_AUTHORICATION = "authorication";
    final private String HEADER_NAME_MAC_ADDRESS = "macAddress";
    final private String HEADER_NAME_TOY_TALK_VERSION = "toyTalkVersion";
    
    final private String REST_RESOUCE_PATH = PropertyConst.REST_RESOURCES_PATH;
    final private String FETCH_ALL_EVENT_RESOUCE_PATH = "events/0.1/events";

    private RestClient() {
    }
    
    /**
     * イベントの全取得を行うメソッド
     * @return
     */
    public List<RestEvent> fetchAllEvent () {
        String rotNum = utilLogic.getRotNumber();
        String accessToken = utilLogic.getAccessToken();
        String macAddress = utilLogic.getMacAddress();
        String toyTalkVersion = utilLogic.getToyTalkVersion();

        Client restClient = ClientBuilder.newClient();
        // サーバ接続中LEDをONにする
        gpio.connectLedOn();
        
        List<RestEvent> eventList = null;
        try {
            WebTarget webTarget = restClient.target(REST_RESOUCE_PATH).path(FETCH_ALL_EVENT_RESOUCE_PATH);
            
            // プロパティファイルから取得期間と加算する
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = Calendar.getInstance().getTime();
            Date end = utilLogic.calDate(now, utilLogic.getEventFetchTerm(), Calendar.DAY_OF_MONTH);
            logger.debug("フェッチする期間" + now + "〜" + end);
            eventList = webTarget
                    .queryParam("start", formatter.format(now))
                    .queryParam("end", formatter.format(end))
                    .request(MediaType.APPLICATION_XML_TYPE)
                    .header(HEADER_NAME_ROT_NUMBER, rotNum)
                    .header(HEADER_NAME_AUTHORICATION, accessToken)
                    .header(HEADER_NAME_MAC_ADDRESS, macAddress)
                    .header(HEADER_NAME_TOY_TALK_VERSION, toyTalkVersion)
                    .get(new GenericType<List<RestEvent>>(){});
            
            // RESTクライアントの時差の修正(UTCデフォルト時刻で返却されてくるため、現在のタイムゾーンに修正する)
//            for (int index = 0; index < eventList.size(); index++) {
//                eventList.set(index, retouchTimezone(eventList.get(index)));
//            }
            
        } catch (NotAuthorizedException ex) {
            // TODO 認証失敗と期限切れを出し分ける
            System.out.println("ExceptionMessage" + ex.getMessage());

        } catch (ForbiddenException ex) {
            // アクセスフィルターが未認証の場合
            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.ACCESS_FILTER_UN_APPROVAL.getId(), MessageConst.ACCESS_FILTER_UN_APPROVAL.getMessage(), rotNum, accessToken, macAddress, ex);
            viewer.displayAccessFilterUnApprovalView();
        } catch (BadRequestException ex) {
            // リクエストパラメータの値に不備がある時
            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.REQUEST_PARAM_INVALID.getId(), MessageConst.REQUEST_PARAM_INVALID.getMessage(), rotNum, accessToken, macAddress, ex);
            viewer.displayRequestParamInvalidView();
        } finally {
            // イベントの定期取得イベントを追加する
            quartzManager.addFetchEvent();
            // サーバ接続中LEDをOFFにする
            gpio.connectLedOff();
            // RESTクライアントを閉じる
            restClient.close();
        }
        return eventList;
    }
    
    /**
     * イベント取得が完了した事をToyManagerに通知するメソッド(同期時間の更新)
     */
//    private void fetchEventCompleteNotice() {
//        String rotNum = utilLogic.getRotNumber();
//        String accessToken = utilLogic.getAccessToken();
//        String macAddress = utilLogic.getMacAddress();
//        String toyTalkVersion = utilLogic.getToyTalkVersion();
//
//        Client restClient = ClientBuilder.newClient();
//        // サーバ接続中LEDをONにする
//        gpio.connectLedOn();
//        
//        try {
//            WebTarget webTarget = restClient.target(REST_RESOUCE_PATH).path(FETCH_EVENT_COMPLETE_NOTICE_RESOUCE_PATH);
//            webTarget
//                    .request(MediaType.APPLICATION_XML_TYPE)
//                    .header(HEADER_NAME_ROT_NUMBER, rotNum)
//                    .header(HEADER_NAME_AUTHORICATION, accessToken)
//                    .header(HEADER_NAME_MAC_ADDRESS, macAddress)
//                    .header(HEADER_NAME_TOY_TALK_VERSION, toyTalkVersion)
//                    .get();
//            
//        } catch (NotAuthorizedException ex) {
//            // TODO 認証失敗と期限切れを出し分ける
//            System.out.println("ExceptionMessage" + ex.getMessage());
//
//        } catch (ForbiddenException ex) {
//            // アクセスフィルターが未認証の場合
//            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.ACCESS_FILTER_UN_APPROVAL.getId(), MessageConst.ACCESS_FILTER_UN_APPROVAL.getMessage(), rotNum, accessToken, macAddress, ex);
//            viewer.displayAccessFilterUnApprovalView();
//        } catch (BadRequestException ex) {
//            // リクエストパラメータの値に不備がある時
//            logger.warn("{}:{} ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{}", MessageConst.REQUEST_PARAM_INVALID.getId(), MessageConst.REQUEST_PARAM_INVALID.getMessage(), rotNum, accessToken, macAddress, ex);
//            viewer.displayRequestParamInvalidView();
//        } finally {
//            // サーバ接続中LEDをOFFにする
//            gpio.connectLedOff();
//            // RESTクライアントを閉じる
//            restClient.close();
//        }
//    }
    
    /**
     * Restで受領した際のDate型の時差を現在のシステムタイムゾーンに修正する
     * (クライアント側で受領するときに、勝手にこっちのシステムタイムゾーンに合わせて時間を変更してくるため、元通りにする)
     * @param restEvent 
     */
//    public RestEvent retouchTimezone(RestEvent restEvent) {
//        Date startDate = restEvent.getStartDate();
//        Date endDate = restEvent.getEndDate();
//        Date roopEndDate = restEvent.getRoopEndDate();
//        if (startDate != null) {
//            try {
//                restEvent.setStartDate(utilLogic.retouchTimezone(startDate));
//            } catch (ParseException ex) {
//                ex.printStackTrace();
//            }
//        }
//        if (endDate != null) {
//            try {
//                restEvent.setEndDate(utilLogic.retouchTimezone(endDate));
//            } catch (ParseException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        if (roopEndDate != null) {
//            try {
//                restEvent.setRoopEndDate(utilLogic.retouchTimezone(roopEndDate));
//            } catch (ParseException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return restEvent;
//    }
    
    /**
     * RestClientのインスタンス(シングルトン)を返却するメソッド
     * @return RestClientのシングルトンインスタンス
     */
    public static RestClient getInstance () {
        return REST_CLIENT;
    }
    
}
