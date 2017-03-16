/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.util;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author hassoubeat
 */
public class UtilLogic {
    
    private static final Logger logger = LoggerFactory.getLogger(UtilLogic.class.getName());    
    
    private UtilLogic() {
        
    }
    
    /**
     * ロットナンバーを取得する
     * @return String 設定ファイルに記載してあるロットナンバー
     */
    public String getRotNumber() {
        // TODO 実装を書く
        return "114514";
    }
    
    /**
     * アクセストークンを取得する
     * @return String 設定ファイルに記載してあるアクセストークン
     */
    public String getAccessToken() {
        // TODO 実装を書く
        return "aaaaa";
    }
    
    /**
     * ToyTalkアプリケーションのバージョンを取得する
     * @return String ToyTalkアプリケーションのバージョンを取得する
     */
    public String getToyTalkVersion() {
        // TODO 実装を書く
        return "0.1";
    }
    
    /**
     * Macアドレスを取得する
     */
    public void getMacAddress() {
        try {
            String nicaddr = "";
            Enumeration<NetworkInterface> nic = NetworkInterface.getNetworkInterfaces();
            while(nic.hasMoreElements() ) {
                NetworkInterface n = nic.nextElement();

                // インタフェース名を表示
                System.out.print( n.getName() + " : " );

                // MACアドレスをbyte[]で取得
                byte[] addressByte = n.getHardwareAddress();

                // 取得したMACアドレスをStringへ変換
                if( addressByte != null ){
                    for( byte b : addressByte ){
                        nicaddr = nicaddr + String.format( "%02X", b);
                    }
                }
                // 取得したMACアドレスをStringへ変換
                if( addressByte != null ){
                    for( byte b : addressByte ){
                        nicaddr = nicaddr + String.format( "%02X", b);
                    }
                }

                // Stringクラスのnicaddrを表示
                System.out.println( nicaddr );

                nicaddr = "";
            }
        } catch (SocketException ex) {
            // TODO 取得しそこなったこときの処理
        }
        
    }
    
    /**
     * ネットワーク接続があるかを確認する
     * @return 
     */
    public boolean isOnlineCheck (){
        // TODO ネットワーク接続有無確認(もうちょっとましな確認方法ないかな) サーバ接続中LEDの点灯とコンソール出力
        
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
     * インスタンスを返却する
     * @return UtilLogicのインスタンス
     */
    static public UtilLogic getInstace() {
        return new UtilLogic();
    }
    
    
}
