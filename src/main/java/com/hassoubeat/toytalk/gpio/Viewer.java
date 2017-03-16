/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.gpio;

import java.util.Arrays;
import java.util.List;

/**
 * 画面表示の親クラス
 * @author hassoubeat
 */
public abstract class Viewer {
    
    // 表示中ViewのID
    private String showingViewId = "";
    // 選択中のメニューインデックス番号
    private int selectingMenuItemIndex;
    
    
    // 開始画面のID
    public static String STARTING_VIEW = "starting_view";
    // サーバアクセス中画面のID
    final static public String SERVER_CONNECTING_VIEW = "server_connecting_view";
    // トップ画面のID
    final static public String TOP_VIEW = "top_view";
    // モードセレクト画面のID
    final static public String MODE_SELECT_VIEW = "mode_select_view";
    // アクセスフィルター未認証画面のID
    final static public String ACCESS_FILTER_UN_APPROVAL_VIEW = "access_filter_un_approval";
    // リクエストパラメータ不備画面のID
    final static public String REQUEST_PARAM_INVALID_VIEW = "request_param_invalid_view";
    // アクセストークン認証失敗画面のID
    final static public String AUTH_FAILED_VIEW = "auth_failed_view";
    // システムアップデート中画面のID
    final static public String SYSTEM_UPDATING_VIEW = "system_updating_view";
    // イベント同期中画面のID
    final static public String SYNCING_EVENT_VIEW = "syncing_event_view";
    // ネットワーク接続なし画面のID
    final static public String NETWORK_UNCONNECT_VIEW = "network_unconnect_view";
    // シャットダウン画面のID
    final static public String SHUTDOWN_VIEW = "shutdown_view";
    
    // メニュー画面に表示するメニュー一覧
    final static public List<String> MODE_SELECT_MENU_LIST = Arrays.asList("Event Data Reset", "Toy Init", "Return Top");
    
    // イベント再取得画面のID
    final static public String EVENT_RESET_MODE = "Event Data Reset";
    // Toy初期化の画面ID
    final static public String TOY_INIT = "Toy Init";
    // TOP画面に戻る画面ID
    final static public String RETURN_TOP = "Return Top";
    
    /**
     * 起動画面を表示する
     */
    abstract public void displayStartingView();
    
    /**
     * サーバアクセス中画面を表示する
     */
    abstract public void displayServerConnectingView();
    
    /**
     * TOP画面を表示する
     */
    abstract public void displayTopView();
    
    /**
     * ネットワーク接続なし画面を表示する
     */
    abstract public void displayNetworkUnconnectView();
    
    /**
     * モード選択画面を表示する
     */
    abstract public void displayModeSelectView();
    
    /**
     * リクエストパラメータ不備画面を表示する
     */
    abstract public void displayRequestParamInvalidView();
    
    /**
     * アクセスフィルター未認証画面を表示する
     */
    abstract public void displayAccessFilterUnApprovalView();
    
    /**
     * 認証失敗選択画面を表示する
     */
    abstract public void displayAuthFailedView();
    
    /**
     * システムアップデート画面を表示する
     */
    abstract public void displaySystemUpdatingView();
    
    /**
     * イベント同期中画面を表示する
     */
    abstract public void displaySyncingEventView();
    
    /**
     * イベント再取得画面を表示する
     */
    abstract public void displayEventDataResetView();
    
    /**
     * Toyの初期化画面を表示する
     */
    abstract public void displayToyInitView();
    
    /**
     * シャットダウン画面を表示する
     */
    abstract public void displayShutdownView();

    
    public String getShowingViewId() {
        return showingViewId;
    }

    public void setShowingViewId(String showingViewId) {
        this.showingViewId = showingViewId;
    }

    public int getSelectingMenuItemIndex() {
        return selectingMenuItemIndex;
    }

    public void setSelectingMenuItemIndex(int selectingMenuItemIndex) {
        this.selectingMenuItemIndex = selectingMenuItemIndex;
    }
    
    
}
