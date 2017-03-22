/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;

import com.hassoubeat.toytalk.constract.MessageConst;
import com.hassoubeat.toytalk.exception.ToyTalkException;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hassoubeat
 */
public class UtilLogic {

    private static final Logger logger = LoggerFactory.getLogger(UtilLogic.class.getName());
    
    private static final UtilLogic utilLogic = new UtilLogic();
    
    private final String ROT_NUMBER;
    private final String ACCESS_TOKEN;
    private final String TOY_TALK_VERSION;
    
    private UtilLogic() {
        // プロパティファイルから各種パラメータの取得
        ResourceBundle properties = ResourceBundle.getBundle("toytalk");
        ROT_NUMBER = properties.getString("RotNumber");
        ACCESS_TOKEN = properties.getString("AccessToken");
        TOY_TALK_VERSION = properties.getString("ToyTalkVersion");
    }
    
    /**
     * ロットナンバーを取得する
     * @return String 設定ファイルに記載してあるロットナンバー
     */
    public String getRotNumber() {
        return ROT_NUMBER;
    }
    
    /**
     * アクセストークンを取得する
     * @return String 設定ファイルに記載してあるアクセストークン
     */
    public String getAccessToken() {
        return ACCESS_TOKEN;
    }
    
    /**
     * ToyTalkアプリケーションのバージョンを取得する
     * @return String ToyTalkアプリケーションのバージョンを取得する
     */
    public String getToyTalkVersion() {
        return TOY_TALK_VERSION;
    }
    
    /**
     * Macアドレスを取得する
     * @return 取得したMacアドレス
     */
    public String getMacAddress() {
        try {
            String macAddressHash = "";
            Enumeration<NetworkInterface> nic = NetworkInterface.getNetworkInterfaces();
            while(nic.hasMoreElements()) {
                NetworkInterface networkInterface = nic.nextElement();
                
                // インタフェース名を表示
                logger.debug("NetworkInterface:" + networkInterface.getName() + "," + networkInterface.isUp() + "," + networkInterface.hashCode());
                
                if (networkInterface.isLoopback()) {
                    // ループバックアドレスは無視する
                    continue;
                }
                
                if (!networkInterface.isUp()) {
                    // 稼働していないアドレスは無視する
                    continue;
                }
                
                // ハッシュ情報
                macAddressHash = String.valueOf(networkInterface.hashCode());
            }
            
            if (!"".equals(macAddressHash)) {
                return macAddressHash;
            } else {
                // Macアドレスが存在しなかった場合
                throw new SocketException();
            }
        } catch (SocketException ex) {
            // Macアドレスが取得できなかった例外
            logger.error("{}:{}", MessageConst.MAC_ADDRESS_FETCH_ERROR.getId(), MessageConst.MAC_ADDRESS_FETCH_ERROR.getMessage(), ex);
            throw new ToyTalkException(MessageConst.MAC_ADDRESS_FETCH_ERROR.getMessage() , ex);
        }
    }
    
    /**
     * ネットワーク接続があるかを確認する
     * @return 
     */
    public boolean isOnlineCheck (){
        // TODO ネットワーク接続有無確認(もうちょっとましな確認方法ないかな) 
        
        boolean isNetworkConnection = true;
        try {
            URL url = new URL("http://google.com");
            URLConnection con = url.openConnection();
            con.getInputStream();
        } catch (IOException e) {
            isNetworkConnection = false;
        }
        return isNetworkConnection;
    }
    
    /**
     * 引数で渡された日付から日付の計算を実施する
     * @param targetDate 計算を行うDate型
     * @param calParam 計算する値
     * @return 
     */
    public Date calDate(Date targetDate, int calParam) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(targetDate);
        calender.add(Calendar.DAY_OF_MONTH, calParam);
        return calender.getTime();
    }
    
    /**
     * 引数で渡された日付の曜日を返却する
     * @param date 曜日を求めたい日付
     * @return 曜日(日曜日：1, 月曜日:2, 火曜日:3, 水曜日:4, 木曜日:5, 金曜日6, 土曜日:7)
     */
    public int getDayOfWeek(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        return calender.get(Calendar.DAY_OF_WEEK);     
    }
    
    /**
     * Dateの時差を現在のシステムタイムゾーンに修正して返却する
     * @param date
     * @return 
     * @throws java.text.ParseException 
     */
    public Date retouchTimezone (Date date) throws ParseException {
        // 現在のシステムタイムゾーンを取得する
        Calendar cal = Calendar.getInstance();
        TimeZone systemTimezone = cal.getTimeZone();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        // 一度標準時刻に戻してから再度、現在のタイムゾーンで変換する。
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = formatter.format(date).toUpperCase();
        formatter.setTimeZone(cal.getTimeZone());
        return formatter.parse(dateStr);
    }
    
    /**
     * インスタンスを返却する
     * @return UtilLogicのインスタンス
     */
    static public UtilLogic getInstance() {
        return utilLogic;
    }
    
    
}
