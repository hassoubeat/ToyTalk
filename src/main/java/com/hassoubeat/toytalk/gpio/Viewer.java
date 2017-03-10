/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author hassoubeat
 */
public interface Viewer {
    
    // 開始画面のID
    public String STARTING_VIEW = "starting_view";
    // サーバアクセス中画面のID
    final public String SERVER_CONNECTING_VIEW = "server_connecting_view";
    // トップ画面のID
    final public String TOP_VIEW = "top_view";
    // モードセレクト画面のID
    final public String MODE_SELECT_VIEW = "mode_select_view";
    // アクセストークン認証失敗画面のID
    final public String AUTH_FAILED_VIEW = "auth_failed_view";
    // システムアップデート中画面のID
    final public String SYSTEM_UPDATING_VIEW = "system_updating_view";
    // イベント同期中画面のID
    final public String SYNCING_EVENT_VIEW = "syncing_event_view";
    // ネットワーク接続なし画面のID
    final public String NETWORK_UNCONNECT_VIEW = "network_unconnect_view";
    // イベント完全再取得画面のID
    final public String FULL_REGET_EVENT_VIEW = "full_reget_event_view";
    // シャットダウン画面のID
    final public String SHUTDOWN_VIEW = "shutdown_view";
    
    
    // メニュー画面に表示するメニュー一覧
    final public List<String> MODE_SELECT_MENU_LIST = Arrays.asList("Event Reset & Event Full Get", "Toy Init", "Return Top");
    
    // モード定義
    final public String EVENT_RESET_MODE = "Event Reset & Event Full Get";
    // モード定義
    final public String TOY_INIT = "Toy Init";
    // モード定義
    final public String RETURN_TOP = "Return Top";
    
    /**
     * 起動画面を表示する
     */
    public void displayStartingView();
    
    /**
     * サーバアクセス中画面を表示する
     */
    public void displayServerConnectingView();
    
    /**
     * TOP画面を表示する
     */
    public void displayTopView();
    
    /**
     * モード選択画面を表示する
     * 
     * @param cursol モード選択画面のカーソルの現在位置
     */
    public void displayModeSelectView(int cursol);
    
    /**
     * 認証失敗選択画面を表示する
     */
    public void displayAuthFailedView();
    
    /**
     * システムアップデート画面を表示する
     */
    public void displaySystemUpdatingView();
    
    /**
     * イベント同期中画面を表示する
     */
    public void displaySyncingEventView();
    
    /**
     * ネットワーク接続なし画面を表示する
     */
    public void displayNetworkUnconnectView();
    
    /**
     * シャットダウン画面を表示する
     */
    public void displayShutdownView();
    
}
