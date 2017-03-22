/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.rest;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.entity.RestEvent;
import com.hassoubeat.toytalk.gpio.GpioManager;
import com.hassoubeat.toytalk.gpio.ToyTalkEvent;
import com.hassoubeat.toytalk.gpio.Viewer;
import com.hassoubeat.toytalk.gpio.ViewerFactory;
import com.hassoubeat.toytalk.util.UtilLogic;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
    // GPIO処理の管理クラス
    private GpioManager gpio = gpio = GpioManager.getInstance();
    // 表示処理の管理クラス
    private Viewer viewer = ViewerFactory.getInstance();
    // Utilロジック
    private UtilLogic utilLogic = UtilLogic.getInstance();
    
    final private String HEADER_NAME_ROT_NUMBER = "rotNum";
    final private String HEADER_NAME_AUTHORICATION = "authorication";
    final private String HEADER_NAME_MAC_ADDRESS = "macAddress";
    final private String HEADER_NAME_TOY_TALK_VERSION = "toyTalkVersion";
    
    final private String resourcePath;
    final private String eventResoucePath = "events/0.1/events";

    private RestClient() {
        // プロパティファイルからREST_APIのリソースパスの取得
        ResourceBundle properties = ResourceBundle.getBundle("PathConfig");
        this.resourcePath = properties.getString("rest.resource.path");
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
            WebTarget webTarget = restClient.target(resourcePath).path(eventResoucePath);
            eventList = webTarget.request(MediaType.APPLICATION_XML_TYPE).header(HEADER_NAME_ROT_NUMBER, rotNum).header(HEADER_NAME_AUTHORICATION, accessToken).header(HEADER_NAME_MAC_ADDRESS, macAddress).header(HEADER_NAME_TOY_TALK_VERSION, "").get(new GenericType<List<RestEvent>>(){});
            
            // RESTクライアントの時差の修正(UTCデフォルト時刻で返却されてくるため、現在のタイムゾーンに修正する)
            for (int index = 0; index < eventList.size(); index++) {
                eventList.set(index, retouchTimezone(eventList.get(index)));
            }
            
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
            // サーバ接続中LEDをOFFにする
            gpio.connectLedOff();
            // RESTクライアントを閉じる
            restClient.close();
        }
        return eventList;
    }
    
    /**
     * Restで受領した際のDate型の時差を現在のシステムタイムゾーンに修正する
     * (クライアント側で受領するときに、勝手にこっちのシステムタイムゾーンに合わせて時間を変更してくるため、元通りにする)
     * @param restEvent 
     */
    public RestEvent retouchTimezone(RestEvent restEvent) {
        Date startDate = restEvent.getStartDate();
        Date endDate = restEvent.getEndDate();
        Date roopEndDate = restEvent.getRoopEndDate();
        if (startDate != null) {
            try {
                restEvent.setStartDate(utilLogic.retouchTimezone(startDate));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        if (endDate != null) {
            try {
                restEvent.setEndDate(utilLogic.retouchTimezone(endDate));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

        if (roopEndDate != null) {
            try {
                restEvent.setRoopEndDate(utilLogic.retouchTimezone(roopEndDate));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return restEvent;
    }
    /**
     * RestClientのインスタンス(シングルトン)を返却するメソッド
     * @return RestClientのシングルトンインスタンス
     */
    public static RestClient getInstance () {
        return REST_CLIENT;
    }
    
}
