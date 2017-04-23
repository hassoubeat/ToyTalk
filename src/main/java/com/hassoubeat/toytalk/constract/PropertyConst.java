/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toytalk.constract;

import java.util.ResourceBundle;

/**
 * 外部プロパティ値を保持するクラス
 * 
 * @author hassoubeat
 */
public class PropertyConst {
    
    
    // ロットナンバー
    public static final String ROT_NUMBER = ResourceBundle.getBundle("toytalk").getString("RotNumber");
    // アクセストークン
    public static final String ACCESS_TOKEN = ResourceBundle.getBundle("toytalk").getString("AccessToken");
    // ToyTalkアプリのバージョン
    public static final String TOY_TALK_VERSION = ResourceBundle.getBundle("toytalk").getString("ToyTalkVersion");
    // イベント取得間隔
    public static final String EVENT_FETCH_INTERVAL = ResourceBundle.getBundle("toytalk").getString("EventFetchInterval");
    // イベント取得期間
    public static final String EVENT_FETCH_TERM = ResourceBundle.getBundle("toytalk").getString("EventFetchTerm");
    
    // ファセットプログラムの実行ファイルまでのパッケージ名
    public static final String FACET_PACKAGE = ResourceBundle.getBundle("PathConfig").getString("facet.package");
    // REST_APIでアクセスする先のリソース
    public static final String REST_RESOURCES_PATH = ResourceBundle.getBundle("PathConfig").getString("rest.resource.path");
    // ファイルを取り扱うルートパス
    public static final String ROOT_PATH = ResourceBundle.getBundle("PathConfig").getString("root.path");
    // ファセットプログラムを保持するパス
    public static final String FACET_LIB_PATH = ResourceBundle.getBundle("PathConfig").getString("facet.lib.path");
    // ファセットプロパティを保持するパス
    public static final String FACET_PROPERTIES_PATH = ResourceBundle.getBundle("PathConfig").getString("facet.properties.path");
    // ファセットプロパティ編集画面を保持するパス
    public static final String FACET_PROPERTIES_EDIT_VIEW_PATH = ResourceBundle.getBundle("PathConfig").getString("facet.properties.edit.view.path");    
}
